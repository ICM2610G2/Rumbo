package com.appnotresponding.rumbo.ui.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.ui.components.organisms.common.MainTopBar
import com.appnotresponding.rumbo.ui.components.organisms.common.Nav

@Composable
fun FriendsTemplate(
    currentUser: User,
    onProfileClick: () -> Unit = {},
    controller: NavHostController,
    content: @Composable () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = { MainTopBar(u = currentUser, onProfileClick = onProfileClick) },
        bottomBar = { Nav(controller) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    text = "Amigos",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Busca y conecta con otros viajeros",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                content()
            }
        }
    }
}
