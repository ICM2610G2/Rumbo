package com.appnotresponding.rumbo.ui.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
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
    isGroup: Boolean = false,
    isMuted: Boolean = false,
    isOnline: Boolean = false,
    onMuteClick: (() -> Unit)? = null,
    onLeaveClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    messageInputValue: String = "",
    onMessageInputValueChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
    onImageClick: () -> Unit = {},
    onCameraClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onMicClick: () -> Unit = {},
    isRecordingAudio: Boolean = false,
    content: @Composable () -> Unit
) {
    Scaffold(contentWindowInsets = WindowInsets(0), topBar = {
        ChatTopBar(
            u = chatAvatarUser, 
            activity = chatSubtitle,
            isGroup = isGroup,
            isMuted = isMuted,
            isOnline = isOnline,
            onMuteClick = onMuteClick,
            onLeaveClick = onLeaveClick,
            onBackClick = onBackClick
        )
    }, bottomBar = {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 12.dp)
        ) {
            MessageComposer(
                value = messageInputValue,
                onValueChange = onMessageInputValueChange,
                onSendClick = onSendClick,
                onImageClick = onImageClick,
                onCameraClick = onCameraClick,
                onLocationClick = onLocationClick,
                onMicClick = onMicClick,
                isRecordingAudio = isRecordingAudio
            )
        }
    }) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            content()
        }
    }
}

