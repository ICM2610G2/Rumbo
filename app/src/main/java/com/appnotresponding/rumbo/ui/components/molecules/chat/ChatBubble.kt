package com.appnotresponding.rumbo.ui.components.molecules.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.appnotresponding.rumbo.BuildConfig
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonSize
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import android.media.MediaPlayer
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    modifier: Modifier = Modifier,
    message: String,
    mediaUrl: String? = null,
    mediaType: String? = null,
    isUserMessage: Boolean,
    senderName: String? = null,
    senderActivity: String? = null,
    type: ChatBubbleType = ChatBubbleType.Regular,
    place: Place? = null,
    timestamp: Long = 0,
    seenText: String? = null,
    isLastInSequence: Boolean = true,
    onMediaClick: ((String) -> Unit)? = null,
    onLocationClick: (() -> Unit)? = null
) {
    val horizontalAlignment = if (isUserMessage) {
        Alignment.End
    } else {
        Alignment.Start
    }

    val backgroundColor = if (isUserMessage) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest
    }

    val contentColor = if (isUserMessage) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val bubbleAlignment = if (isUserMessage) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    val bubbleShape = when {
        isUserMessage && isLastInSequence -> RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 4.dp
        )
        !isUserMessage && isLastInSequence -> RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 4.dp,
            bottomEnd = 16.dp
        )
        else -> RoundedCornerShape(16.dp)
    }

    when (type) {
        ChatBubbleType.Regular -> {
            Row(
                modifier = modifier.fillMaxWidth(), horizontalArrangement = bubbleAlignment
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 280.dp)
                        .then(if (mediaUrl != null) Modifier.widthIn(min = 220.dp, max = 280.dp) else Modifier)
                        .background(backgroundColor, bubbleShape),
                    horizontalAlignment = horizontalAlignment,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {

                    Column(
                        modifier = Modifier
                            .then(if (mediaUrl != null) Modifier.fillMaxWidth() else Modifier)
                            .padding(16.dp),
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
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .fillMaxWidth()
                                    .clickable(enabled = onMediaClick != null) {
                                        onMediaClick?.invoke(mediaUrl)
                                    },
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
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val icon = if (isPreparing) "..." else if (isPlaying) "II" else "▶"
                                    Text(
                                        text = icon,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val bars = if (isPlaying) listOf(10, 18, 13, 22, 15, 24, 12, 19, 14, 20, 10, 18, 13, 22, 15) else listOf(8, 14, 10, 16, 11, 18, 9, 15, 10, 13, 8, 14, 10, 16, 11)
                                    bars.forEach { barHeight ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(barHeight.dp)
                                                .background(contentColor.copy(alpha = 0.55f), RoundedCornerShape(8.dp))
                                        )
                                    }
                                }
                                Text(
                                    text = if (isPreparing) "..." else "0:00",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = contentColor.copy(alpha = 0.7f)
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

                        if (timestamp > 0 || seenText != null) {
                            Row(
                                modifier = Modifier.align(Alignment.End),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (timestamp > 0) {
                                    Text(
                                        text = formatMessageTime(timestamp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = contentColor.copy(alpha = 0.62f)
                                    )
                                }
                                if (seenText != null) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = seenText,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (seenText == "Visto") MaterialTheme.colorScheme.primary else contentColor.copy(alpha = 0.62f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }

        ChatBubbleType.Location -> {
            Row(
                modifier = modifier.fillMaxWidth(), horizontalArrangement = bubbleAlignment
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

                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 2f),

                        model = staticMapPreviewUrl(message),
                        fallback = painterResource(R.mipmap.img_map),
                        error = painterResource(R.mipmap.img_map),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Mapa de ubicación compartida"
                    )
                    if (timestamp > 0) {
                        Text(
                            text = formatMessageTime(timestamp),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = contentColor.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        ChatBubbleType.LiveActivity -> {
            if (place != null) {
                Row(
                    modifier = modifier.fillMaxWidth(), horizontalArrangement = bubbleAlignment
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

private fun formatMessageTime(timestamp: Long): String {
    return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
}

private fun staticMapPreviewUrl(message: String): String {
    val parts = message.removePrefix("Ubicación: ").split(",")
    val lat = parts.getOrNull(0)?.trim()?.toDoubleOrNull() ?: 4.627293
    val lng = parts.getOrNull(1)?.trim()?.toDoubleOrNull() ?: -74.063228
    return "https://maps.googleapis.com/maps/api/staticmap?center=$lat,$lng&zoom=17&size=640x360&scale=2&markers=color:red%7C$lat,$lng&key=${BuildConfig.MAPS_API_KEY}"
}


@Composable
private fun ChatBubblePreviewContent() {
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

