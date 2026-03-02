package com.appnotresponding.rumbo.models

data class DropNote(
    val id: String,
    val user: User,
    val authorId: String = user.id,
    val authorName: String = user.name,
    val authorAvatarUrl: String? = user.profilePictureUrl,
    val public: Boolean,
    val content: String,
    val imageUrl: String?,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double
)

val sampleDropNote = DropNote(
    id = "1",
    user = sampleUser,
    public = true,
    content = "Hello! This is a sample drop note.",
    imageUrl = null,
    timestamp = System.currentTimeMillis(),
    latitude = 37.7749,
    longitude = -122.4194
)