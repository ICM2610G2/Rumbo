package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import com.appnotresponding.rumbo.R

//Tamaños predefinidos para el Avatar, con tamaños de texto asociados para las iniciales
enum class AvatarSize(val size: Dp) {
    Small(32.dp), Medium(40.dp), Large(56.dp)
}

/**
 * Componente Avatar para mostrar una imagen de perfil o iniciales de usuario.
 * @param modifier Modificador para ajustar el diseño, tamaño y comportamiento visual del componente Avatar.
 * @param pfp URL de la imagen de perfil (opcional)
 * @param initials Recibe el nombre o las iniciales del usuario
 * @param size Tamaño del avatar (Small, Medium, Large)
 * @param backgroundColor Color de fondo del avatar
 * @param contentDescription Descripción para accesibilidad
 * @param borderWidth Ancho del borde alrededor del avatar (0 para sin borde)
 * @param borderColor Color del borde alrededor del avatar
 * @param isOnline Indica si se debe mostrar el indicador de estado en línea.
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
    isOnline: Boolean = false
) {
    // Extraer las iniciales para mostrar, limitando a 2 caracteres y convirtiendo a mayúsculas
    val displayInitials = initials?.trim()?.take(2)?.uppercase()?.ifBlank { null }

    // Tamaño del indicador online proporcional al tamaño del avatar
    val indicatorSize = when (size) {
        AvatarSize.Small -> 10.dp
        AvatarSize.Medium -> 14.dp
        AvatarSize.Large -> 18.dp
    }
    val indicatorBorderWidth = when (size) {
        AvatarSize.Small -> 1.5.dp
        AvatarSize.Medium -> 2.5.dp
        AvatarSize.Large -> 3.dp
    }

    val initialsSize = when (size) {
        AvatarSize.Small -> MaterialTheme.typography.labelLarge
        AvatarSize.Medium -> MaterialTheme.typography.bodyMedium
        AvatarSize.Large -> MaterialTheme.typography.bodyLarge
    }

    //Contenedor circular para el avatar
    Box(
        modifier = modifier.size(size.size), contentAlignment = Alignment.Center
    ) {
        // Avatar circular
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                    SubcomposeAsyncImage(
                        model = pfp,
                        contentDescription = contentDescription,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        error = {
                            Image(
                                painter = painterResource(R.drawable.ic_user),
                                contentDescription = contentDescription,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(size.size * 0.2f)
                            )
                        },
                        success = {
                            SubcomposeAsyncImageContent()
                        }
                    )
                }

                displayInitials != null -> {
                    Text(
                        text = displayInitials,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = initialsSize,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Indicador de online
        if (isOnline) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(indicatorSize)
                    .background(Color(0xFF4CAF50), CircleShape)
                    .border(indicatorBorderWidth, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }
    }
}

@Preview(showBackground = true, name = "Avatar - Light")
@Composable
private fun AvatarLightPreview() {
    RumboTheme(darkTheme = false) {
        Row(
            modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (size in AvatarSize.entries) {
                    Avatar(initials = "SP", size = size, isOnline = true)
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (size in AvatarSize.entries) {
                    Avatar(initials = "SP", size = size, isOnline = false)
                }
            }

        }
    }
}

@Preview(showBackground = true, name = "Avatar - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun AvatarDarkPreview() {
    RumboTheme(darkTheme = true) {
        Row(
            modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (size in AvatarSize.entries) {
                    Avatar(initials = "SP", size = size, isOnline = true)
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (size in AvatarSize.entries) {
                    Avatar(initials = "SP", size = size, isOnline = false)
                }
            }

        }
    }
}