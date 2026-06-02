package com.appnotresponding.rumbo.models

data class ChatConversation(
    val chatId: String = "",
    val otherUserId: String = "",
    val otherUserName: String = "",
    val otherUserPhotoUrl: String? = null,
    val otherUserActivity: String? = null,
    val isOtherUserOnline: Boolean = false,
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = 0,
    val unreadCount: Int = 0
)

data class GroupChat(
    val placeId: String = "",
    val placeName: String = "",
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = 0,
    val unreadCount: Int = 0,
    val mutedBy: Map<String, Boolean> = emptyMap()
)
