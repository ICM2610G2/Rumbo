package com.appnotresponding.rumbo.ui.screens.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import java.io.File
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.components.molecules.chat.ChatBubble
import com.appnotresponding.rumbo.ui.components.molecules.chat.ChatBubbleType
import com.appnotresponding.rumbo.ui.templates.ChatThreadTemplate
import com.appnotresponding.rumbo.ui.viewModel.ChatThreadViewModel
import com.appnotresponding.rumbo.ui.viewModel.ChatViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserLocationViewModel
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.utils.rememberMediaHardwareManager

@Composable
fun ChatThreadScreen(
    controller: NavHostController,
    chatViewModel: ChatViewModel,
    chatThreadViewModel: ChatThreadViewModel,
    userViewModel: UserViewModel,
    locationViewModel: UserLocationViewModel,
    placesViewModel: PlacesViewModel
) {
    val chatState by chatViewModel.uiState.collectAsState()
    val threadState by chatThreadViewModel.uiState.collectAsState()
    val userState by userViewModel.currentUserState.collectAsState()
    val currentUser = userState ?: sampleUser
    val locationState by locationViewModel.uiState.collectAsState()

    var messageInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val chatId = chatState.selectedChatId
    val isGroup = chatState.isGroupChat
    var unreadDividerTimestamp by remember(chatId) { mutableStateOf<Long?>(null) }

    val mediaManager = rememberMediaHardwareManager()
    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var audioFile by remember { mutableStateOf<File?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    var imagePreviewUrl by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            chatThreadViewModel.sendMediaMessage(chatId, currentUser.name, uri, isGroup, "image")
        }
    }

    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = pendingCameraUri
        if (success && uri != null) {
            chatThreadViewModel.sendMediaMessage(chatId, currentUser.name, uri, isGroup, "image")
        }
        pendingCameraUri = null
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Handle permission result if needed
    }

    LaunchedEffect(chatId) {
        if (chatId.isNotBlank()) {
            if (isGroup) {
                chatThreadViewModel.listenToGroupMessages(chatId)
            } else {
                chatThreadViewModel.listenToMessages(chatId)
            }
        }
    }

    LaunchedEffect(threadState.messages.size) {
        if (threadState.messages.isNotEmpty()) {
            if (unreadDividerTimestamp == null) {
                unreadDividerTimestamp = threadState.lastReadTimestamp
            }
            listState.animateScrollToItem(threadState.messages.size - 1)
            chatThreadViewModel.markChatAsRead(chatId, isGroup)
        }
    }

    val avatarUser = sampleUser.copy(
        name = chatState.selectedChatTitle, profilePictureUrl = chatState.selectedChatPhoto
    )

    val isMuted =
        chatState.groupChats.find { it.placeId == chatId }?.mutedBy?.get(currentUser.id) == true

    val otherUid = chatId.split("_").firstOrNull { it != currentUser.id }
    val otherUser = threadState.messageAuthors[otherUid]

    ChatThreadTemplate(
        chatTitle = chatState.selectedChatTitle,
        chatSubtitle = if (isGroup) "Chat grupal" else (otherUser?.activity ?: ""),
        chatAvatarUser = avatarUser,
        isGroup = isGroup,
        isMuted = isMuted,
        isOnline = !isGroup && threadState.otherUserIsOnline,
        onMuteClick = {
            if (isMuted) {
                chatViewModel.unmuteGroup(chatId)
            } else {
                chatViewModel.muteGroup(chatId)
            }
        },
        onLeaveClick = {
            chatViewModel.leaveGroup(chatId)
            controller.navigateUp()
        },
        onBackClick = {
            controller.navigateUp()
        },
        messageInputValue = messageInput,
        onMessageInputValueChange = { messageInput = it },
        onSendClick = {
            val text = messageInput.trim()
            if (!isRecording && text.isNotBlank()) {
                if (isGroup) {
                    chatThreadViewModel.sendGroupMessage(chatId, currentUser.name, text)
                } else {
                    chatThreadViewModel.sendMessage(chatId, text)
                }
                messageInput = ""
            }
        },
        onImageClick = {
            imagePickerLauncher.launch("image/*")
        },
        onCameraClick = {
            mediaManager.launchCamera()
        },
        onLocationClick = {
            val lat = locationState.latitude
            val lng = locationState.longitude
            val finalLat = if (lat != 0.0) lat else 4.627293
            val finalLng = if (lng != 0.0) lng else -74.063228
            chatThreadViewModel.sendLocationMessage(
                chatId,
                currentUser.name,
                finalLat,
                finalLng,
                isGroup
            )
        },
        onMicClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (isRecording) {
                    mediaRecorder?.stop()
                    mediaRecorder?.release()
                    mediaRecorder = null
                    isRecording = false
                    audioFile?.let { file ->
                        chatThreadViewModel.sendMediaMessage(
                            chatId,
                            currentUser.name,
                            Uri.fromFile(file),
                            isGroup,
                            "audio"
                        )
                    }
                } else {
                    messageInput = ""
                    audioFile = File.createTempFile("audio", ".mp4", context.cacheDir)
                    val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        MediaRecorder(context)
                    } else {
                        @Suppress("DEPRECATION") MediaRecorder()
                    }
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    recorder.setOutputFile(audioFile!!.absolutePath)
                    try {
                        recorder.prepare()
                        recorder.start()
                        mediaRecorder = recorder
                        isRecording = true
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        },
        isRecordingAudio = isRecording
    ) {
        if (threadState.messages.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Sin mensajes aún. ¡Di hola!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                state = listState, contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                itemsIndexed(threadState.messages) { index, msg ->
                    val isMine = msg.senderId == currentUser.id
                    val previousMessage = threadState.messages.getOrNull(index - 1)
                    val nextMessage = threadState.messages.getOrNull(index + 1)
                    val isSameAsPrevious = previousMessage?.senderId == msg.senderId
                    val isLastInSequence = nextMessage?.senderId != msg.senderId
                    val dividerTimestamp = unreadDividerTimestamp ?: threadState.lastReadTimestamp
                    val shouldShowNewMessages =
                        !isMine && msg.timestamp > dividerTimestamp && threadState.messages.take(
                            index
                        ).none { previous ->
                            previous.senderId != currentUser.id && previous.timestamp > dividerTimestamp
                        }
                    if (shouldShowNewMessages) {
                        com.appnotresponding.rumbo.ui.components.molecules.chat.ChatSeparator("Nuevos mensajes")
                    }
                    val author = threadState.messageAuthors[msg.senderId]
                    val activity = author?.activity
                    val bubbleType =
                        if (msg.type == "location") ChatBubbleType.Location else ChatBubbleType.Regular
                    val onLocClick: (() -> Unit)? = if (msg.type == "location") {
                        {
                            val parts = msg.text.removePrefix("Ubicación: ").split(",")
                            if (parts.size == 2) {
                                val lat = parts[0].trim().toDoubleOrNull()
                                val lng = parts[1].trim().toDoubleOrNull()
                                if (lat != null && lng != null) {
                                    placesViewModel.focusOnLocation(
                                        com.google.android.gms.maps.model.LatLng(
                                            lat,
                                            lng
                                        )
                                    )
                                    controller.navigate(AppScreens.Map.name)
                                }
                            }
                        }
                    } else null

                    val displayName = author?.name?.takeIf { it.isNotBlank() } ?: msg.senderName
                    val seenText = if (isMine) {
                        if (isGroup) {
                            val seenCount = msg.seenBy.keys.count { it != currentUser.id }
                            if (seenCount > 0) "Visto por $seenCount" else "Enviado"
                        } else {
                            val otherHasSeen = otherUid != null && msg.seenBy[otherUid] == true
                            if (otherHasSeen) "Visto" else "Enviado"
                        }
                    } else {
                        null
                    }
                    ChatBubble(
                        modifier = Modifier.padding(top = if (isSameAsPrevious) 2.dp else 8.dp),
                        message = msg.text,
                        mediaUrl = msg.mediaUrl,
                        mediaType = msg.type,
                        isUserMessage = isMine,
                        senderName = if (!isMine && isGroup && displayName.isNotBlank()) displayName else null,
                        senderActivity = if (!isMine && isGroup) activity else null,
                        type = bubbleType,
                        timestamp = msg.timestamp,
                        seenText = seenText,
                        isLastInSequence = isLastInSequence,
                        onMediaClick = { imagePreviewUrl = it },
                        onLocationClick = onLocClick
                    )
                }
            }
        }

        imagePreviewUrl?.let { previewUrl ->
            Dialog(
                onDismissRequest = { imagePreviewUrl = null },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.88f))
                        .padding(16.dp), contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = previewUrl,
                        contentDescription = "Preview de imagen",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

private fun createCameraImageUri(context: android.content.Context): Uri {
    val imageFile = File.createTempFile("chat_camera_", ".jpg", context.filesDir)
    return FileProvider.getUriForFile(
        context, "${context.packageName}.fileprovider", imageFile
    )
}


