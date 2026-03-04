package com.appnotresponding.rumbo.ui.components.organisms.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.ui.components.molecules.chat.ChatBubble
import com.appnotresponding.rumbo.ui.components.molecules.chat.ChatBubbleType

data class ChatMessageData(
    val message: String,
    val isUserMessage: Boolean,
    val senderName: String? = null,
    val type: ChatBubbleType = ChatBubbleType.Regular,
    val place: Place? = null,
    val isSeparator: Boolean = false
)

@Composable
fun ChatThread(
    messages: List<ChatMessageData>, modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        messages.forEach { msg ->
            if (msg.isSeparator) {
                com.appnotresponding.rumbo.ui.components.molecules.chat.ChatSeparator(text = msg.message)
            } else {
                ChatBubble(
                    message = msg.message,
                    isUserMessage = msg.isUserMessage,
                    senderName = msg.senderName,
                    type = msg.type,
                    place = msg.place
                )
            }
        }
    }
}