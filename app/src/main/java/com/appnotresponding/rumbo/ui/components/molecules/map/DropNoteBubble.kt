package com.appnotresponding.rumbo.ui.components.molecules.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.DropNote
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.sampleDropNote
import com.appnotresponding.rumbo.ui.components.atoms.Avatar
import com.appnotresponding.rumbo.ui.components.atoms.AvatarSize
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Un composable que representa una DropNote como un globo de pensamiento con el avatar del autor y dos "puntos" de pensamiento.
 *
 * El globo principal es un círculo que contiene el avatar, con un borde y sombra para dar profundidad. Debajo se encuentran
 * dos círculos más pequeños que representan los "puntos" de pensamiento, posicionados para crear una conexión visual con el globo principal.
 *
 * @param d Los datos de la DropNote que se mostrarán en el globo.
 * @param modifier Modifier opcional para ajustes de estilo y diseño.
 * @param avatar Lambda composable que define cómo mostrar el avatar. Por defecto, utiliza el composable Avatar con la información del autor.
 */
@Composable
fun DropNoteBubble(
    d: DropNote,
    modifier: Modifier = Modifier,
) {
    val borderColor = MaterialTheme.colorScheme.secondary
    val bgColor = MaterialTheme.colorScheme.surfaceContainerHighest

    Box(modifier = modifier) {
        // Main bubble
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .shadow(elevation = 6.dp, shape = CircleShape, clip = false)
                .background(color = bgColor, shape = CircleShape)
                .border(width = 3.dp, color = borderColor, shape = CircleShape)
                .padding(3.dp)
                .clip(CircleShape), contentAlignment = Alignment.Center
        ) {
            Avatar(
                user = User(
                    id = d.user.id,
                    name = d.user.name,
                    email = d.user.email,
                    phone = d.user.phone,
                    profilePictureUrl = d.authorAvatarUrl
                ), size = AvatarSize.Large
            )
        }

        // Medium thought dot
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = 18.dp, y = (-3).dp)
                .size(12.dp)
                .background(color = bgColor, shape = CircleShape)
                .border(width = 2.dp, color = borderColor, shape = CircleShape)
        )

        // Small thought dot
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = 24.dp, y = 0.dp)
                .size(7.dp)
                .background(color = bgColor, shape = CircleShape)
                .border(width = 1.5.dp, color = borderColor, shape = CircleShape)
        )
    }
}

@Preview(showBackground = true, name = "DropNoteBubble - Light")
@Composable
private fun DropNoteBubbleLightPreview() {
    RumboTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            DropNoteBubble(
                d = sampleDropNote
            )
        }
    }
}

@Preview(showBackground = true, name = "DropNoteBubble - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun DropNoteBubbleDarkPreview() {
    RumboTheme(darkTheme = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            DropNoteBubble(
                d = sampleDropNote
            )
        }
    }
}

