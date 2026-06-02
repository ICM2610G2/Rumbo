package com.appnotresponding.rumbo.ui.components.molecules.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import android.media.MediaPlayer
import androidx.compose.ui.unit.sp

@Composable
fun ChatSeparator(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        )
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        )
    }
}

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
    mediaUrl: String? = null,
    mediaType: String? = null,
    isUserMessage: Boolean,
    senderName: String? = null,
    senderActivity: String? = null,
    type: ChatBubbleType = ChatBubbleType.Regular,
    place: Place? = null,
    onLocationClick: (() -> Unit)? = null
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

    val bubbleShape = if (isUserMessage) {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 4.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 4.dp,
            bottomEnd = 16.dp
        )
    }

    when (type) {
        ChatBubbleType.Regular -> {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = bubbleAlignment
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 280.dp)
                        .then(
                            if (mediaUrl != null) Modifier.width(240.dp) else Modifier
                        )
                        .background(backgroundColor, bubbleShape),
                    horizontalAlignment = horizontalAlignment,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {

                    Column(
                        modifier = Modifier
                            .then(if (mediaUrl != null) Modifier.fillMaxWidth() else Modifier)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (senderName != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 2.dp)
                            ) {
                                Text(
                                    text = senderName,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor.copy(alpha = 0.8f)
                                )
                                if (!senderActivity.isNullOrBlank()) {
                                    Text(
                                        text = " · ",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = contentColor.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = senderActivity,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = contentColor
                                    )
                                }
                            }
                        }

                        if (mediaUrl != null && mediaType == "image") {
                            AsyncImage(
                                modifier = Modifier.clip(MaterialTheme.shapes.medium).fillMaxWidth(),
                                model = mediaUrl,
                                contentScale = ContentScale.FillWidth,
                                contentDescription = null
                            )
                        } else if (mediaUrl != null && mediaType == "audio") {
                            var isPlaying by remember { mutableStateOf(false) }
                            var isPreparing by remember { mutableStateOf(false) }
                            val mediaPlayer = remember { MediaPlayer() }

                            DisposableEffect(mediaUrl) {
                                onDispose {
                                    mediaPlayer.release()
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
                                    .clickable {
                                        try {
                                            if (isPlaying) {
                                                mediaPlayer.stop()
                                                mediaPlayer.reset()
                                                isPlaying = false
                                            } else if (!isPreparing) {
                                                isPreparing = true
                                                mediaPlayer.reset()
                                                mediaPlayer.setDataSource(mediaUrl)
                                                mediaPlayer.setOnPreparedListener {
                                                    isPreparing = false
                                                    isPlaying = true
                                                    mediaPlayer.start()
                                                }
                                                mediaPlayer.setOnCompletionListener {
                                                    isPlaying = false
                                                }
                                                mediaPlayer.setOnErrorListener { _, _, _ ->
                                                    isPreparing = false
                                                    isPlaying = false
                                                    true
                                                }
                                                mediaPlayer.prepareAsync()
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            isPreparing = false
                                            isPlaying = false
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                            ) {
                                val icon = if (isPreparing) "⏳" else if (isPlaying) "⏸" else "▶"
                                Text(icon, color = MaterialTheme.colorScheme.primary)
                                Text(
                                    text = if (isPreparing) "Preparando..." else if (isPlaying) "Reproduciendo..." else "Nota de voz",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        val isMediaPlaceholder = mediaUrl != null && (message == "📷 Imagen" || message == "🎤 Nota de voz")
                        if (!isMediaPlaceholder && message.isNotBlank()) {
                            Text(
                                text = message,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    lineHeight = 22.sp
                                ),
                                color = contentColor
                            )
                        }
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
                        .widthIn(max = 280.dp)
                        .background(
                            backgroundColor,
                            bubbleShape
                        )
                        .clip(bubbleShape)
                        .clickable(enabled = onLocationClick != null) { onLocationClick?.invoke() },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Ubicación",
                            style = MaterialTheme.typography.labelMedium,
                            color = contentColor,
                            fontStyle = FontStyle.Italic
                        )
                    }

                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 2f)
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 28.dp,
                                    bottomEnd = 28.dp,
                                    topStart = 0.dp,
                                    topEnd = 0.dp
                                )
                            ),
                        painter = painterResource(R.mipmap.img_map),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Mapa de ubicación compartida"
                    )
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
                            .widthIn(max = 280.dp)
                            .background(backgroundColor, bubbleShape),
                        horizontalAlignment = horizontalAlignment,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = horizontalAlignment,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (senderName != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = senderName,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = contentColor
                                    )
                                    if (!senderActivity.isNullOrBlank()) {
                                        Text(
                                            text = " · ",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = contentColor.copy(alpha = 0.6f)
                                        )
                                        Text(
                                            text = senderActivity,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = contentColor
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "Únete a mi ruta compartida",
                                style = MaterialTheme.typography.titleSmall,
                                color = contentColor,
                            )

                            Row {
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
                                        })
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
                                        text = place.price ?: "No hay",
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
    val placeholderImage = ImageRequest.Builder(context).data(R.mipmap.img_mock).build()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Regular - received with image
        ChatBubble(
            message = "¡Hola! ¿Cómo estás?",
            mediaUrl = null,
            mediaType = null,
            isUserMessage = false,
            senderName = "Carlos",
            type = ChatBubbleType.Regular
        )
        // Regular - sent with image
        ChatBubble(
            message = "¡Todo bien! ¿Y tú?",
            mediaUrl = null,
            mediaType = null,
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
            message = "", isUserMessage = true, type = ChatBubbleType.Location
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

