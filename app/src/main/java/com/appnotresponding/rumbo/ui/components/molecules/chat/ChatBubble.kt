package com.appnotresponding.rumbo.ui.components.molecules.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Componente que representa un mensaje en el chat, con soporte para texto e imagen.
 *
 * @param message El texto del mensaje.
 * @param messageImage Una imagen opcional asociada al mensaje.
 * @param isUserMessage Indica si el mensaje es del usuario o de otro participante.b
 * @param senderName El nombre del remitente, opcional para mensajes del usuario.
 */
@Composable
fun ChatBubble(
    message: String,
    messageImage: ImageRequest? = null,
    isUserMessage: Boolean,
    senderName: String? = null
) {
    val horizontalAlignment = if (isUserMessage) {
        Alignment.End
    } else {
        Alignment.Start
    }

    val backgroundColor = if (isUserMessage) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.primary
    }

    val contentColor = if (isUserMessage) {
        MaterialTheme.colorScheme.onSecondary
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    val bubbleAlignment = if (isUserMessage) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = bubbleAlignment
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(.75f)
                .padding(8.dp)
                .background(backgroundColor, MaterialTheme.shapes.medium),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {

            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = horizontalAlignment,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (senderName != null) {
                    Text(text = senderName, style = MaterialTheme.typography.labelLarge, color = contentColor)
                }

                if (messageImage != null) {
                    AsyncImage(
                        modifier = Modifier.clip(MaterialTheme.shapes.medium),
                        model = messageImage,
                        contentDescription = null
                    )
                }
                Text(text = message, style = MaterialTheme.typography.bodyMedium, color = contentColor)
            }
        }
    }
}

@Composable
private fun ChatBubblePreviewContent() {
    val context = LocalContext.current
    val placeholderImage = ImageRequest.Builder(context)
        .data(R.drawable.img_mock)
        .build()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ChatBubble(
            message = "¡Hola! ¿Cómo estás?",
            messageImage = placeholderImage,
            isUserMessage = false,
            senderName = "Carlos"
        )
        ChatBubble(
            message = "¡Todo bien! ¿Y tú?",
            messageImage = placeholderImage,
            isUserMessage = true,
            senderName = null
        )
        ChatBubble(
            message = "Perfecto, nos vemos en el punto de encuentro 🚗",
            isUserMessage = false,
            senderName = "Carlos"
        )
    }
}

@Preview(showBackground = true, name = "ChatBubble - Light")
@Composable
private fun ChatBubbleLightPreview() {
    RumboTheme(darkTheme = false) {
        ChatBubblePreviewContent()
    }
}

@Preview(showBackground = true, name = "ChatBubble - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun ChatBubbleDarkPreview() {
    RumboTheme(darkTheme = true) {
        ChatBubblePreviewContent()
    }
}

