package com.appnotresponding.rumbo.ui.viewModel

import androidx.lifecycle.ViewModel
import com.appnotresponding.rumbo.models.ChatMessage
import com.appnotresponding.rumbo.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ChatThreadState(
    val messages: List<ChatMessage> = emptyList(),
    val isSending: Boolean = false,
    val messageAuthors: Map<String, User> = emptyMap()
)

class ChatThreadViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _uiState = MutableStateFlow(ChatThreadState())
    val uiState: StateFlow<ChatThreadState> = _uiState.asStateFlow()

    private var currentListener: ValueEventListener? = null
    private var currentRef: com.google.firebase.database.DatabaseReference? = null

    private val dbUsers = db.getReference("users")
    private val userCache = mutableMapOf<String, User>()
    private val userListeners = mutableMapOf<String, ValueEventListener>()

    private fun clearUserListeners() {
        userListeners.forEach { (senderId, listener) ->
            dbUsers.child(senderId).removeEventListener(listener)
        }
        userListeners.clear()
        userCache.clear()
    }

    private fun resolveUsersAndEmit(rawMessages: List<ChatMessage>, extraUid: String? = null) {
        val uniqueSenderIds = (rawMessages.map { it.senderId } + listOfNotNull(extraUid)).distinct()

        fun pushState() {
            val authorsMap = userCache.toMap()
            _uiState.update {
                it.copy(messages = rawMessages, messageAuthors = authorsMap)
            }
        }

        uniqueSenderIds.forEach { senderId ->
            if (!userCache.containsKey(senderId) && !userListeners.containsKey(senderId)) {
                val listener = object : ValueEventListener {
                    override fun onDataChange(userSnapshot: DataSnapshot) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            userCache[senderId] = user
                        }
                        pushState()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
                userListeners[senderId] = listener
                dbUsers.child(senderId).addValueEventListener(listener)
            }
        }

        pushState()
    }

    fun listenToMessages(chatId: String) {
        clearUserListeners()
        currentRef?.let { ref ->
            currentListener?.let { ref.removeEventListener(it) }
        }

        val ref = db.getReference("messages").child(chatId)
        currentRef = ref

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<ChatMessage>()
                for (child in snapshot.children) {
                    val msg = child.getValue(ChatMessage::class.java) ?: continue
                    messages.add(msg)
                }
                val parts = chatId.split("_")
                val myUid = auth.currentUser?.uid ?: ""
                val otherUid = parts.firstOrNull { it != myUid }
                resolveUsersAndEmit(messages.sortedBy { m -> m.timestamp }, otherUid)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        currentListener = listener
        ref.addValueEventListener(listener)
    }

    fun listenToGroupMessages(placeId: String) {
        clearUserListeners()
        currentRef?.let { ref ->
            currentListener?.let { ref.removeEventListener(it) }
        }

        val ref = db.getReference("groupMessages").child(placeId)
        currentRef = ref

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<ChatMessage>()
                for (child in snapshot.children) {
                    val msg = child.getValue(ChatMessage::class.java) ?: continue
                    messages.add(msg)
                }
                resolveUsersAndEmit(messages.sortedBy { m -> m.timestamp })
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        currentListener = listener
        ref.addValueEventListener(listener)
    }

    fun sendMessage(chatId: String, text: String) {
        val myUid = auth.currentUser?.uid ?: return
        if (text.isBlank()) return

        _uiState.update { it.copy(isSending = true) }
        
        val participants = chatId.split("_")
        if (participants.size == 2) {
            db.getReference("chats").child(chatId).child("participants").setValue(participants)
        }

        val ref = db.getReference("messages").child(chatId)
        val msgId = ref.push().key ?: return
        val msg = ChatMessage(
            id = msgId,
            senderId = myUid,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        ref.child(msgId).setValue(msg).addOnSuccessListener {
            db.getReference("chats").child(chatId).child("lastMessage").setValue(text)
            db.getReference("chats").child(chatId).child("lastMessageTimestamp").setValue(msg.timestamp)
            _uiState.update { it.copy(isSending = false) }
        }.addOnFailureListener {
            _uiState.update { it.copy(isSending = false) }
        }
    }

    fun sendGroupMessage(placeId: String, senderName: String, text: String) {
        val myUid = auth.currentUser?.uid ?: return
        if (text.isBlank()) return

        _uiState.update { it.copy(isSending = true) }
        val ref = db.getReference("groupMessages").child(placeId)
        val msgId = ref.push().key ?: return
        val msg = ChatMessage(
            id = msgId,
            senderId = myUid,
            senderName = senderName,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        ref.child(msgId).setValue(msg).addOnSuccessListener {
            db.getReference("groupChats").child(placeId).child("lastMessage").setValue("${senderName}: $text")
            db.getReference("groupChats").child(placeId).child("lastMessageTimestamp").setValue(msg.timestamp)
            _uiState.update { it.copy(isSending = false) }
        }.addOnFailureListener {
            _uiState.update { it.copy(isSending = false) }
        }
    }

    fun createDirectChatIfNeeded(chatId: String, myUid: String, friendUid: String) {
        val ref = db.getReference("chats").child(chatId)
        ref.child("participants").setValue(listOf(myUid, friendUid))
    }

    fun sendLocationMessage(chatId: String, senderName: String?, latitude: Double, longitude: Double, isGroup: Boolean) {
        val myUid = auth.currentUser?.uid ?: return
        _uiState.update { it.copy(isSending = true) }

        val ref = if (isGroup) db.getReference("groupMessages").child(chatId) else db.getReference("messages").child(chatId)
        val msgId = ref.push().key ?: return
        val textValue = "Ubicación: $latitude, $longitude"
        val msg = ChatMessage(
            id = msgId,
            senderId = myUid,
            senderName = senderName ?: "",
            text = textValue,
            timestamp = System.currentTimeMillis(),
            type = "location"
        )

        ref.child(msgId).setValue(msg).addOnSuccessListener {
            if (isGroup) {
                db.getReference("groupChats").child(chatId).child("lastMessage").setValue("${senderName ?: ""}: 📍 Ubicación")
                db.getReference("groupChats").child(chatId).child("lastMessageTimestamp").setValue(msg.timestamp)
            } else {
                val parts = chatId.split("_")
                if (parts.size == 2) {
                    val friendUid = if (parts[0] == myUid) parts[1] else parts[0]
                    createDirectChatIfNeeded(chatId, myUid, friendUid)
                }
                db.getReference("chats").child(chatId).child("lastMessage").setValue("📍 Ubicación")
                db.getReference("chats").child(chatId).child("lastMessageTimestamp").setValue(msg.timestamp)
            }
            _uiState.update { it.copy(isSending = false) }
        }.addOnFailureListener {
            _uiState.update { it.copy(isSending = false) }
        }
    }

    fun sendMediaMessage(chatId: String, senderName: String?, uri: Uri, isGroup: Boolean, mediaType: String) {
        val myUid = auth.currentUser?.uid ?: return
        _uiState.update { it.copy(isSending = true) }

        val storageRef = storage.reference.child("chat_media").child(chatId).child("${System.currentTimeMillis()}_${myUid}")
        storageRef.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                val ref = if (isGroup) db.getReference("groupMessages").child(chatId) else db.getReference("messages").child(chatId)
                val msgId = ref.push().key ?: return@addOnSuccessListener
                val msg = ChatMessage(
                    id = msgId,
                    senderId = myUid,
                    senderName = senderName ?: "",
                    text = if (mediaType == "image") "📷 Imagen" else "🎤 Nota de voz",
                    timestamp = System.currentTimeMillis(),
                    type = mediaType,
                    mediaUrl = downloadUrl.toString()
                )
                
                ref.child(msgId).setValue(msg).addOnSuccessListener {
                    if (isGroup) {
                        db.getReference("groupChats").child(chatId).child("lastMessage").setValue("${senderName ?: ""}: ${msg.text}")
                        db.getReference("groupChats").child(chatId).child("lastMessageTimestamp").setValue(msg.timestamp)
                    } else {
                        val parts = chatId.split("_")
                        if (parts.size == 2) {
                            val friendUid = if (parts[0] == myUid) parts[1] else parts[0]
                            createDirectChatIfNeeded(chatId, myUid, friendUid)
                        }
                        db.getReference("chats").child(chatId).child("lastMessage").setValue(msg.text)
                        db.getReference("chats").child(chatId).child("lastMessageTimestamp").setValue(msg.timestamp)
                    }
                    _uiState.update { state -> state.copy(isSending = false) }
                }.addOnFailureListener {
                    _uiState.update { state -> state.copy(isSending = false) }
                }
            }
        }.addOnFailureListener {
            _uiState.update { it.copy(isSending = false) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearUserListeners()
        currentRef?.let { ref ->
            currentListener?.let { ref.removeEventListener(it) }
        }
    }
}
