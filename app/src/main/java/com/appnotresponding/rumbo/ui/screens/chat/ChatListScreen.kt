package com.appnotresponding.rumbo.ui.screens.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.molecules.chat.ChatListItem
import com.appnotresponding.rumbo.ui.components.organisms.common.MainTopBar
import com.appnotresponding.rumbo.ui.components.organisms.common.Nav
import com.appnotresponding.rumbo.ui.viewModel.ChatViewModel
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ChatListScreen(
    controller: NavHostController,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    placesViewModel: PlacesViewModel
) {
    val userState by userViewModel.currentUserState.collectAsState()
    val currentUser = userState ?: sampleUser.copy(name = "Cargando...")
    val chatState by chatViewModel.uiState.collectAsState()
    val placesState by placesViewModel.uiState.collectAsState()

    LaunchedEffect(placesState.itinerary) {
        chatViewModel.listenToGroupChats(placesState.itinerary)
    }

    val myUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            MainTopBar(u = currentUser, controller = controller)
        },
        bottomBar = { Nav(controller) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { controller.navigate(AppScreens.Friends.name) },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_user_add),
                    contentDescription = "Amigos",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    text = "Chats",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Mensajes en tiempo real",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (chatState.directChats.isNotEmpty()) {
                    item {
                        Text(
                            text = "Directos",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    items(chatState.directChats) { convo ->
                        val friendUser = User(
                            id = convo.otherUserId,
                            name = convo.otherUserName,
                            profilePictureUrl = convo.otherUserPhotoUrl,
                            activity = convo.otherUserActivity,
                            isOnline = convo.isOtherUserOnline
                        )
                        ChatListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    chatViewModel.selectDirectChat(
                                        chatId = convo.chatId,
                                        chatTitle = convo.otherUserName,
                                        photoUrl = convo.otherUserPhotoUrl,
                                        isOnline = convo.isOtherUserOnline
                                    )
                                    controller.navigate(AppScreens.ChatThread.name)
                                },
                            user = friendUser,
                            lastMessage = convo.lastMessage,
                            status = convo.otherUserActivity,
                            timestamp = formatTimestamp(convo.lastMessageTimestamp),
                            hasUnread = convo.unreadCount > 0,
                            unreadCount = convo.unreadCount,
                            isOnline = convo.isOtherUserOnline
                        )
                    }
                }

                if (chatState.groupChats.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Grupos",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    items(chatState.groupChats) { group ->
                        val groupUser = User(
                            name = group.placeName,
                            profilePictureUrl = group.placePhotoUrl
                        )
                        ChatListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    chatViewModel.selectGroupChat(
                                        placeId = group.placeId,
                                        placeName = group.placeName,
                                        photoUrl = group.placePhotoUrl
                                    )
                                    controller.navigate(AppScreens.ChatThread.name)
                                },
                            user = groupUser,
                            lastMessage = group.lastMessage,
                            timestamp = formatTimestamp(group.lastMessageTimestamp),
                            hasUnread = group.unreadCount > 0,
                            unreadCount = group.unreadCount
                        )
                    }
                }

                if (chatState.directChats.isEmpty() && chatState.groupChats.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "No tienes chats aún.\nAgrega amigos con el botón +",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    if (timestamp == 0L) return ""
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60_000 -> "Ahora"
        diff < 3_600_000 -> "${diff / 60_000}m"
        diff < 86_400_000 -> "${diff / 3_600_000}h"
        else -> "${diff / 86_400_000}d"
    }
}
