package com.appnotresponding.rumbo.ui.components.molecules.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.Review
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleReview
import com.appnotresponding.rumbo.ui.components.atoms.Avatar
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.components.atoms.RumboRatingDisplay
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Componente que muestra la información de un lugar, incluyendo su imagen, nombre, descripción y un botón para escribir una reseña.
 * @param p El objeto Place que contiene la información del lugar a mostrar
 */
@Composable
fun PlaceInfo(p: Place, onNavigateClick: () -> Unit = {}, onReviewClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(4f)
                .aspectRatio(1f)
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 2.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = p.imageUrl,
                contentDescription = "Imagen de ${p.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = {
                    Image(
                        painter = painterResource(R.drawable.ic_picture),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                        contentScale = ContentScale.Crop
                    )
                })
        }
        Column(
            modifier = Modifier
                .weight(6f)
                .padding(top = 2.dp, start = 4.dp, end = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = p.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            p.openHours?.let { hours ->
                if (hours.isNotEmpty() && hours.first().isNotBlank()) {
                    Text(
                        text = "Horario: ${hours.joinToString(", ")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            p.price?.let { price ->
                if (price.isNotBlank()) {
                    Text(
                        text = "Precio: $price",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Text(
                text = p.description ?: "No hay información",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                RumboButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Navegar al lugar",
                    onClick = onNavigateClick,
                    style = RumboButtonStyle.Primary,
                    icon = painterResource(R.drawable.ic_map)
                )
                RumboButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Escribir Reseña",
                    onClick = onReviewClick,
                    style = RumboButtonStyle.Secondary,
                    icon = painterResource(R.drawable.ic_text_box_edit)
                )
            }
        }
    }
}

/**
 * Componente que muestra una reseña individual, incluyendo el avatar del usuario, su nombre, la calificación y el texto de la reseña.
 * @param r El objeto Review que contiene la información de la reseña a mostrar
 */
@Composable
fun ReviewItem(r: Review) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .weight(2f)
                .aspectRatio(1f)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Avatar(user = r.user)
        }
        Column(
            modifier = Modifier
                .weight(8f)
                .padding(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = r.user.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            RumboRatingDisplay(rating = r.rating)
            Text(
                text = r.text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (!r.photoUrl.isNullOrEmpty()) {
                SubcomposeAsyncImage(
                    model = r.photoUrl,
                    contentDescription = "Foto adjunta",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .aspectRatio(16f/9f)
                        .clip(MaterialTheme.shapes.medium)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PlaceInfoLightPreview() {
    RumboTheme(darkTheme = false) {
        PlaceInfo(p = samplePlace)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
private fun PlaceInfoDarkPreview() {
    RumboTheme(darkTheme = true) {
        PlaceInfo(p = samplePlace)
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewItemLightPreview() {
    RumboTheme(darkTheme = false) {
        ReviewItem(r = sampleReview)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
private fun ReviewItemDarkPreview() {
    RumboTheme(darkTheme = true) {
        ReviewItem(r = sampleReview)
    }
}