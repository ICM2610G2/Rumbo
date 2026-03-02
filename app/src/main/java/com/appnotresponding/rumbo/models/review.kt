package com.appnotresponding.rumbo.models

data class Review(
    val id: String,
    val user: User,
    val authorName: String = user.name,
    val authorProfilePhotoUrl: String? = user.profilePictureUrl,
    val rating: Float,
    val text: String,
    val time: Long
)

val sampleReview = Review(
    id = "1",
    user = sampleUser,
    rating = 4.5f,
    text = "Great place! Loved the ambiance and the food was delicious.",
    time = System.currentTimeMillis()
)