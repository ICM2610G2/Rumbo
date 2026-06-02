package com.appnotresponding.rumbo.models

import com.google.android.gms.maps.model.LatLng

data class PlaceState(
    val availablePlaces: List<Place> = emptyList(),
    val itinerary: List<Place> = emptyList(),
    val selectedPlace: Place? = null,
    val focusLocation: LatLng? = null
)