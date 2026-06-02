package com.appnotresponding.rumbo.ui.screens.friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.atoms.RumboTextField
import com.appnotresponding.rumbo.ui.components.molecules.friends.UserSearchResultItem
import com.appnotresponding.rumbo.ui.components.molecules.friends.FriendRequestItem
import com.appnotresponding.rumbo.ui.components.organisms.friends.FriendsList
import com.appnotresponding.rumbo.ui.templates.FriendsTemplate
import com.appnotresponding.rumbo.ui.viewModel.ChatViewModel
import com.appnotresponding.rumbo.ui.viewModel.FriendsViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FriendsScreen(
    controller: NavHostController,
    userViewModel: UserViewModel,
    friendsViewModel: FriendsViewModel,
    chatViewModel: ChatViewModel
) {
    val userState by userViewModel.currentUserState.collectAsState()
    val currentUser = userState ?: sampleUser.copy(name = "Cargando...")
    val friendsState by friendsViewModel.uiState.collectAsState()
    val myUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var searchQuery by remember { mutableStateOf("") }

    FriendsTemplate(
        currentUser = currentUser,
        controller = controller
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RumboTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    if (it.isBlank()) {
                        friendsViewModel.clearSearch()
                    } else {
                        friendsViewModel.searchUserByName(it)
                    }
                },
                placeholder = "Buscar por nombre...",
                label = "Buscar usuarios"
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (searchQuery.isNotBlank()) {
                if (friendsState.isSearching) {
                    Text(
                        text = "Buscando...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else if (friendsState.searchError != null && friendsState.searchResults.isEmpty()) {
                    Text(
                        text = friendsState.searchError!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(friendsState.searchResults) { user ->
                            UserSearchResultItem(
                                user = user,
                                isAlreadyFriend = friendsState.friendIds.contains(user.id),
                                isPending = friendsState.sentRequestIds.contains(user.id),
                                onAddClick = { friendsViewModel.addFriend(user.id) }
                            )
                        }
                    }
                }
            } else {
                if (friendsState.pendingRequests.isNotEmpty()) {
                    Text(
                        text = "Solicitudes de amistad",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    ) {
                        items(friendsState.pendingRequests) { requestUser ->
                            FriendRequestItem(
                                user = requestUser,
                                onAcceptClick = { friendsViewModel.acceptFriendRequest(requestUser.id) },
                                onDeclineClick = { friendsViewModel.declineFriendRequest(requestUser.id) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = "Mis amigos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (friendsState.friends.isEmpty()) {
                    Text(
                        text = "Aún no tienes amigos agregados.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    FriendsList(
                        friends = friendsState.friends,
                        onFriendClick = { friend ->
                            val chatId = chatViewModel.getOrCreateDirectChatId(myUid, friend.id)
                            chatViewModel.selectDirectChat(
                                chatId = chatId,
                                chatTitle = friend.name,
                                photoUrl = friend.profilePictureUrl
                            )
                            controller.navigate(AppScreens.ChatThread.name)
                        }
                    )
                }
            }
        }
    }
}
