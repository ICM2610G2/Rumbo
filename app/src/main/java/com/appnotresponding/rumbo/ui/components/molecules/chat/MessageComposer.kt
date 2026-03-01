package com.appnotresponding.rumbo.ui.components.molecules.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Componente de composición de mensajes para el chat.
 * Contiene un campo de texto en la parte superior, botones de acción (imagen, ubicación, micrófono)
 * en la parte inferior izquierda y un botón de envío circular en la parte inferior derecha.
 *
 * @param value El texto actual del mensaje
 * @param onValueChange Callback cuando el texto cambia
 * @param onSendClick Callback cuando se presiona el botón de enviar
 * @param onImageClick Callback cuando se presiona el botón de imagen
 * @param onLocationClick Callback cuando se presiona el botón de ubicación
 * @param onMicClick Callback cuando se presiona el botón de micrófono
 * @param modifier Modificador para personalizar la apariencia
 */
@Composable
fun MessageComposer(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
    onImageClick: () -> Unit = {},
    onLocationClick: () -> Unit = {},
    onMicClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Text input area
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
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

            // Bottom row: action icons on the left, send button on the right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Action icons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onImageClick, modifier = Modifier.size(40.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_image),
                            contentDescription = "Adjuntar imagen",
                            modifier = Modifier.size(22.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onLocationClick, modifier = Modifier.size(40.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_marker),
                            contentDescription = "Enviar ubicación",
                            modifier = Modifier.size(22.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onMicClick, modifier = Modifier.size(40.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_microphone),
                            contentDescription = "Grabar audio",
                            modifier = Modifier.size(22.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Send button
                IconButton(
                    onClick = onSendClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = "Enviar mensaje",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "MessageComposer - Light")
@Composable
private fun MessageComposerLightPreview() {
    RumboTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            MessageComposer()
        }
    }
}

@Preview(showBackground = true, name = "MessageComposer - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun MessageComposerDarkPreview() {
    RumboTheme(darkTheme = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            MessageComposer()
        }
    }
}