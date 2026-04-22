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

    fun updatePlaces(list: List<Place>) {
        _uiState.update { it.copy(availablePlaces = list) }
    }

    fun addToItinerary(place: Place) {
        val current = _uiState.value.itinerary
        if (current.none { it.id == place.id }) {
            _uiState.update { it.copy(itinerary = current + place) }
        }
    }

    fun selectForNavigation(place: Place) {
        _uiState.update { it.copy(selectedPlace = place) }
    }

    fun clearForNavigation() {
        _uiState.update { it.copy(selectedPlace = null) }
    }
}