package com.appnotresponding.rumbo.ui.components.molecules.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonSize
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Compositor de mensajes del chat.
 *
 * Estados posibles, animados con [AnimatedContent]:
 * - Normal: campo de texto + botones + enviar.
 * - Recording: indicador animado "Grabando audio" + [MicButton] pulsante.
 * - AudioReady: "Nota de voz lista" + botones Eliminar / Enviar audio.
 *
 * Micro-interacciones:
 * - [MicButton]: pulso infinito radial en el fondo + color animado del ícono.
 * - [RecordingDot]: punto rojo que pulsa en escala de forma infinita.
 * - Botón enviar: [Box] + [clickable] con ripple acotado al círculo.
 */
@Composable
fun MessageComposer(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
    onImageClick: () -> Unit = {},
    onCameraClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onMicClick: () -> Unit = {},
    onSendAudio: () -> Unit = {},
    onDiscardAudio: () -> Unit = {},
    isRecordingAudio: Boolean = false,
    isAudioReady: Boolean = false,
) {
    val composerState = when {
        isAudioReady -> ComposerState.AudioReady
        isRecordingAudio -> ComposerState.Recording
        else -> ComposerState.Normal
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Área de contenido superior
            AnimatedContent(
                targetState = composerState,
                transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(150)) },
                label = "composerContent"
            ) { state ->
                when (state) {
                    ComposerState.Normal -> BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        decorationBox = { innerTextField ->
                            Box {
                                if (value.isEmpty()) {
                                    Text(
                                        text = "Mensaje",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    ComposerState.Recording -> Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RecordingDot()
                        Text(
                            text = "Grabando audio",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "· Toca el mic para parar",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    ComposerState.AudioReady -> Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_microphone),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Nota de voz lista",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Fila de acciones inferior
            AnimatedContent(
                targetState = composerState,
                transitionSpec = {
                    (fadeIn(tween(200)) + slideInVertically(tween(200)) { it }) togetherWith
                    (fadeOut(tween(150)) + slideOutVertically(tween(150)) { it })
                },
                label = "composerActions"
            ) { state ->
                when (state) {
                    ComposerState.AudioReady -> Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RumboButton(
                            modifier = Modifier.weight(1f),
                            text = "Eliminar",
                            onClick = onDiscardAudio,
                            style = RumboButtonStyle.Secondary,
                            size = RumboButtonSize.Small,
                            icon = painterResource(R.drawable.ic_destroy)
                        )
                        RumboButton(
                            modifier = Modifier.weight(1f),
                            text = "Enviar audio",
                            onClick = onSendAudio,
                            style = RumboButtonStyle.Primary,
                            size = RumboButtonSize.Small,
                            icon = painterResource(R.drawable.ic_send)
                        )
                    }

                    else -> Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onImageClick, modifier = Modifier.size(40.dp)) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_add_image),
                                    contentDescription = "Adjuntar imagen",
                                    modifier = Modifier.size(22.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = onCameraClick, modifier = Modifier.size(40.dp)) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_camera),
                                    contentDescription = "Tomar foto",
                                    modifier = Modifier.size(22.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = onLocationClick, modifier = Modifier.size(40.dp)) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_marker),
                                    contentDescription = "Compartir ubicación",
                                    modifier = Modifier.size(22.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            MicButton(isRecording = isRecordingAudio, onClick = onMicClick)
                        }

                        AnimatedVisibility(
                            visible = !isRecordingAudio,
                            enter = fadeIn(tween(200)),
                            exit = fadeOut(tween(150))
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondary)
                                    .clickable(onClick = onSendClick),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_send),
                                    contentDescription = "Enviar mensaje",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Botón de micrófono. Color del ícono y fondo animados al cambiar estado de grabación. */
@Composable
private fun MicButton(isRecording: Boolean, onClick: () -> Unit) {
    val micTint by animateColorAsState(
        targetValue = if (isRecording) MaterialTheme.colorScheme.error
                      else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(300),
        label = "micTint"
    )
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .then(
                if (isRecording) Modifier.background(MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_microphone),
            contentDescription = if (isRecording) "Parar grabación" else "Grabar audio",
            modifier = Modifier.size(22.dp),
            tint = micTint
        )
    }
}

/**
 * Punto rojo que pulsa en escala de forma infinita para indicar grabación activa.
 * Escala entre 0.7 y 1.4 con [EaseInOut] en 500ms.
 */
@Composable
private fun RecordingDot() {
    val infinite = rememberInfiniteTransition(label = "recordingDot")
    val scale by infinite.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotScale"
    )
    Box(
        modifier = Modifier
            .size(10.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(MaterialTheme.colorScheme.error, CircleShape)
    )
}

private enum class ComposerState { Normal, Recording, AudioReady }

@Preview(showBackground = true, name = "Composer - Normal", backgroundColor = 0xFF1E1E1E)
@Composable
private fun ComposerNormalPreview() {
    RumboTheme(darkTheme = true) { Box(Modifier.padding(16.dp)) { MessageComposer() } }
}

@Preview(showBackground = true, name = "Composer - Recording", backgroundColor = 0xFF1E1E1E)
@Composable
private fun ComposerRecordingPreview() {
    RumboTheme(darkTheme = true) { Box(Modifier.padding(16.dp)) { MessageComposer(isRecordingAudio = true) } }
}

@Preview(showBackground = true, name = "Composer - AudioReady", backgroundColor = 0xFF1E1E1E)
@Composable
private fun ComposerAudioReadyPreview() {
    RumboTheme(darkTheme = true) { Box(Modifier.padding(16.dp)) { MessageComposer(isAudioReady = true) } }
}
