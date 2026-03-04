package com.appnotresponding.rumbo.ui.screens.chat

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.components.organisms.chat.ChatList
import com.appnotresponding.rumbo.ui.components.organisms.chat.ChatPreviewData
import com.appnotresponding.rumbo.ui.templates.ChatTemplate
import com.appnotresponding.rumbo.ui.theme.RumboTheme


@Composable
fun ChatListScreen(
    onChatClick: (ChatPreviewData) -> Unit = {}
) {
    val currentUser = sampleUser.copy(name = "Ana")

    val mockChats = listOf(
        ChatPreviewData(
            sampleUser.copy(name = "Brandon"),
            "¡Ya estoy cerca! ...",
            "Rumbo al Museo Nacional",
            "",
            true
        ),
        ChatPreviewData(
            sampleUser.copy(name = "Aylean"),
            "¿Nos vemos allá?",
            "Rumbo al Museo Nacional",
            "",
            false
        ),
        ChatPreviewData(
            sampleUser.copy(name = "Ahbdul"),
            "¡Ya estoy cerca! ...",
            "Rumbo al Museo Nacional",
            "",
            false
        ),
        ChatPreviewData(
            sampleUser.copy(name = "Los Mochileros"),
            "@Ana, dónde estás?!",
            "Rumbo al Museo N...",
            "",
            true
        ),
        ChatPreviewData(sampleUser.copy(name = "Kyle"), "Fué un gusto conocerte!", null, "", false),
        ChatPreviewData(sampleUser.copy(name = "Ashley"), "¡Ya estoy cerca! ...", null, "", false),
        ChatPreviewData(sampleUser.copy(name = "Tatiana"), "¡Ya estoy cerca! ...", null, "", false)
    )

    ChatTemplate(
        currentUser = currentUser, title = "Chats", subtitle = "Ubicación actual: Bogotá"
    ) {
        ChatList(
            chatItems = mockChats, onChatClick = onChatClick
        )
    }
}


@Preview(
    showBackground = true,
    name = "3. Pantalla Lista de Chats demostracion",
    backgroundColor = 0xFF121212
)
@Composable
private fun ChatListScreenPreview() {
    RumboTheme(darkTheme = true) {
        ChatListScreen()
    }
}
