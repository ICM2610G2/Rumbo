package com.appnotresponding.rumbo.ui.viewModel

import androidx.lifecycle.ViewModel
import com.appnotresponding.rumbo.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("users")
    private val connectedRef = database.getReference(".info/connected")
    private var presenceListener: ValueEventListener? = null

    private val _currentUserState = MutableStateFlow<User?>(null)
    val currentUserState: StateFlow<User?> = _currentUserState.asStateFlow()

    init {
        // Escucha cambios en el estado de autenticación de Firebase
        auth.addAuthStateListener { firebaseAuth ->
            val uid = firebaseAuth.currentUser?.uid
            if (uid != null) {
                fetchUserData(uid)
                setupPresence(uid)
            } else {
                _currentUserState.value = null
            }
        }
    }

    private fun setupPresence(uid: String) {
        presenceListener?.let { connectedRef.removeEventListener(it) }
        val userStatusRef = dbRef.child(uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue(Boolean::class.java) != true) return
                userStatusRef.child("isOnline").onDisconnect().setValue(false)
                userStatusRef.child("lastSeenAt").onDisconnect().setValue(ServerValue.TIMESTAMP)
                userStatusRef.child("isOnline").setValue(true)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        presenceListener = listener
        connectedRef.addValueEventListener(listener)
    }

    private fun fetchUserData(uid: String) {
        android.util.Log.d("UserViewModel", "Starting ValueEventListener for uid: $uid")
        dbRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val user = snapshot.getValue(User::class.java)
                    android.util.Log.d("UserViewModel", "fetchUserData success: user=${user?.name}, sharingLocation=${user?.sharingLocation}")
                    _currentUserState.value = user
                } catch (e: Exception) {
                    android.util.Log.e("UserViewModel", "Error deserializing User object: ${e.message}", e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                android.util.Log.e("UserViewModel", "fetchUserData cancelled: ${error.message}")
            }
        })
    }

    fun toggleLocationSharing(isSharing: Boolean) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            android.util.Log.e("UserViewModel", "Cannot toggle location sharing: user not authenticated (uid is null)")
            return
        }
        android.util.Log.d("UserViewModel", "Toggling location sharing to $isSharing for uid: $uid")
        dbRef.child(uid).child("sharingLocation").setValue(isSharing)
            .addOnSuccessListener {
                android.util.Log.d("UserViewModel", "Location sharing successfully set to $isSharing in DB")
            }
            .addOnFailureListener { e ->
                android.util.Log.e("UserViewModel", "Failed to set location sharing to $isSharing: ${e.message}", e)
            }
    }

    fun updateActivity(activity: String?) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            android.util.Log.e("UserViewModel", "Cannot update activity: user not authenticated (uid is null)")
            return
        }
        android.util.Log.d("UserViewModel", "Updating activity to $activity for uid: $uid")
        dbRef.child(uid).child("activity").setValue(activity)
            .addOnSuccessListener {
                android.util.Log.d("UserViewModel", "Activity successfully set to $activity in DB")
            }
            .addOnFailureListener { e ->
                android.util.Log.e("UserViewModel", "Failed to set activity to $activity: ${e.message}", e)
            }
    }

    override fun onCleared() {
        super.onCleared()
        presenceListener?.let { connectedRef.removeEventListener(it) }
        auth.currentUser?.uid?.let { uid ->
            dbRef.child(uid).child("isOnline").setValue(false)
            dbRef.child(uid).child("lastSeenAt").setValue(ServerValue.TIMESTAMP)
        }
    }
}

