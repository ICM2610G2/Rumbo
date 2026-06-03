package com.appnotresponding.rumbo.ui.components.organisms.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.Review
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleReview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.SubcomposeAsyncImage
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.components.molecules.map.ReviewItem
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import java.util.Locale

/**
 * Componente que muestra una tarjeta de vista previa de un lugar, incluyendo su información y una lista de reseñas asociadas.
 * @param place El objeto Place que contiene la información del lugar a mostrar
 * @param reviews La lista de objetos Review que contienen las reseñas asociadas al lugar
 */
@Composable
fun formatHoursForCard(openHours: List<String>?): String {
    if (openHours.isNullOrEmpty()) return "No disponible"
    return openHours.joinToString("\n") { hours ->
        hours.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}

@Composable
fun PlacePreviewCard(
    place: Place,
    reviews: List<Review>,
    onNavigateClick: () -> Unit = {},
    onReviewClick: () -> Unit = {},
    onAddToItineraryClick: () -> Unit = {},
    isInItinerary: Boolean = false,
    isReviewEnabled: Boolean = true
) {
    Surface(shape = MaterialTheme.shapes.large) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = place.imageUrl,
                        contentDescription = "Imagen de ${place.name}",
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
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        place.openHours?.let { hours ->
                            if (hours.isNotEmpty() && hours.first().isNotBlank()) {
                                Text(
                                    text = "Horario:\n${formatHoursForCard(hours)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        place.price?.let { price ->
                            if (price.isNotBlank()) {
                                Text(
                                    text = "Precio: $price",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Text(
                            text = place.description ?: "No hay información",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RumboButton(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Navegar al lugar",
                                onClick = onNavigateClick,
                                style = RumboButtonStyle.Primary,
                                icon = painterResource(R.drawable.ic_map)
                            )
                            val itineraryText = if (isInItinerary) "Eliminar del Itinerario" else "Añadir al Itinerario"
                            val itineraryIcon = if (isInItinerary) R.drawable.ic_minus else R.drawable.ic_plus
                            RumboButton(
                                modifier = Modifier.fillMaxWidth(),
                                text = itineraryText,
                                onClick = onAddToItineraryClick,
                                style = RumboButtonStyle.Secondary,
                                icon = painterResource(itineraryIcon)
                            )
                            RumboButton(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Escribir Reseña",
                                onClick = onReviewClick,
                                style = RumboButtonStyle.Secondary,
                                icon = painterResource(R.drawable.ic_text_box_edit),
                                enabled = isReviewEnabled
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Reseñas de ${place.name}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(items = reviews, key = { it.id }) { review ->
                    ReviewItem(r = review)
                }
            }
        }
    }
}

private val sampleReviews = listOf(
    sampleReview, Review(
        id = "2",
        user = User(
            id = "2",
            name = "María López",
            email = "maria@mail.com",
            phone = "+573001234567",
            profilePictureUrl = null
        ),
        rating = 3.0f,
        text = "Buen lugar, pero estaba muy lleno.",
        time = System.currentTimeMillis()
    ), Review(
        id = "3",
        user = User(
            id = "3",
            name = "Carlos Pérez",
            email = "carlos@mail.com",
            phone = "+573009876543",
            profilePictureUrl = "https://randomuser.me/api/portraits/men/3.jpg"
        ),
        rating = 5.0f,
        text = "Increíble experiencia, 100% recomendado.",
        time = System.currentTimeMillis()
    )
)

@Preview(showBackground = true, name = "PlacePreviewCard - Light")
@Composable
private fun PlacePreviewCardLightPreview() {
    RumboTheme(darkTheme = false) {
        PlacePreviewCard(place = samplePlace, reviews = sampleReviews)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E, name = "PlacePreviewCard - Dark")
@Composable
private fun PlacePreviewCardDarkPreview() {
    RumboTheme(darkTheme = true) {
        PlacePreviewCard(place = samplePlace, reviews = sampleReviews)
    }
}