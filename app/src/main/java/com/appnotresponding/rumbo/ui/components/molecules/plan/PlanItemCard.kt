package com.appnotresponding.rumbo.ui.components.molecules.plan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.components.atoms.RumboRatingDisplay
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel

/**
 * PlanItemCard.kt
 *
 * Componente que representa una tarjeta de un lugar en el planificador.
 * Muestra la imagen, nombre, descripción, precio y un botón para añadir al itinerario.
 *
 * @param p El lugar a mostrar en la tarjeta.
 */
@Composable
fun PlanItemCard(p: Place, placesViewModel: PlacesViewModel, controller: NavHostController) {

    val uiState by placesViewModel.uiState.collectAsState()
    val isInItinerary = uiState.itinerary.any { it.id == p.id }
    val icon = if (isInItinerary) R.drawable.ic_minus else R.drawable.ic_plus
    val msg = if (isInItinerary) "Eliminar del Itinerario" else "Añadir al Itinerario"

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ), elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        ), shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(4f)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable {
                            placesViewModel.showPreview(p)
                            placesViewModel.selectForNavigation(p)
                            controller.navigate(AppScreens.Map.name)
                        }, contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = p.imageUrl,
                        contentDescription = "Imagen de ${p.name}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = {
                            Image(
                                painter = painterResource(R.drawable.ic_picture),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                                contentScale = ContentScale.Crop
                            )
                        })
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(6f), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = p.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (p.reviews.isNotEmpty()) {
                        RumboRatingDisplay(
                            rating = p.reviews.map { it.rating }.average().toFloat(),
                            starSize = 14.dp
                        )
                    }
                    Text(
                        text = p.description ?: "No hay descripción",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }
            }
        }
        RumboButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            text = msg, onClick = {
                if (isInItinerary) {
                    placesViewModel.removeFromItinerary(p)
                } else {
                    placesViewModel.addToItinerary(p)
                }
            }, style = RumboButtonStyle.Secondary, icon = painterResource(icon)
        )
    }
}

/**
@Preview(showBackground = true, name = "PlanItemCard - Light")
@Composable
private fun PlanItemCardLightPreview() {
RumboTheme(darkTheme = false) {
PlanItemCard(p = samplePlace)
}
}

@Preview(showBackground = true, name = "PlanItemCard - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun PlanItemCardDarkPreview() {
RumboTheme(darkTheme = true) {
PlanItemCard(p = samplePlace)
}
}
 */