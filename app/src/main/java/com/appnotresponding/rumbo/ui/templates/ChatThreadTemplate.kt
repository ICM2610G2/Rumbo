package com.appnotresponding.rumbo.ui.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.ui.components.molecules.chat.MessageComposer
import com.appnotresponding.rumbo.ui.components.organisms.common.ChatTopBar

@Composable
fun ChatThreadTemplate(
    modifier: Modifier = Modifier,
    chatTitle: String,
    chatSubtitle: String,
    chatAvatarUser: User,
    messageInputValue: String = "",
    onMessageInputValueChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        ChatTopBar(u = chatAvatarUser, activity = chatSubtitle)
    }, bottomBar = {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            MessageComposer(
                value = messageInputValue,
                onValueChange = onMessageInputValueChange,
                onSendClick = onSendClick
            )
        }
    }) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .padding(horizontal = 8.dp)
        ) {
            content()
        }
    }
}

