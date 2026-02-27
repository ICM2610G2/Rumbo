package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.appnotresponding.rumbo.ui.theme.RumboTheme

//Tamaños predefinidos para el Avatar, con tamaños de texto asociados para las iniciales
enum class AvatarSize(val size: Dp) {
    Small(32.dp),
    Medium(40.dp),
    Large(56.dp)
}

/**
 * Componente Avatar para mostrar una imagen de perfil o iniciales de usuario.
 * @param modifier
 * @param pfp URL de la imagen de perfil (opcional)
 * @param initials Recibe el nombre o las iniciales del usuario
 * @param size Tamaño del avatar (Small, Medium, Large)
 * @param backgroundColor Color de fondo del avatar
 * @param contentDescription Descripción para accesibilidad
 * @param borderWidth Ancho del borde alrededor del avatar (0 para sin borde)
 * @param borderColor Color del borde alrededor del avatar
 */
@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    pfp: String? = null,
    initials: String? = null,
    size: AvatarSize = AvatarSize.Medium,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentDescription: String? = "Avatar Image",
    borderWidth: Dp = 0.dp,
    borderColor: Color = MaterialTheme.colorScheme.outline,
) {
    // Extraer las iniciales para mostrar, limitando a 2 caracteres y convirtiendo a mayúsculas
    val displayInitials = initials?.trim()?.take(2)?.uppercase()?.ifBlank { null }

    //Contenedor circular para el avatar
    Box(
        modifier = modifier
            .size(size.size)
            .background(color = backgroundColor, shape = CircleShape)
            .then(
                if (borderWidth > 0.dp) {
                    Modifier.border(borderWidth, borderColor, CircleShape)
                } else {
                    Modifier
                }
            ), contentAlignment = Alignment.Center
    ) {
        //Verificar si hay foto de perfil
        when {
            pfp != null -> {
                AsyncImage(
                    model = pfp,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            displayInitials != null -> {
                Text(
                    text = displayInitials,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Avatar - Light")
@Composable
private fun AvatarLightPreview() {
    RumboTheme(darkTheme = false) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            for (size in AvatarSize.entries) {
                Avatar(initials = "SP", size = size)
            }
        }
    }
}

@Preview(showBackground = true, name = "Avatar - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun AvatarDarkPreview() {
    RumboTheme(darkTheme = true) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            for (size in AvatarSize.entries) {
                Avatar(initials = "SP", size = size)
            }
        }
    }
}