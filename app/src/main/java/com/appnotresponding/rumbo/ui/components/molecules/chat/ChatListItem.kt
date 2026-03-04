package com.appnotresponding.rumbo.ui.components.molecules.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.components.atoms.Avatar
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Componente que representa un elemento de la lista de chats, mostrando el nombre del remitente, el último mensaje, el estado (opcional) y la hora del último mensaje.
 *
 * @param user El objeto User que representa al remitente del mensaje, del cual se obtiene el nombre y las iniciales para el avatar.
 * @param lastMessage El texto del último mensaje enviado o recibido en el chat.
 * @param status Un estado opcional que muestra la ruta de la persona "Rumbo al Museo Nacional".
 * @param timestamp La hora o fecha del último mensaje, como "12:30", "Ayer" o "Lun".
 * @param modifier Modificador para ajustar el diseño del componente.
 * @param hasUnread Indica si hay mensajes no leídos en el chat.
 */
@Composable
fun ChatListItem(
    user: User,
    lastMessage: String,
    status: String? = null,
    timestamp: String,
    modifier: Modifier = Modifier,
    hasUnread: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Avatar(user = user)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (status != null) {
                        Text(
                            text = " · ",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = status,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                Box(modifier = Modifier.padding(top = 4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lastMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (timestamp.isNotEmpty()) {
                        Text(
                            text = timestamp,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else if (hasUnread) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatListItemPreviewContent() {
    Column(
        modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ChatListItem(
            user = sampleUser,
            lastMessage = "¡Nos vemos en el punto!",
            timestamp = "12:30",
        )
        ChatListItem(
            user = sampleUser,
            lastMessage = "Gracias por el viaje 🚗",
            timestamp = "Ayer",
        )
        ChatListItem(
            user = sampleUser,
            status = "Rumbo al Museo Nacional",
            lastMessage = "¿A qué hora sales?",
            timestamp = "Lun",
        )
    }
}

@Preview(showBackground = true, name = "ChatListItem - Light")
@Composable
private fun ChatListItemLightPreview() {
    RumboTheme(darkTheme = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            ChatListItemPreviewContent()
        }
    }
}

@Preview(showBackground = true, name = "ChatListItem - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun ChatListItemDarkPreview() {
    RumboTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            ChatListItemPreviewContent()
        }
    }
}
