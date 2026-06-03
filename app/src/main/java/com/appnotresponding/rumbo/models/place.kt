package com.appnotresponding.rumbo.models

data class Place(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val description: String? = null,
    val openHours: List<String>? = null,
    val price: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val rating: Double? = null,
    val reviews: List<Review> = emptyList(),
    val imageUrl: String? = null
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