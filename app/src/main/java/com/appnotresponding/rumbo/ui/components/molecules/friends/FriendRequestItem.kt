package com.appnotresponding.rumbo.ui.components.molecules.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.ui.components.atoms.Avatar
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonSize
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle

@Composable
fun FriendRequestItem(
    user: User, onAcceptClick: () -> Unit, onDeclineClick: () -> Unit, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = MaterialTheme.shapes.medium
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(user = user)
            Column(modifier = Modifier.weight(6f)) {
                Text(
                    text = "${user.name} ${user.lastname}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Te envió una solicitud",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(
                modifier = Modifier.weight(4f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RumboButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Aceptar",
                    onClick = onAcceptClick,
                    style = RumboButtonStyle.Primary,
                    size = RumboButtonSize.Small,
                    icon = painterResource(R.drawable.ic_check)
                )
                RumboButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Rechazar",
                    onClick = onDeclineClick,
                    style = RumboButtonStyle.Secondary,
                    size = RumboButtonSize.Small,
                    icon = painterResource(R.drawable.ic_cancel)
                )
            }
        }
    }
}
