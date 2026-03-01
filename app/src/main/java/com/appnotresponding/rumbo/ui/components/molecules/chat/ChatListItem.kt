package com.appnotresponding.rumbo.ui.components.molecules.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.ui.components.atoms.Avatar
import com.appnotresponding.rumbo.ui.theme.RumboTheme


/**
 * Componente que representa un elemento de la lista de chats, mostrando el nombre del remitente, el último mensaje, el estado (opcional) y la hora del último mensaje.
 *
 * @param senderName El nombre del remitente del chat.
 * @param lastMessage El texto del último mensaje enviado o recibido en el chat.
 * @param status Un estado opcional que muestra la ruta de la persona "Rumbo al Museo Nacional".
 * @param timestamp La hora o fecha del último mensaje, como "12:30", "Ayer" o "Lun".
 * @param avatar Un composable opcional para mostrar el avatar del remitente, por defecto se muestra un Avatar con las iniciales del nombre del remitente.
 */
@Composable
fun ChatListItem(
    senderName: String,
    lastMessage: String,
    status: String? = null,
    timestamp: String,
    avatar: @Composable () -> Unit = { Avatar(initials = senderName) }
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box() {
            avatar()
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = senderName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (status != null)
                    Text(
                        text = status,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

    }
}

@Composable
private fun ChatListItemPreviewContent() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ChatListItem(
            senderName = "Carlos Pérez",
            lastMessage = "¡Nos vemos en el punto!",
            timestamp = "12:30",
        )
        ChatListItem(
            senderName = "María López",
            lastMessage = "Gracias por el viaje 🚗",
            timestamp = "Ayer",
        )
        ChatListItem(
            senderName = "Samuel Pico",
            status = "Rumbo al Museo Nacional",
            lastMessage = "¿A qué hora sales?",
            timestamp = "Lun",
            avatar = { Avatar(pfp = "https://github.com/Samu-Kiss.png", initials = "JG") }
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
