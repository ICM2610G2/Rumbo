
package com.appnotresponding.rumbo.models

data class User(
    val id: String = "",
    val name: String = "",
    val lastname: String = "",
    val email: String = "",
    val phone: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double = 0.0,
    val profilePictureUrl: String? = null,
    val sharingLocation: Boolean = false,
    val activity: String? = null
)

val sampleUser = User(
    id = "1",
    name = "John",
    lastname = "Doe",
    email = "john.doe@mail.com",
    phone = "+1234567890",
    latitude = 0.0,
    longitude = 0.0,
    altitude = 0.0,
    profilePictureUrl = "https://randomuser.me/api/portraits/men/1.jpg",
    sharingLocation = false
)