package com.appnotresponding.rumbo.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val profilePictureUrl: String? = null
)

val sampleUser = User(
    id = "1",
    name = "John Doe",
    email = "john.doe@mail.com",
    phone = "+1234567890",
    profilePictureUrl = "https://randomuser.me/api/portraits/men/1.jpg"
)