package com.appnotresponding.rumbo.models

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val type: String = "text",
    val placeId: String? = null,
    val mediaUrl: String? = null
)
