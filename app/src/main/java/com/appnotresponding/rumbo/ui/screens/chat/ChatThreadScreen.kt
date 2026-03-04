package com.appnotresponding.rumbo.ui.screens.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.molecules.chat.ChatBubbleType
import com.appnotresponding.rumbo.ui.components.organisms.chat.ChatMessageData
import com.appnotresponding.rumbo.ui.components.organisms.chat.ChatThread
import com.appnotresponding.rumbo.ui.templates.ChatThreadTemplate
import com.appnotresponding.rumbo.ui.theme.RumboTheme

@Composable
fun ChatThreadScreen(
    controller: NavHostController
) {
    var messageInput by remember { mutableStateOf("") }
    val brandonUser = sampleUser.copy(name = "Brandon")

    val museoNacional = samplePlace.copy(
        name = "Museo Nacional",
        openHours = "9:00AM - 11:00AM",
        price = "$ 40.000 COP"
    )

    val messages = listOf(
        ChatMessageData("Hola! Vamos al Museo Nacional?", isUserMessage = true),
        ChatMessageData("Hola! De una!", isUserMessage = false),
        ChatMessageData("", isUserMessage = false, type = ChatBubbleType.LiveActivity, place = museoNacional),
        ChatMessageData("Iniciaste una ruta compartida", isUserMessage = false, isSeparator = true),
        ChatMessageData("Ya estoy en camino!", isUserMessage = true),
        ChatMessageData("¡Ya estoy cerca!\nTe parece si nos vemos en la entrada?", isUserMessage = false)
    )
    ChatThreadTemplate(
        chatTitle = brandonUser.name,
        chatSubtitle = "",
        chatAvatarUser = brandonUser,
        onBackClick = { controller.navigate(AppScreens.Chat.name) },
        messageInputValue = messageInput,
        onMessageInputValueChange = { messageInput = it },
        onSendClick = {
            messageInput = ""
        }
    ) {
        ChatThread(messages = messages)
    }
}

@Preview(showBackground = true, name = "4A. Hilo de Chat (1 a 1) - Demo", backgroundColor = 0xFF121212, heightDp = 800)
@Composable
fun ChatThreadOneOnOnePreview() {
    val brandonUser = sampleUser.copy(name = "Brandon")

    val museoNacional = samplePlace.copy(
        name = "Museo Nacional",
        openHours = "9:00AM - 11:00AM",
        price = "$ 40.000 COP"
    )

    val mockMessages = listOf(
        ChatMessageData("Hola! Vamos al Museo Nacional?", isUserMessage = true),
        ChatMessageData("Hola! De una!", isUserMessage = false),
        ChatMessageData("", isUserMessage = false, type = ChatBubbleType.LiveActivity, place = museoNacional),
        ChatMessageData("Iniciaste una ruta compartida", isUserMessage = false, isSeparator = true),
        ChatMessageData("Ya estoy en camino!", isUserMessage = true),
        ChatMessageData("¡Ya estoy cerca!\nTe parece si nos vemos en la entrada?", isUserMessage = false)
    )

    RumboTheme(darkTheme = true) {
        ChatThreadScreen(controller = rememberNavController())
    }
}
@Preview(showBackground = true, name = "4.1. Hilo de Chat Grupal demostrcion", backgroundColor = 0xFF121212, heightDp = 800)
@Composable
fun ChatThreadGroupPreview() {
    val groupAvatar = sampleUser.copy(name = "Grupo")

    val mockGroupMessages = listOf(
        ChatMessageData("Hola! Cómo van??", isUserMessage = true),
        ChatMessageData("Hola! Yo estoy saliendo del hotel", isUserMessage = false, senderName = "Brandon"),
        ChatMessageData("Yo ya llegué, acá los espero", isUserMessage = false, senderName = "Ahbdul"),
        ChatMessageData("@Ashley, dónde vienes?", isUserMessage = true),
        ChatMessageData("Creo que estoy perdida 😭", isUserMessage = false, senderName = "Ashley"),
        ChatMessageData("Mentira, ya estoy con los demás", isUserMessage = true),
        ChatMessageData("@Ana, dónde estás?!", isUserMessage = false, senderName = "Ana"),
        ChatMessageData("", isUserMessage = true, type = ChatBubbleType.Location)
    )

    RumboTheme(darkTheme = true) {
        ChatThreadScreen(
            controller = rememberNavController()
        )
    }
}
