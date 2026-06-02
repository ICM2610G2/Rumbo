package com.appnotresponding.rumbo.ui.viewModel

import androidx.lifecycle.ViewModel
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.PlaceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlacesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PlaceState())
    val uiState: StateFlow<PlaceState> = _uiState.asStateFlow()

    private var rawPlaces: List<Place> = emptyList()
    private var firebaseReviews: Map<String, List<com.appnotresponding.rumbo.models.Review>> = emptyMap()

    init {
        observeReviews()
    }

    private fun observeReviews() {
        com.google.firebase.database.FirebaseDatabase.getInstance().getReference("places_reviews")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val map = mutableMapOf<String, List<com.appnotresponding.rumbo.models.Review>>()
                    for (placeSnapshot in snapshot.children) {
                        val placeId = placeSnapshot.key ?: continue
                        val reviewsList = mutableListOf<com.appnotresponding.rumbo.models.Review>()
                        for (reviewSnapshot in placeSnapshot.children) {
                            val r = reviewSnapshot.getValue(com.appnotresponding.rumbo.models.Review::class.java)
                            if (r != null) reviewsList.add(r)
                        }
                        map[placeId] = reviewsList
                    }
                    firebaseReviews = map
                    mergePlacesWithReviews()
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    android.util.Log.e("PlacesViewModel", "Error fetching reviews: ${error.message}")
                }
            })
    }

    fun updatePlaces(list: List<Place>) {
        rawPlaces = list
        mergePlacesWithReviews()
    }

    private fun mergePlacesWithReviews() {
        _uiState.update { currentState ->
            val merged = rawPlaces.map { place ->
                val reviewsForPlace = (firebaseReviews[place.id] ?: emptyList()).sortedByDescending { it.time }
                val newRating = if (reviewsForPlace.isNotEmpty()) reviewsForPlace.map { it.rating }.average() else place.rating
                place.copy(reviews = reviewsForPlace, rating = if (newRating?.isNaN() == true) 0.0 else newRating)
            }
            val mergedSelected = merged.find { it.id == currentState.selectedPlace?.id }
            val mergedPreviewed = merged.find { it.id == currentState.previewedPlace?.id }

            currentState.copy(
                availablePlaces = merged,
                selectedPlace = mergedSelected ?: currentState.selectedPlace,
                previewedPlace = mergedPreviewed ?: currentState.previewedPlace
            )
        }
    }

    fun addToItinerary(place: Place) {
        val current = _uiState.value.itinerary
        if (current.none { it.id == place.id }) {
            _uiState.update { it.copy(itinerary = current + place) }
        }
    }

    fun removeFromItinerary(place: Place) {
        val current = _uiState.value.itinerary
        if (current.any { it.id == place.id }) {
            _uiState.update { it.copy(itinerary = current.filterNot { it.id == place.id }) }
        }
    }

    fun selectForNavigation(place: Place?) {
        _uiState.update { it.copy(selectedPlace = place) }
    }

    fun showPreview(place: Place?) {
        _uiState.update { it.copy(previewedPlace = place) }
    }

    fun clearForNavigation() {
        _uiState.update { it.copy(selectedPlace = null) }
    }

    fun uploadAndSaveReview(
        placeId: String,
        review: com.appnotresponding.rumbo.models.Review,
        imageUri: android.net.Uri?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val reviewId = java.util.UUID.randomUUID().toString()
        val dbRef = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("places_reviews").child(placeId).child(reviewId)

        if (imageUri != null) {
            val storageRef = com.google.firebase.storage.FirebaseStorage.getInstance("gs://rumbowapp.firebasestorage.app")
                .getReference("reviews/$reviewId.jpg")
            storageRef.putFile(imageUri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val finalReview = review.copy(id = reviewId, photoUrl = downloadUrl.toString(), time = System.currentTimeMillis())
                        dbRef.setValue(finalReview).addOnCompleteListener {
                            onSuccess()
                        }
                    }.addOnFailureListener { e ->
                        onFailure(e.message ?: "Error al obtener URL")
                    }
                }
                .addOnFailureListener { e ->
                    onFailure(e.message ?: "Error al subir imagen")
                }
        } else {
            val finalReview = review.copy(id = reviewId, photoUrl = null, time = System.currentTimeMillis())
            dbRef.setValue(finalReview).addOnCompleteListener {
                onSuccess()
            }
        }
    }
}
