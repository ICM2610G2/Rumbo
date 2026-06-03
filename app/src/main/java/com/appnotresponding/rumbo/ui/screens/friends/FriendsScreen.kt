package com.appnotresponding.rumbo.ui.screens.friends

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.appnotresponding.rumbo.R
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonSize
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
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
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var contactDiscoveryActive by remember { mutableStateOf(false) }

    val contactsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val contacts = readContactKeys(context)
            contactDiscoveryActive = true
            searchQuery = ""
            friendsViewModel.searchUsersByContacts(contacts.emails, contacts.phones)
        }
    }

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
                    contactDiscoveryActive = false
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

            RumboButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Buscar amigos en contactos",
                onClick = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        val contacts = readContactKeys(context)
                        contactDiscoveryActive = true
                        searchQuery = ""
                        friendsViewModel.searchUsersByContacts(contacts.emails, contacts.phones)
                    } else {
                        contactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                },
                style = RumboButtonStyle.Secondary,
                size = RumboButtonSize.Medium,
                icon = painterResource(R.drawable.ic_user_add)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (searchQuery.isNotBlank() || contactDiscoveryActive) {
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

private data class ContactKeys(
    val emails: Set<String>,
    val phones: Set<String>
)

private fun readContactKeys(context: Context): ContactKeys {
    val emails = mutableSetOf<String>()
    val phones = mutableSetOf<String>()

    context.contentResolver.query(
        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
        arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
        null,
        null,
        null
    )?.use { cursor ->
        val emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
        if (emailIndex >= 0) {
            while (cursor.moveToNext()) {
                cursor.getString(emailIndex)?.takeIf { it.isNotBlank() }?.let { emails.add(it) }
            }
        }
    }

    context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
        null,
        null,
        null
    )?.use { cursor ->
        val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        if (phoneIndex >= 0) {
            while (cursor.moveToNext()) {
                cursor.getString(phoneIndex)?.takeIf { it.isNotBlank() }?.let { phones.add(it) }
            }
        }
    }

    return ContactKeys(emails = emails, phones = phones)
}
