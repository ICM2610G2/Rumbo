package com.appnotresponding.rumbo.models

data class Review(
    val id: String = "",
    val user: User = User(),
    val authorName: String = user.name,
    val authorProfilePhotoUrl: String? = user.profilePictureUrl,
    val rating: Float = 0f,
    val text: String = "",
    val photoUrl: String? = null,
    val time: Long = 0L
)

val sampleReview = Review(
    id = "1",
    user = sampleUser,
    rating = 4.5f,
    text = "Great place! Loved the ambiance and the food was delicious.",
    time = System.currentTimeMillis()
)