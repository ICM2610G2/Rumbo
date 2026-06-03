package com.appnotresponding.rumbo.ui.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel que mantiene el conteo de mensajes no leídos en tiempo real.
 * Agrega los chats directos ("chats") y grupales ("groupChats") del usuario autenticado.
 * Los listeners de Firebase se liberan en [onCleared] para evitar memory leaks.
 */
class UnreadCountViewModel : ViewModel() {

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val directRef = FirebaseDatabase.getInstance().getReference("chats")
    private val groupRef = FirebaseDatabase.getInstance().getReference("groupChats")

    private var directUnread = 0
    private var groupUnread = 0

    private var directListener: ValueEventListener? = null
    private var groupListener: ValueEventListener? = null

    init {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) setupListeners(uid)
    }

    private fun setupListeners(uid: String) {
        fun readUnread(snapshot: DataSnapshot): Int =
            snapshot.children.sumOf { child ->
                child.child("unreadCounts").child(uid).getValue(Int::class.java)
                    ?: child.child("unreadCounts").child(uid).getValue(Long::class.java)?.toInt()
                    ?: 0
            }

        directListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                directUnread = readUnread(snapshot)
                _unreadCount.value = directUnread + groupUnread
            }
            override fun onCancelled(error: DatabaseError) {}
        }

        groupListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupUnread = readUnread(snapshot)
                _unreadCount.value = directUnread + groupUnread
            }
            override fun onCancelled(error: DatabaseError) {}
        }

        directListener?.let { directRef.addValueEventListener(it) }
        groupListener?.let { groupRef.addValueEventListener(it) }
    }

    override fun onCleared() {
        super.onCleared()
        directListener?.let { directRef.removeEventListener(it) }
        groupListener?.let { groupRef.removeEventListener(it) }
    }
}
