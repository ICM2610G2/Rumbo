package com.appnotresponding.rumbo.ui.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.appnotresponding.rumbo.models.DropNote
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.VisitedPlace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProfileState(
    val isSavingProfile: Boolean = false,
    val profileError: String = "",
    val profileSuccess: String = "",
    val itineraryHistory: Map<String, Map<String, List<VisitedPlace>>> = emptyMap(),
    val userDropNotes: List<DropNote> = emptyList()
)

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val dbUsers = FirebaseDatabase.getInstance().getReference("users")
    private val dbDropNotes = FirebaseDatabase.getInstance().getReference("dropNotes")
    private val dbItineraryHistory = FirebaseDatabase.getInstance().getReference("itineraryHistory")

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()

    private var dropNotesListener: ValueEventListener? = null
    private var historyListener: ValueEventListener? = null
    private var loadedDropNotesUid: String? = null
    private var loadedHistoryUid: String? = null

    fun updateProfile(
        user: User,
        name: String,
        lastname: String,
        phone: String,
        photoUri: Uri?,
        onSuccess: () -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid ?: user.id
        if (uid.isBlank()) {
            _uiState.update { it.copy(profileError = "No hay usuario autenticado") }
            return
        }

        _uiState.update {
            it.copy(isSavingProfile = true, profileError = "", profileSuccess = "")
        }

        fun saveFields(photoUrl: String?) {
            val updates = mutableMapOf<String, Any?>(
                "name" to name.trim(), "lastname" to lastname.trim(), "phone" to phone.trim()
            )
            if (photoUrl != null) {
                updates["profilePictureUrl"] = photoUrl
            }

            dbUsers.child(uid).updateChildren(updates).addOnSuccessListener {
                    _uiState.update {
                        it.copy(
                            isSavingProfile = false,
                            profileError = "",
                            profileSuccess = "Perfil actualizado"
                        )
                    }
                    onSuccess()
                }.addOnFailureListener { error ->
                    _uiState.update {
                        it.copy(
                            isSavingProfile = false,
                            profileError = error.message ?: "Error al actualizar el perfil"
                        )
                    }
                }
        }

        if (photoUri != null) {
            val storageRef = FirebaseStorage.getInstance("gs://rumbowapp.firebasestorage.app")
                .getReference("profile_pictures/$uid.jpg")
            storageRef.putFile(photoUri).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { url -> saveFields(url.toString()) }
                        .addOnFailureListener { error ->
                            _uiState.update {
                                it.copy(
                                    isSavingProfile = false,
                                    profileError = error.message ?: "Error al obtener la foto"
                                )
                            }
                        }
                }.addOnFailureListener { error ->
                    _uiState.update {
                        it.copy(
                            isSavingProfile = false,
                            profileError = error.message ?: "Error al subir la foto"
                        )
                    }
                }
        } else {
            saveFields(null)
        }
    }

    fun loadItineraryHistory(uid: String) {
        if (uid.isBlank() || loadedHistoryUid == uid) return
        historyListener?.let { oldListener ->
            loadedHistoryUid?.let { oldUid ->
                dbItineraryHistory.child(oldUid).removeEventListener(oldListener)
            }
        }
        loadedHistoryUid = uid
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val grouped = linkedMapOf<String, Map<String, List<VisitedPlace>>>()
                snapshot.children.forEach { citySnapshot ->
                    val days = linkedMapOf<String, List<VisitedPlace>>()
                    citySnapshot.children.forEach { daySnapshot ->
                        val places = daySnapshot.children.mapNotNull {
                            it.getValue(VisitedPlace::class.java)
                        }.sortedByDescending { it.visitedAt }
                        if (places.isNotEmpty()) {
                            days[daySnapshot.key ?: "Sin fecha"] = places
                        }
                    }
                    if (days.isNotEmpty()) {
                        grouped[citySnapshot.key ?: "Sin ciudad"] = days
                    }
                }
                _uiState.update { it.copy(itineraryHistory = grouped) }
            }

            override fun onCancelled(error: DatabaseError) {
                _uiState.update {
                    it.copy(profileError = error.message)
                }
            }
        }
        historyListener = listener
        dbItineraryHistory.child(uid).addValueEventListener(listener)
    }

    fun loadUserDropNotes(uid: String) {
        if (uid.isBlank() || loadedDropNotesUid == uid) return
        dropNotesListener?.let { oldListener ->
            dbDropNotes.removeEventListener(oldListener)
        }
        loadedDropNotesUid = uid
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notes = snapshot.children.mapNotNull {
                    it.getValue(DropNote::class.java)
                }.filter { it.creatorId == uid }.sortedByDescending { it.timestamp }

                _uiState.update { it.copy(userDropNotes = notes) }
            }

            override fun onCancelled(error: DatabaseError) {
                _uiState.update {
                    it.copy(profileError = error.message)
                }
            }
        }
        dropNotesListener = listener
        dbDropNotes.addValueEventListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        dropNotesListener?.let { dbDropNotes.removeEventListener(it) }
        historyListener?.let { listener ->
            loadedHistoryUid?.let { uid ->
                dbItineraryHistory.child(uid).removeEventListener(listener)
            }
        }
    }
}
