package com.appnotresponding.rumbo.ui.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.appnotresponding.rumbo.models.DropNote
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MyMarker(var position: LatLng,
                    var title: String = "Marker", var snippet: String ="Desc")
data class MapState(
    val userMarker: MyMarker = MyMarker(LatLng(0.0, 0.0)),
    val additionalMarker: MyMarker = MyMarker(LatLng(0.0, 0.0)),
    val additionalMarkerVisible: Boolean = false,
    val routePoints: List<LatLng> = emptyList(),
    val userRoutePoints: List<LatLng> = emptyList(),
    val userRouteVisible: Boolean = false,
    val place: String = "",
    val centerInUserFirstTime: Boolean = true,
    val lastSafeLatLng: LatLng = LatLng(0.0, 0.0),
    val dropNotes: List<DropNote> = emptyList(),
    val isUploadingNote: Boolean = false,
    val noteUploadError: String = ""
)

class MapViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(MapState())
    val uiState: StateFlow<MapState> = _uiState.asStateFlow()

    private val dbDropNotes = FirebaseDatabase.getInstance().getReference("dropNotes")

    init {
        fetchDropNotes()
    }

    private fun fetchDropNotes() {
        dbDropNotes.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notes = mutableListOf<DropNote>()
                for (child in snapshot.children) {
                    val note = child.getValue(DropNote::class.java)
                    if (note != null) {
                        notes.add(note)
                    }
                }
                _uiState.update { it.copy(dropNotes = notes) }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo de error
            }
        })
    }

    fun uploadAndSaveDropNote(
        content: String,
        imageUri: Uri?,
        latitude: Double,
        longitude: Double,
        creatorId: String,
        creatorName: String,
        creatorAvatarUrl: String?,
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
                            creatorName = creatorName,
                            creatorAvatarUrl = creatorAvatarUrl,
                            onSuccess = onSuccess
                        )
                    }.addOnFailureListener { e ->
                        _uiState.update { it.copy(isUploadingNote = false, noteUploadError = e.message ?: "Error al obtener URL de descarga") }
                    }
                }
                .addOnFailureListener { e ->
                    _uiState.update { it.copy(isUploadingNote = false, noteUploadError = e.message ?: "Error al subir imagen") }
                }
        } else {
            saveDropNoteMetadata(
                noteId = noteId,
                content = content,
                imageUrl = null,
                latitude = latitude,
                longitude = longitude,
                creatorId = creatorId,
                creatorName = creatorName,
                creatorAvatarUrl = creatorAvatarUrl,
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
        creatorName: String,
        creatorAvatarUrl: String?,
        onSuccess: () -> Unit
    ) {
        val dropNote = DropNote(
            id = noteId,
            creatorId = creatorId,
            creatorName = creatorName,
            creatorAvatarUrl = creatorAvatarUrl,
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
                _uiState.update { it.copy(isUploadingNote = false, noteUploadError = e.message ?: "Error al guardar en base de datos") }
            }
    }

    fun updatePlace(place: String) {
        _uiState.update { it.copy(place = place) }
    }

    fun updateCenterInUserFirstTime() {
        _uiState.update { it.copy(centerInUserFirstTime = false) }
    }

    fun updateUserMarker(lat: Double, lng: Double) {
        val newLatLng = LatLng(lat, lng)
        _uiState.update { it.copy(userMarker = MyMarker(newLatLng)) }
    }

    fun updateAdditionalMarker(position: LatLng, title: String) {
        _uiState.update { it.copy(additionalMarker = MyMarker(position), additionalMarkerVisible = true) }
    }

    fun cancelAdditionalMarkerVisibility() {
        _uiState.update { it.copy(additionalMarkerVisible = false) }
    }

    fun updateRoutePoints(points: List<LatLng>) {
        _uiState.update { it.copy(routePoints = points) }
    }

    fun updateUserRouteVisible() {
        _uiState.update { it.copy(userRouteVisible = !it.userRouteVisible) }
    }

    fun updateLastSafeLatLng(lat: Double, lng: Double) {
        _uiState.update { it.copy(lastSafeLatLng = LatLng(lat, lng)) }
    }

    fun updateUserRoutePoints(points: List<LatLng>) {
        _uiState.update { it.copy(userRoutePoints = points) }
    }

}