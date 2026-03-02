package com.appnotresponding.rumbo.ui.components.molecules.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonSize
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.theme.RumboTheme

//TODO: Separator Component

enum class ChatBubbleType {
    Regular, Location, LiveActivity
}

/**
 * Componente que representa un mensaje en el chat, con soporte para texto e imagen.
 *
 * @param message El texto del mensaje.
 * @param messageImage Una imagen opcional asociada al mensaje.
 * @param isUserMessage Indica si el mensaje es del usuario o de otro participante.
 * @param senderName El nombre del remitente, opcional para mensajes del usuario.
 * @param type El tipo de burbuja de chat (Regular, Location, LiveActivity), que determina el diseño y contenido mostrado.
 * @param place El objeto [Place] asociado al mensaje, requerido cuando el tipo es [ChatBubbleType.LiveActivity].
 */
@Composable
fun ChatBubble(
    message: String,
    messageImage: ImageRequest? = null,
    isUserMessage: Boolean,
    senderName: String? = null,
    type: ChatBubbleType = ChatBubbleType.Regular,
    place: Place? = null
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

    when (type) {
        ChatBubbleType.Regular -> {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = bubbleAlignment
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.75f)
                        .padding(8.dp)
                        .background(backgroundColor, MaterialTheme.shapes.large),
                    horizontalAlignment = horizontalAlignment,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = horizontalAlignment,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (senderName != null) {
                            Text(
                                text = senderName,
                                style = MaterialTheme.typography.labelLarge,
                                color = contentColor
                            )
                        }

                        if (messageImage != null) {
                            AsyncImage(
                                modifier = Modifier.clip(MaterialTheme.shapes.medium),
                                model = messageImage,
                                contentDescription = null
                            )
                        }
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = contentColor
                        )
                    }
                }
            }

        }

        ChatBubbleType.Location -> {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = bubbleAlignment
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.75f)
                        .padding(8.dp)
                        .background(backgroundColor, MaterialTheme.shapes.large),
                    horizontalAlignment = horizontalAlignment,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = horizontalAlignment,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (senderName != null) {
                            Text(
                                text = senderName,
                                style = MaterialTheme.typography.labelLarge,
                                color = contentColor
                            )
                        }

                        Text(
                            text = "Ubicación compartida",
                            style = MaterialTheme.typography.labelMedium,
                            color = contentColor,
                            fontStyle = FontStyle.Italic
                        )

                        //Location Preview Image
                        AsyncImage(
                            modifier = Modifier.clip(MaterialTheme.shapes.medium).fillMaxWidth(),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(R.drawable.img_map).build(),
                            contentDescription = null
                        )
                    }
                }
            }
        }

        ChatBubbleType.LiveActivity -> {
            if (place != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = bubbleAlignment
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(.75f)
                            .padding(8.dp)
                            .background(backgroundColor, MaterialTheme.shapes.large),
                        horizontalAlignment = horizontalAlignment,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = horizontalAlignment,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (senderName != null) {
                                Text(
                                    text = senderName,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = contentColor
                                )
                            }

                            Text(
                                text = "Únete a mi ruta compartida",
                                style = MaterialTheme.typography.titleSmall,
                                color = contentColor,
                            )

                            Row() {
                                Box(
                                    modifier = Modifier
                                        .weight(3f)
                                        .aspectRatio(1f)
                                        .clip(MaterialTheme.shapes.large)
                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    SubcomposeAsyncImage(
                                        model = place.imageUrl,
                                        contentDescription = "Imagen de ${place.name}",
                                        contentScale = ContentScale.Crop,
                                        error = {
                                            Image(
                                                painter = painterResource(R.drawable.ic_picture),
                                                contentDescription = null,
                                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
                                            )
                                        }
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(7f)
                                        .padding(start = 8.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = place.name,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = contentColor
                                    )
                                    Text(
                                        text = place.price,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = contentColor.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            RumboButton(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Unirse",
                                onClick = { /*TODO: Implementar acción de unirse a la ruta*/ },
                                style = RumboButtonStyle.Secondary,
                                size = RumboButtonSize.Small,
                                icon = painterResource(R.drawable.ic_user_add)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ChatBubblePreviewContent() {
    val context = LocalContext.current
    val placeholderImage = ImageRequest.Builder(context).data(R.drawable.img_mock).build()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Regular - received with image
        ChatBubble(
            message = "¡Hola! ¿Cómo estás?",
            messageImage = placeholderImage,
            isUserMessage = false,
            senderName = "Carlos",
            type = ChatBubbleType.Regular
        )
        // Regular - sent with image
        ChatBubble(
            message = "¡Todo bien! ¿Y tú?",
            messageImage = placeholderImage,
            isUserMessage = true,
            senderName = null,
            type = ChatBubbleType.Regular
        )
        // Regular - text only
        ChatBubble(
            message = "Perfecto, nos vemos en el punto de encuentro 🚗",
            isUserMessage = false,
            senderName = "Carlos",
            type = ChatBubbleType.Regular
        )
        // Location - received
        ChatBubble(
            message = "",
            isUserMessage = false,
            senderName = "Carlos",
            type = ChatBubbleType.Location
        )
        // Location - sent
        ChatBubble(
            message = "",
            isUserMessage = true,
            type = ChatBubbleType.Location
        )
        // LiveActivity - received
        ChatBubble(
            message = "",
            isUserMessage = false,
            senderName = "Carlos",
            type = ChatBubbleType.LiveActivity,
            place = samplePlace
        )
        // LiveActivity - sent
        ChatBubble(
            message = "",
            isUserMessage = true,
            type = ChatBubbleType.LiveActivity,
            place = samplePlace
        )
    }
}

@Preview(showBackground = true, name = "ChatBubble - Light", heightDp = 2000)

@Composable
private fun ChatBubbleLightPreview() {
    RumboTheme(darkTheme = false) {
        ChatBubblePreviewContent()
    }
}

@Preview(
    showBackground = true, name = "ChatBubble - Dark", backgroundColor = 0xFF1E1E1E, heightDp = 2000
)

@Composable
private fun ChatBubbleDarkPreview() {
    RumboTheme(darkTheme = true) {
        ChatBubblePreviewContent()
    }
}

