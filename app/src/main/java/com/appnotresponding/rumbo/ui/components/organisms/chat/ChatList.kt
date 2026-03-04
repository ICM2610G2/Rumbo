package com.appnotresponding.rumbo.ui.components.organisms.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.components.molecules.chat.ChatListItem
import com.appnotresponding.rumbo.ui.theme.RumboTheme

@Composable
fun ChatList(
    chatItems: List<ChatPreviewData>,
    onChatClick: (ChatPreviewData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(chatItems) { chat ->
            ChatListItem(
                user = chat.user,
                lastMessage = chat.lastMessage,
                status = chat.status,
                timestamp = chat.timestamp,
                modifier = Modifier.clickable { onChatClick(chat) }
            )
        }
    }
}

data class ChatPreviewData(
    val user: User,
    val lastMessage: String,
    val status: String? = null,
    val timestamp: String,
    val hasUnread: Boolean = false
)

@Preview(showBackground = true, backgroundColor = 0xFF121212, name = "Chat List Organism")
@Composable
private fun ChatListPreview() {
    val mockChats = listOf(
        ChatPreviewData(sampleUser.copy(name = "Brandon"), "¡Ya estoy cerca! ...", "Rumbo al Museo Nacional", ""),
        ChatPreviewData(sampleUser.copy(name = "Aylean"), "¿Nos vemos allá?", "Rumbo al Museo Nacional", ""),
        ChatPreviewData(sampleUser.copy(name = "Ahbdul"), "¡Ya estoy cerca! ...", "Rumbo al Museo Nacional", ""),
        ChatPreviewData(sampleUser.copy(name = "Los Mochileros"), "@Ana, dónde estás?!", "Rumbo al Museo N...", ""),
        ChatPreviewData(sampleUser.copy(name = "Kyle"), "Fué un gusto conocerte!", null, ""),
        ChatPreviewData(sampleUser.copy(name = "Ashley"), "¡Ya estoy cerca! ...", null, ""),
        ChatPreviewData(sampleUser.copy(name = "Tatiana"), "¡Ya estoy cerca! ...", null, "")
    )
    
    RumboTheme(darkTheme = true) {
        ChatList(chatItems = mockChats)
    }
}