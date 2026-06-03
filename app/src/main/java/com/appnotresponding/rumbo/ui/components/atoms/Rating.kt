package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import java.util.Locale

/**
 * Estrellas de valoración interactivas o de solo lectura.
 * En modo interactivo: scale spring 1.25 al presionar cada estrella (sin ripple, solo escala).
 * En ambos modos: [animateColorAsState] para transición suave de color al cambiar rating.
 */
@Composable
fun RumboRatingStar(
    rating: Float,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Dp = 24.dp,
    filledColor: Color = MaterialTheme.colorScheme.primary,
    emptyColor: Color = MaterialTheme.colorScheme.outlineVariant,
    onRatingChanged: ((Float) -> Unit)? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isFilled = i <= rating || i - 0.5f <= rating
            val starIcon = when {
                i <= rating -> R.drawable.ic_star_filled
                i - 0.5f <= rating -> R.drawable.ic_star_half
                else -> R.drawable.ic_star_empty
            }

            // Color animado para transición suave al cambiar rating
            val tint by animateColorAsState(
                targetValue = if (isFilled) filledColor else emptyColor,
                animationSpec = tween(durationMillis = 150),
                label = "starTint$i"
            )

            // InteractionSource siempre creado (regla de composables); se usa solo si es interactivo
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(
                targetValue = if (onRatingChanged != null && isPressed) 1.25f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessHigh
                ),
                label = "starScale$i"
            )

            Icon(
                painter = painterResource(id = starIcon),
                contentDescription = "Star $i",
                modifier = Modifier
                    .size(starSize)
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .then(
                        if (onRatingChanged != null) Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null  // Solo escala, sin ripple sobre el ícono
                        ) { onRatingChanged(i.toFloat()) }
                        else Modifier
                    ),
                tint = tint
            )
        }
    }
}

/**
 * Visualización de calificación con estrellas y valor numérico, solo lectura.
 */
@Composable
fun RumboRatingDisplay(
    rating: Float,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Dp = 12.dp,
    showText: Boolean = true,
    filledColor: Color = MaterialTheme.colorScheme.primary,
    emptyColor: Color = MaterialTheme.colorScheme.outlineVariant
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RumboRatingStar(
            rating = rating,
            maxStars = maxStars,
            starSize = starSize,
            filledColor = filledColor,
            emptyColor = emptyColor
        )
        if (showText) {
            Text(
                text = String.format(Locale.getDefault(), "%.1f", rating),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Preview(showBackground = true, name = "RatingStar - Light")
@Composable
private fun RatingStarLightPreview() {
    RumboTheme(darkTheme = false) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RumboRatingDisplay(rating = 4.5f)
            RumboRatingDisplay(rating = 3.0f, starSize = 20.dp)
            RumboRatingDisplay(rating = 1.0f)
            RumboRatingDisplay(rating = 5.0f)

            var rating by remember { mutableFloatStateOf(3f) }
            RumboRatingStar(rating = rating, starSize = 32.dp, onRatingChanged = { rating = it })
            Text(
                text = "Toca para valorar: ${rating.toInt()}/5",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, name = "RatingStar - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun RatingStarDarkPreview() {
    RumboTheme(darkTheme = true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RumboRatingDisplay(rating = 4.5f)
            RumboRatingDisplay(rating = 3.0f, starSize = 20.dp)
            RumboRatingDisplay(rating = 1.0f)
            RumboRatingDisplay(rating = 5.0f)

            var rating by remember { mutableFloatStateOf(3f) }
            RumboRatingStar(rating = rating, starSize = 32.dp, onRatingChanged = { rating = it })
            Text(
                text = "Toca para valorar: ${rating.toInt()}/5",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
