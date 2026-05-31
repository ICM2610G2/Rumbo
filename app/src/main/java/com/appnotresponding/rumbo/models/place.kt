package com.appnotresponding.rumbo.models

data class Place(
    val id: String,
    val name: String,
    val address: String,
    val description: String?,
    val openHours: List<String>?,
    val price: String?,
    val latitude: Double,
    val longitude: Double,
    val rating: Double?,
    val reviews: List<Review>,
    val imageUrl: String?
)

val samplePlace = Place(
    id = "1",
    name = "Museo del Oro",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed consectetur bibendum diam a interdum. Donec tempus fringilla auctor. Vivamus imperdiet, orci eu consectetur placerat.",
    openHours = listOf("9:00 AM - 5:00 PM", ""),
    price = "Gratis",
    latitude = 4.6018,
    longitude = -74.0719,
    rating = 4.5,
    reviews = emptyList(),
    imageUrl = "",
    address = ""
)