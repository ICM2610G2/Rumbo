package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.AppTheme
import java.util.Locale

/**
 * Componente interno de valoración con estrellas para mostrar y permitir la selección de una calificación.
 * @param rating Valor de la calificación actual (puede ser un número decimal para medias estrellas)
 * @param modifier  Modificador para personalizar la apariencia y comportamiento del componente
 * @param maxStars Número máximo de estrellas a mostrar (por defecto 5)
 * @param starSize Tamaño de cada estrella (por defecto 24.dp)
 * @param filledColor Color de las estrellas llenas (por defecto el color primario del tema)
 * @param emptyColor Color de las estrellas vacías (por defecto el color de variante de contorno del tema)
 * @param onRatingChanged Función opcional que se llama cuando el usuario selecciona una nueva calificación, recibiendo el nuevo valor de calificación como argumento
 */
@Composable
fun RumboRatingStar(
    rating: Float,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Dp = 24.dp,
    filledColor: Color = AppTheme.colorScheme.primary,
    emptyColor: Color = AppTheme.colorScheme.outlineVariant,
    onRatingChanged: ((Float) -> Unit)? = null
) {
    // Contenedor horizontal para las estrellas, con espacio entre ellas y alineación vertical centrada
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Crea cada estrella según el valor de rating, usando iconos diferentes para estrellas llenas, medias y vacías
        for (i in 1..maxStars) {
            val starIcon = when {
                i <= rating -> R.drawable.ic_star_filled
                i - 0.5f <= rating -> R.drawable.ic_star_half
                else -> R.drawable.ic_star_empty
            }
            val tint = if (i <= rating || i - 0.5f <= rating) filledColor else emptyColor
            Icon(
                painter = painterResource(id = starIcon),
                contentDescription = "Star $i",
                modifier = Modifier
                    .size(starSize)
                    .then(
                        if (onRatingChanged != null) {
                            Modifier.clickable { onRatingChanged(i.toFloat()) }
                        } else Modifier
                    ),
                tint = tint
            )
        }
    }
}

/**
 * Componente de visualización de calificación que muestra una fila de estrellas junto con el valor numérico de la calificación.
 * @param rating Valor de la calificación a mostrar
 * @param modifier Modificador para personalizar la apariencia y comportamiento del componente
 * @param maxStars Número máximo de estrellas a mostrar (por defecto 5)
 * @param starSize Tamaño de cada estrella (por defecto 16.dp)
 * @param showText Indica si se debe mostrar el valor numérico de la calificación junto a las estrellas (por defecto true)
 * @param filledColor Color de las estrellas llenas (por defecto el color primario del tema)
 * @param emptyColor Color de las estrellas vacías (por defecto el color de variante de contorno del tema)
 */
@Composable
fun RumboRatingDisplay(
    rating: Float,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Dp = 16.dp,
    showText: Boolean = true,
    filledColor: Color = AppTheme.colorScheme.primary,
    emptyColor: Color = AppTheme.colorScheme.outlineVariant
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
            RumboText(
                text = String.format(Locale.getDefault(), "%.1f", rating),
                style = RumboTextStyle.Button,
                color = AppTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Preview(showBackground = true, name = "RatingStar - Light")
@Composable
private fun RatingStarLightPreview() {
    AppTheme(darkTheme = false) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RumboRatingDisplay(rating = 4.5f)
            RumboRatingDisplay(rating = 3.0f, starSize = 20.dp)
            RumboRatingDisplay(rating = 1.0f)
            RumboRatingDisplay(rating = 5.0f)

            var rating by remember { mutableFloatStateOf(3f) }
            RumboRatingStar(
                rating = rating,
                starSize = 32.dp,
                onRatingChanged = { rating = it }
            )
            RumboText(
                text = "Toca para valorar: ${rating.toInt()}/5",
                style = RumboTextStyle.Body,
                color = AppTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, name = "RatingStar - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun RatingStarDarkPreview() {
    AppTheme(darkTheme = true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RumboRatingDisplay(rating = 4.5f)
            RumboRatingDisplay(rating = 3.0f, starSize = 20.dp)
            RumboRatingDisplay(rating = 1.0f)
            RumboRatingDisplay(rating = 5.0f)

            var rating by remember { mutableFloatStateOf(3f) }
            RumboRatingStar(
                rating = rating,
                starSize = 32.dp,
                onRatingChanged = { rating = it }
            )
            RumboText(
                text = "Toca para valorar: ${rating.toInt()}/5",
                style = RumboTextStyle.Body,
                color = AppTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}