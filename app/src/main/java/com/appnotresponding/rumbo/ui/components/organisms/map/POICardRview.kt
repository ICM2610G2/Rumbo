package com.appnotresponding.rumbo.ui.components.organisms.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.Review
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleReview
import com.appnotresponding.rumbo.ui.components.molecules.map.PlaceInfo
import com.appnotresponding.rumbo.ui.components.molecules.map.ReviewItem
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Componente que muestra una tarjeta de vista previa de un lugar, incluyendo su información y una lista de reseñas asociadas.
 * @param place El objeto Place que contiene la información del lugar a mostrar
 * @param reviews La lista de objetos Review que contienen las reseñas asociadas al lugar
 */
@Composable
fun PlacePreviewCard(place: Place, reviews: List<Review>) {
    Surface(shape = MaterialTheme.shapes.large) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            PlaceInfo(p = place)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item {
                    Text(
                        text = "Reseñas de ${place.name}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
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