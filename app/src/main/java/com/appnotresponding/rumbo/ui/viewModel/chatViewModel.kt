package com.appnotresponding.rumbo.ui.viewModel

import androidx.lifecycle.ViewModel
import com.appnotresponding.rumbo.models.ChatConversation
import com.appnotresponding.rumbo.models.GroupChat
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ChatListState(
    val directChats: List<ChatConversation> = emptyList(),
    val groupChats: List<GroupChat> = emptyList(),
    val selectedChatId: String = "",
    val selectedChatTitle: String = "",
    val selectedChatPhoto: String? = null,
    val isGroupChat: Boolean = false
)

class ChatViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val dbChats = db.getReference("chats")
    private val dbUsers = db.getReference("users")
    private val dbGroupChats = db.getReference("groupChats")

    private val _uiState = MutableStateFlow(ChatListState())
    val uiState: StateFlow<ChatListState> = _uiState.asStateFlow()

    private val chatListeners = mutableListOf<Pair<com.google.firebase.database.DatabaseReference, ValueEventListener>>()
    private val groupListeners = mutableMapOf<String, ValueEventListener>()
    private var authListener: FirebaseAuth.AuthStateListener? = null

    private val userListeners = mutableMapOf<String, ValueEventListener>()
    private val resolvedUsers = mutableMapOf<String, User>()
    private var latestChatsSnapshot: DataSnapshot? = null

    init {
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val uid = firebaseAuth.currentUser?.uid
            clearAllListeners()
            if (uid != null) {
                listenToDirectChats(uid)
            } else {
                _uiState.update { ChatListState() }
            }
        }
        auth.addAuthStateListener(authListener!!)
    }

    private fun setupUserListener(otherUid: String, myUid: String) {
        if (userListeners.containsKey(otherUid)) return
        val userListener = object : ValueEventListener {
            override fun onDataChange(userSnapshot: DataSnapshot) {
                val user = userSnapshot.getValue(User::class.java)
                if (user != null) {
                    resolvedUsers[otherUid] = user
                    rebuildConversationsList(myUid)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        userListeners[otherUid] = userListener
        dbUsers.child(otherUid).addValueEventListener(userListener)
    }

    private fun rebuildConversationsList(myUid: String) {
        val snapshot = latestChatsSnapshot ?: return
        val conversations = mutableListOf<ChatConversation>()
        val children = snapshot.children.toList()

        if (children.isEmpty()) {
            _uiState.update { it.copy(directChats = emptyList()) }
            return
        }

        var pending = children.size
        if (pending == 0) {
            _uiState.update { it.copy(directChats = emptyList()) }
            return
        }

        for (child in children) {
            val chatId = child.key
            if (chatId == null) {
                pending--
                if (pending == 0) {
                    _uiState.update { it.copy(directChats = conversations.sortedByDescending { c -> c.lastMessageTimestamp }) }
                }
                continue
            }
            val participants = child.child("participants").children.map { it.value as? String ?: "" }
            if (!participants.contains(myUid)) {
                pending--
                if (pending == 0) {
                    _uiState.update { it.copy(directChats = conversations.sortedByDescending { c -> c.lastMessageTimestamp }) }
                }
                continue
            }
            val otherUid = participants.firstOrNull { it != myUid }
            if (otherUid == null) {
                pending--
                if (pending == 0) {
                    _uiState.update { it.copy(directChats = conversations.sortedByDescending { c -> c.lastMessageTimestamp }) }
                }
                continue
            }
            val lastMessage = child.child("lastMessage").value as? String ?: ""
            val lastTimestamp = child.child("lastMessageTimestamp").value as? Long ?: 0L

            db.getReference("friendships").child(myUid).child(otherUid).get().addOnSuccessListener { friendshipSnap ->
                val areFriends = friendshipSnap.exists() && friendshipSnap.value == true
                if (!areFriends) {
                    pending--
                    if (pending == 0) {
                        _uiState.update { it.copy(directChats = conversations.sortedByDescending { c -> c.lastMessageTimestamp }) }
                    }
                    return@addOnSuccessListener
                }

                val user = resolvedUsers[otherUid]
                if (user != null) {
                    conversations.add(
                        ChatConversation(
                            chatId = chatId,
                            otherUserId = otherUid,
                            otherUserName = user.name,
                            otherUserPhotoUrl = user.profilePictureUrl,
                            otherUserActivity = user.activity,
                            lastMessage = lastMessage,
                            lastMessageTimestamp = lastTimestamp
                        )
                    )
                } else {
                    setupUserListener(otherUid, myUid)
                }
                pending--
                if (pending == 0) {
                    _uiState.update { it.copy(directChats = conversations.sortedByDescending { c -> c.lastMessageTimestamp }) }
                }
            }.addOnFailureListener {
                pending--
                if (pending == 0) {
                    _uiState.update { it.copy(directChats = conversations.sortedByDescending { c -> c.lastMessageTimestamp }) }
                }
            }
        }
    }

    private fun listenToDirectChats(myUid: String) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                latestChatsSnapshot = snapshot
                rebuildConversationsList(myUid)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        dbChats.addValueEventListener(listener)
        chatListeners.add(Pair(dbChats, listener))
    }

    fun listenToGroupChats(itinerary: List<Place>) {
        val myUid = auth.currentUser?.uid ?: return
        val currentPlaceIds = itinerary.map { it.id }.toSet()

        val toRemove = groupListeners.keys - currentPlaceIds
        for (placeId in toRemove) {
            val listener = groupListeners[placeId]
            if (listener != null) {
                dbGroupChats.child(placeId).removeEventListener(listener)
            }
            groupListeners.remove(placeId)
        }
        
        _uiState.update { state -> 
            state.copy(groupChats = state.groupChats.filter { it.placeId in currentPlaceIds })
        }

        for (place in itinerary) {
            if (groupListeners.containsKey(place.id)) continue

            val ref = dbGroupChats.child(place.id)
            ref.child("participants").child(myUid).setValue(true)
            ref.child("placeId").setValue(place.id)
            ref.child("placeName").setValue(place.name)

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val placeId = snapshot.child("placeId").value as? String ?: return@onDataChange
                    val placeName = snapshot.child("placeName").value as? String ?: ""
                    val lastMessage = snapshot.child("lastMessage").value as? String ?: ""
                    val lastTimestamp = snapshot.child("lastMessageTimestamp").value as? Long ?: 0L
                    val mutedByMap = mutableMapOf<String, Boolean>()
                    for (muteChild in snapshot.child("mutedBy").children) {
                        val muteKey = muteChild.key ?: continue
                        mutedByMap[muteKey] = muteChild.value as? Boolean ?: false
                    }

                    val group = GroupChat(
                        placeId = placeId,
                        placeName = placeName,
                        lastMessage = lastMessage,
                        lastMessageTimestamp = lastTimestamp,
                        mutedBy = mutedByMap
                    )

                    val current = _uiState.value.groupChats.toMutableList()
                    val idx = current.indexOfFirst { it.placeId == placeId }
                    if (idx >= 0) current[idx] = group else current.add(group)
                    _uiState.update { it.copy(groupChats = current.toList()) }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            ref.addValueEventListener(listener)
            groupListeners[place.id] = listener
        }
    }

    fun selectDirectChat(chatId: String, chatTitle: String, photoUrl: String?) {
        _uiState.update {
            it.copy(
                selectedChatId = chatId,
                selectedChatTitle = chatTitle,
                selectedChatPhoto = photoUrl,
                isGroupChat = false
            )
        }
    }

    fun selectGroupChat(placeId: String, placeName: String) {
        _uiState.update {
            it.copy(
                selectedChatId = placeId,
                selectedChatTitle = placeName,
                selectedChatPhoto = null,
                isGroupChat = true
            )
        }
    }

    fun getOrCreateDirectChatId(myUid: String, friendUid: String): String {
        val sorted = listOf(myUid, friendUid).sorted()
        return "${sorted[0]}_${sorted[1]}"
    }

    fun leaveGroup(placeId: String) {
        val myUid = auth.currentUser?.uid ?: return
        dbGroupChats.child(placeId).child("participants").child(myUid).removeValue()
        val current = _uiState.value.groupChats.toMutableList()
        current.removeAll { it.placeId == placeId }
        _uiState.update { it.copy(groupChats = current.toList()) }
    }

    fun muteGroup(placeId: String) {
        val myUid = auth.currentUser?.uid ?: return
        dbGroupChats.child(placeId).child("mutedBy").child(myUid).setValue(true)
    }

    fun unmuteGroup(placeId: String) {
        val myUid = auth.currentUser?.uid ?: return
        dbGroupChats.child(placeId).child("mutedBy").child(myUid).removeValue()
    }

    private fun clearAllListeners() {
        for ((ref, listener) in chatListeners) {
            ref.removeEventListener(listener)
        }
        chatListeners.clear()
        
        for ((placeId, listener) in groupListeners) {
            dbGroupChats.child(placeId).removeEventListener(listener)
        }
        groupListeners.clear()

        for ((otherUid, listener) in userListeners) {
            dbUsers.child(otherUid).removeEventListener(listener)
        }
        userListeners.clear()
        resolvedUsers.clear()
        latestChatsSnapshot = null
    }

    override fun onCleared() {
        super.onCleared()
        authListener?.let { auth.removeAuthStateListener(it) }
        clearAllListeners()
    }
}
