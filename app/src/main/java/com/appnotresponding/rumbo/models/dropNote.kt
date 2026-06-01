package com.appnotresponding.rumbo.models

data class DropNote(
    val id: String = "",
    val creatorId: String = "",
    val creatorName: String = "",
    val creatorAvatarUrl: String? = null,
    val content: String = "",
    val imageUrl: String? = null,
    val timestamp: Long = 0L,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val public: Boolean = true
) {
    val user: User
        get() = User(
            id = creatorId,
            name = creatorName,
            profilePictureUrl = creatorAvatarUrl
        )
    val authorAvatarUrl: String?
        get() = creatorAvatarUrl
}

val sampleDropNote = DropNote(
    id = "1",
    creatorId = sampleUser.id,
    creatorName = sampleUser.name,
    creatorAvatarUrl = sampleUser.profilePictureUrl,
    public = true,
    content = "Hello! This is a sample drop note.",
    imageUrl = null,
    timestamp = System.currentTimeMillis(),
    latitude = 37.7749,
    longitude = -122.4194
)