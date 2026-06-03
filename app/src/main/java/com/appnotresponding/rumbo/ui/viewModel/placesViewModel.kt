package com.appnotresponding.rumbo.ui.viewModel

import androidx.lifecycle.ViewModel
import com.appnotresponding.rumbo.models.Place
import com.google.android.gms.maps.model.LatLng
import com.appnotresponding.rumbo.models.PlaceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlacesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PlaceState())
    val uiState: StateFlow<PlaceState> = _uiState.asStateFlow()

    private var rawNearbyPlaces: List<Place> = emptyList()
    private var rawSearchPlaces: List<Place> = emptyList()
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
        rawNearbyPlaces = list
        mergePlacesWithReviews()
    }

    private fun mergePlacesWithReviews() {
        _uiState.update { currentState ->
            val activeList = if (currentState.searchQuery.isBlank()) rawNearbyPlaces else rawSearchPlaces
            val merged = activeList.map { place ->
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

    fun onSearchQueryChanged(query: String, context: android.content.Context) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.isBlank()) {
            rawSearchPlaces = emptyList()
            mergePlacesWithReviews()
        } else {
            com.appnotresponding.rumbo.ui.utils.searchTextPlaces(
                query = query,
                context = context,
                onPlacesReceived = { places ->
                    rawSearchPlaces = places
                    mergePlacesWithReviews()
                },
                onError = { error ->
                    android.util.Log.e("PlacesViewModel", "Error searching places: $error")
                }
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
        if (place == null) {
            _uiState.update { it.copy(previewedPlace = null) }
            return
        }
        val reviewsForPlace = (firebaseReviews[place.id] ?: emptyList()).sortedByDescending { it.time }
        val newRating = if (reviewsForPlace.isNotEmpty()) reviewsForPlace.map { it.rating }.average() else place.rating
        val mergedPlace = place.copy(reviews = reviewsForPlace, rating = if (newRating?.isNaN() == true) 0.0 else newRating)
        _uiState.update { it.copy(previewedPlace = mergedPlace) }
    }

    fun searchAndShowNearestPlace(
        latitude: Double,
        longitude: Double,
        radius: Double,
        context: android.content.Context
    ) {
        com.appnotresponding.rumbo.ui.utils.searchNearbyPlaces(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            context = context,
            onPlacesReceived = { places ->
                if (places.isNotEmpty()) {
                    val pressLatLng = LatLng(latitude, longitude)
                    val nearestPlace = places.minByOrNull { place ->
                        com.google.maps.android.SphericalUtil.computeDistanceBetween(
                            pressLatLng,
                            LatLng(place.latitude, place.longitude)
                        )
                    }
                    showPreview(nearestPlace)
                } else {
                    android.util.Log.d("PlacesViewModel", "No places found near the long press location.")
                }
            },
            onError = { error ->
                android.util.Log.e("PlacesViewModel", "Error fetching places for long press: $error")
            }
        )
    }

    fun clearForNavigation() {
        _uiState.update { it.copy(selectedPlace = null) }
    }

    fun focusOnLocation(latLng: com.google.android.gms.maps.model.LatLng) {
        _uiState.update { it.copy(focusLocation = latLng) }
    }

    fun clearFocusLocation() {
        _uiState.update { it.copy(focusLocation = null) }
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
