package com.appnotresponding.rumbo.ui.components.molecules.map

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.DropNote
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.sampleDropNote
import com.appnotresponding.rumbo.ui.components.atoms.Avatar
import com.appnotresponding.rumbo.ui.components.atoms.AvatarSize
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import androidx.compose.foundation.layout.padding
import com.appnotresponding.rumbo.models.sampleUser

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
    author: User?,
    modifier: Modifier = Modifier,
) {
    val borderColor = MaterialTheme.colorScheme.secondary
    val bgColor = MaterialTheme.colorScheme.surfaceContainerHighest

    Box(
        modifier = modifier.size(48.dp)
    ) {
        Avatar(
            user = author,
            size = AvatarSize.Medium,
            borderWidth = 2.dp,
            borderColor = borderColor,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Medium thought dot
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = 10.dp, y = (-2).dp)
                .size(7.dp)
                .background(color = bgColor, shape = CircleShape)
                .border(width = 1.5.dp, color = borderColor, shape = CircleShape)
        )

        // Small thought dot
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = 14.dp, y = 1.dp)
                .size(4.dp)
                .background(color = bgColor, shape = CircleShape)
                .border(width = 1.0.dp, color = borderColor, shape = CircleShape)
        )
    }
}

@Preview(showBackground = true, name = "DropNoteBubble - Light")
@Composable
private fun DropNoteBubbleLightPreview() {
    RumboTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            DropNoteBubble(
                d = sampleDropNote,
                author = sampleUser
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
                d = sampleDropNote,
                author = sampleUser
            )
        }
    }
}