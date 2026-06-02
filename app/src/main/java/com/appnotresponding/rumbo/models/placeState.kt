package com.appnotresponding.rumbo.models

data class PlaceState(
    val availablePlaces: List<Place> = emptyList(),
    val itinerary: List<Place> = emptyList(),
    val selectedPlace: Place? = null
)