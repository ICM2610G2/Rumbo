package com.appnotresponding.rumbo.ui.viewModel

import androidx.lifecycle.ViewModel
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

data class FriendsState(
    val friends: List<User> = emptyList(),
    val searchResults: List<User> = emptyList(),
    val isSearching: Boolean = false,
    val searchError: String? = null,
    val friendIds: Set<String> = emptySet(),
    val pendingRequests: List<User> = emptyList(),
    val sentRequestIds: Set<String> = emptySet()
)

class FriendsViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val dbUsers = FirebaseDatabase.getInstance().getReference("users")
    private val dbFriendships = FirebaseDatabase.getInstance().getReference("friendships")
    private val dbRequests = FirebaseDatabase.getInstance().getReference("friend_requests")
    private val dbSentRequests = FirebaseDatabase.getInstance().getReference("friend_requests_sent")

    private val _uiState = MutableStateFlow(FriendsState())
    val uiState: StateFlow<FriendsState> = _uiState.asStateFlow()

    private var friendsListener: ValueEventListener? = null
    private var requestsListener: ValueEventListener? = null
    private var sentRequestsListener: ValueEventListener? = null
    private var authListener: FirebaseAuth.AuthStateListener? = null

    init {
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val uid = firebaseAuth.currentUser?.uid
            clearAllListeners()
            if (uid != null) {
                listenToFriends(uid)
                listenToRequests(uid)
            } else {
                _uiState.update { FriendsState() }
            }
        }
        auth.addAuthStateListener(authListener!!)
    }

    private fun listenToFriends(myUid: String) {
        friendsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friendIds = mutableSetOf<String>()
                for (child in snapshot.children) {
                    friendIds.add(child.key ?: continue)
                }
                _uiState.update { it.copy(friendIds = friendIds) }
                loadFriendUsers(friendIds.toList())
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        dbFriendships.child(myUid).addValueEventListener(friendsListener!!)
    }

    private val friendListeners = mutableMapOf<String, ValueEventListener>()

    private fun loadFriendUsers(friendIds: List<String>) {
        friendListeners.forEach { (friendId, listener) ->
            dbUsers.child(friendId).removeEventListener(listener)
        }
        friendListeners.clear()

        if (friendIds.isEmpty()) {
            _uiState.update { it.copy(friends = emptyList()) }
            return
        }

        val friendsMap = mutableMapOf<String, User>()
        for (friendId in friendIds) {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        friendsMap[friendId] = user
                        _uiState.update { it.copy(friends = friendsMap.values.toList()) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            friendListeners[friendId] = listener
            dbUsers.child(friendId).addValueEventListener(listener)
        }
    }

    private fun listenToRequests(myUid: String) {
        requestsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val senderIds = snapshot.children.mapNotNull { it.key }
                loadRequestUsers(senderIds)
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        dbRequests.child(myUid).addValueEventListener(requestsListener!!)

        sentRequestsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sentIds = snapshot.children.mapNotNull { it.key }.toSet()
                _uiState.update { it.copy(sentRequestIds = sentIds) }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        dbSentRequests.child(myUid).addValueEventListener(sentRequestsListener!!)
    }

    private val requestUsersMap = mutableMapOf<String, User>()
    private val requestListeners = mutableMapOf<String, ValueEventListener>()

    private fun loadRequestUsers(senderIds: List<String>) {
        requestListeners.forEach { (senderId, listener) ->
            dbUsers.child(senderId).removeEventListener(listener)
        }
        requestListeners.clear()

        if (senderIds.isEmpty()) {
            requestUsersMap.clear()
            _uiState.update { it.copy(pendingRequests = emptyList()) }
            return
        }

        requestUsersMap.keys.retainAll(senderIds)
        _uiState.update { it.copy(pendingRequests = requestUsersMap.values.toList()) }

        for (senderId in senderIds) {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        requestUsersMap[senderId] = user
                        _uiState.update { it.copy(pendingRequests = requestUsersMap.values.toList()) }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            }
            requestListeners[senderId] = listener
            dbUsers.child(senderId).addValueEventListener(listener)
        }
    }

    fun searchUserByName(query: String) {
        val myUid = auth.currentUser?.uid ?: return
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), searchError = null) }
            return
        }
        _uiState.update { it.copy(isSearching = true, searchError = null) }
        dbUsers.get().addOnSuccessListener { snapshot ->
            val results = mutableListOf<User>()
            for (child in snapshot.children) {
                val user = child.getValue(User::class.java) ?: continue
                val fullName = "${user.name} ${user.lastname}".lowercase().trim()
                if (user.id != myUid && fullName.contains(query.lowercase().trim())) {
                    results.add(user)
                }
            }
            _uiState.update {
                it.copy(
                    searchResults = results,
                    isSearching = false,
                    searchError = if (results.isEmpty()) "No se encontraron usuarios" else null
                )
            }
        }.addOnFailureListener {
            _uiState.update { s -> s.copy(isSearching = false, searchError = "Error al buscar") }
        }
    }

    fun addFriend(targetUid: String) {
        val myUid = auth.currentUser?.uid ?: return
        if (targetUid == myUid) return
        
        // Optimistic UI: update sentRequestIds immediately
        _uiState.update { state ->
            val updatedSent = state.sentRequestIds.toMutableSet().apply { add(targetUid) }
            state.copy(sentRequestIds = updatedSent)
        }

        dbRequests.child(targetUid).child(myUid).setValue(true)
        dbSentRequests.child(myUid).child(targetUid).setValue(true)
    }

    fun acceptFriendRequest(senderUid: String) {
        val myUid = auth.currentUser?.uid ?: return

        // 1. Remove request
        dbRequests.child(myUid).child(senderUid).removeValue()
        dbSentRequests.child(senderUid).child(myUid).removeValue()

        // 2. Add mutual friendship
        dbFriendships.child(myUid).child(senderUid).setValue(true)
        dbFriendships.child(senderUid).child(myUid).setValue(true)
    }

    fun declineFriendRequest(senderUid: String) {
        val myUid = auth.currentUser?.uid ?: return

        // Remove request
        dbRequests.child(myUid).child(senderUid).removeValue()
        dbSentRequests.child(senderUid).child(myUid).removeValue()
    }

    fun clearSearch() {
        _uiState.update { it.copy(searchResults = emptyList(), searchError = null) }
    }

    private fun clearAllListeners() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            friendsListener?.let { dbFriendships.child(uid).removeEventListener(it) }
            requestsListener?.let { dbRequests.child(uid).removeEventListener(it) }
            sentRequestsListener?.let { dbSentRequests.child(uid).removeEventListener(it) }
        }
        friendListeners.forEach { (friendId, listener) ->
            dbUsers.child(friendId).removeEventListener(listener)
        }
        friendListeners.clear()
        requestListeners.forEach { (senderId, listener) ->
            dbUsers.child(senderId).removeEventListener(listener)
        }
        requestListeners.clear()

        friendsListener = null
        requestsListener = null
        sentRequestsListener = null
    }

    override fun onCleared() {
        super.onCleared()
        authListener?.let { auth.removeAuthStateListener(it) }
        clearAllListeners()
    }
}
