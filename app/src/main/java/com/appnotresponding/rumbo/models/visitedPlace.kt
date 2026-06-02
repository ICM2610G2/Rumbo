package com.appnotresponding.rumbo.models

data class VisitedPlace(
    val placeId: String = "",
    val placeName: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String? = null,
    val city: String = "",
    val visitedAt: Long = 0L
)
