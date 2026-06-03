package com.appnotresponding.rumbo.ui.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.appnotresponding.rumbo.models.DropNote
import com.appnotresponding.rumbo.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DropNoteState(
    val dropNotes: List<DropNote> = emptyList(),
    val dropNoteAuthors: Map<String, User> = emptyMap(),
    val isUploadingNote: Boolean = false,
    val noteUploadError: String = ""
)

private const val TAG = "DropNoteVM"
private const val EXPIRATION_MS = 12 * 60 * 60 * 1000L // 12 horas

class DropNoteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DropNoteState())
    val uiState: StateFlow<DropNoteState> = _uiState.asStateFlow()

    private val dbDropNotes = FirebaseDatabase.getInstance().getReference("dropNotes")
    private val dbUsers = FirebaseDatabase.getInstance().getReference("users")

    /** Cache en memoria para usuarios ya resueltos. */
    private val userCache = mutableMapOf<String, User>()

    /** Listeners activos de usuarios (para hacer removeEventListener en onCleared). */
    private val userListeners = mutableMapOf<String, ValueEventListener>()

    init {
        fetchDropNotes()
    }

    private fun fetchDropNotes() {
        dbDropNotes.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notes = mutableListOf<DropNote>()
                val currentTime = System.currentTimeMillis()

                Log.d(TAG, "fetchDropNotes: total children = ${snapshot.childrenCount}")

                for (child in snapshot.children) {
                    val note = child.getValue(DropNote::class.java) ?: continue
                    val isExpired = (currentTime - note.timestamp) > EXPIRATION_MS

                    if (note.public && !isExpired) {
                        notes.add(note)
                        Log.d(TAG, "Nota activa: id=${note.id} creatorId=${note.creatorId}")
                    } else if (note.public && isExpired) {
                        // TODO: migrar a Cloud Function para no hacer escrituras desde el cliente
                        dbDropNotes.child(note.id).child("public").setValue(false)
                        Log.d(TAG, "Nota expirada y desactivada: id=${note.id}")
                    }
                }

                Log.d(TAG, "Notas activas encontradas: ${notes.size}")
                resolveUsersAndEmit(notes)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "fetchDropNotes cancelado: ${error.message}")
            }
        })
    }

    private fun resolveUsersAndEmit(rawNotes: List<DropNote>) {
        val uniqueCreatorIds = rawNotes.map { it.creatorId }.distinct()
        Log.d(TAG, "resolveUsers: creatorIds únicos = $uniqueCreatorIds")

        fun pushState() {
            val authorsMap = userCache.toMap()
            Log.d(TAG, "pushState: usuarios en caché = ${authorsMap.keys}")
            _uiState.update {
                it.copy(dropNotes = rawNotes, dropNoteAuthors = authorsMap)
            }
        }

        uniqueCreatorIds.forEach { creatorId ->
            if (!userCache.containsKey(creatorId) && !userListeners.containsKey(creatorId)) {
                Log.d(TAG, "Cargando usuario desde Firebase: $creatorId")
                val listener = object : ValueEventListener {
                    override fun onDataChange(userSnapshot: DataSnapshot) {
                        Log.d(TAG, "Usuario raw snapshot[$creatorId]: ${userSnapshot.value}")
                        val user = userSnapshot.getValue(User::class.java)
                        Log.d(TAG, "Usuario parseado[$creatorId]: $user")
                        if (user != null) {
                            val isOnlineVal = userSnapshot.child("isOnline").getValue(Boolean::class.java) ?: false
                            userCache[creatorId] = user.copy(isOnline = isOnlineVal)
                            Log.d(TAG, "Usuario cacheado[$creatorId]: avatarUrl=${user.profilePictureUrl}")
                        } else {
                            Log.w(TAG, "Usuario nulo para creatorId=$creatorId — verifica users/$creatorId en Firebase")
                        }
                        pushState()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Error al cargar usuario[$creatorId]: ${error.message}")
                    }
                }
                userListeners[creatorId] = listener
                dbUsers.child(creatorId).addValueEventListener(listener)
            } else {
                Log.d(TAG, "Usuario ya en caché o listener activo: $creatorId")
            }
        }

        // Emitir estado inmediato con lo que ya hay en caché
        pushState()
    }

    fun uploadAndSaveDropNote(
        content: String,
        imageUri: Uri?,
        latitude: Double,
        longitude: Double,
        creatorId: String,
        onSuccess: () -> Unit
    ) {
        val noteId = dbDropNotes.push().key ?: java.util.UUID.randomUUID().toString()
        _uiState.update { it.copy(isUploadingNote = true, noteUploadError = "") }

        if (imageUri != null) {
            val storageRef = FirebaseStorage.getInstance("gs://rumbowapp.firebasestorage.app")
                .getReference("drop_notes/$noteId.jpg")
            storageRef.putFile(imageUri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        saveDropNoteMetadata(
                            noteId = noteId,
                            content = content,
                            imageUrl = downloadUrl.toString(),
                            latitude = latitude,
                            longitude = longitude,
                            creatorId = creatorId,
                            onSuccess = onSuccess
                        )
                    }.addOnFailureListener { e ->
                        _uiState.update {
                            it.copy(isUploadingNote = false, noteUploadError = e.message ?: "Error al obtener URL")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    _uiState.update {
                        it.copy(isUploadingNote = false, noteUploadError = e.message ?: "Error al subir imagen")
                    }
                }
        } else {
            saveDropNoteMetadata(
                noteId = noteId,
                content = content,
                imageUrl = null,
                latitude = latitude,
                longitude = longitude,
                creatorId = creatorId,
                onSuccess = onSuccess
            )
        }
    }

    private fun saveDropNoteMetadata(
        noteId: String,
        content: String,
        imageUrl: String?,
        latitude: Double,
        longitude: Double,
        creatorId: String,
        onSuccess: () -> Unit
    ) {
        val dropNote = DropNote(
            id = noteId,
            creatorId = creatorId,
            content = content,
            imageUrl = imageUrl,
            timestamp = System.currentTimeMillis(),
            latitude = latitude,
            longitude = longitude,
            public = true
        )
        dbDropNotes.child(noteId).setValue(dropNote)
            .addOnSuccessListener {
                _uiState.update { it.copy(isUploadingNote = false) }
                onSuccess()
            }
            .addOnFailureListener { e ->
                _uiState.update {
                    it.copy(
                        isUploadingNote = false,
                        noteUploadError = e.message ?: "Error al guardar en base de datos"
                    )
                }
            }
    }

    fun deleteDropNote(
        noteId: String,
        imageUrl: String?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        dbDropNotes.child(noteId).removeValue()
            .addOnSuccessListener {
                if (!imageUrl.isNullOrEmpty()) {
                    val storageRef = FirebaseStorage.getInstance("gs://rumbowapp.firebasestorage.app")
                        .getReference("drop_notes/$noteId.jpg")
                    storageRef.delete()
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e ->
                            Log.d(TAG, "No se pudo eliminar imagen: ${e.message}")
                            onSuccess() // nota ya borrada de DB, imagen opcional
                        }
                } else {
                    onSuccess()
                }
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error al eliminar DropNote")
            }
    }

    override fun onCleared() {
        super.onCleared()
        userListeners.forEach { (creatorId, listener) ->
            dbUsers.child(creatorId).removeEventListener(listener)
        }
        userListeners.clear()
    }
}
