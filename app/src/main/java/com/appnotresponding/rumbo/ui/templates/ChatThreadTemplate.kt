package com.appnotresponding.rumbo.ui.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.ui.components.molecules.chat.MessageComposer
import com.appnotresponding.rumbo.ui.components.organisms.common.ChatTopBar

@Composable
fun ChatThreadTemplate(
    modifier: Modifier = Modifier,
    chatTitle: String,
    chatSubtitle: String,
    chatAvatarUser: User,
    onBackClick: () -> Unit = {},
    messageInputValue: String = "",
    onMessageInputValueChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBackClick, modifier = Modifier
                    .padding(start = 8.dp)
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            ChatTopBar(u = chatAvatarUser, activity = chatSubtitle)
        }
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
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {
            content()
        }
    }
}

