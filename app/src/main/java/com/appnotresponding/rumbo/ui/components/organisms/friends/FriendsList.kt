package com.appnotresponding.rumbo.ui.components.organisms.friends

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.ui.components.molecules.friends.UserSearchResultItem

@Composable
fun FriendsList(
    friends: List<User>,
    modifier: Modifier = Modifier,
    onFriendClick: (User) -> Unit = {}
) {
    if (friends.isEmpty()) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Aún no tienes amigos en Rumbo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(friends) { friend ->
            UserSearchResultItem(
                modifier = Modifier.clickable { onFriendClick(friend) },
                user = friend,
                isAlreadyFriend = true,
                onAddClick = {}
            )
        }
    }
}
