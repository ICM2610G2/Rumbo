package com.appnotresponding.rumbo.models

data class Review(
    val id: String,
    val authorName: String,
    val authorProfilePhotoUrl: String?,
    val rating: Float,
    val text: String,
    val time: Long
)