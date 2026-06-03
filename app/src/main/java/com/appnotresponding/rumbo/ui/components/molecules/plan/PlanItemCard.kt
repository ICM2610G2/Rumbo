package com.appnotresponding.rumbo.ui.components.molecules.plan

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonSize
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.components.atoms.RumboRatingDisplay
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel

/**
 * Tarjeta de un lugar en el planificador.
 * El fondo y borde de la card se animan al añadir/quitar del itinerario,
 * manteniendo el lenguaje visual de la app (primaryContainer cuando está activo).
 */
@Composable
fun PlanItemCard(p: Place, placesViewModel: PlacesViewModel, controller: NavHostController) {
    val uiState by placesViewModel.uiState.collectAsState()
    val isInItinerary = uiState.itinerary.any { it.id == p.id }

    val containerColor by animateColorAsState(
        targetValue = if (isInItinerary) MaterialTheme.colorScheme.secondaryContainer
                      else MaterialTheme.colorScheme.surfaceContainerLow,
        animationSpec = tween(durationMillis = 300),
        label = "planCardBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isInItinerary) MaterialTheme.colorScheme.secondary
                      else MaterialTheme.colorScheme.outlineVariant,
        animationSpec = tween(durationMillis = 300),
        label = "planCardBorder"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                        },
                    contentAlignment = Alignment.Center
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
                        }
                    )
                }
                Column(
                    modifier = Modifier.weight(6f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = p.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (p.reviews.isNotEmpty()) {
                        RumboRatingDisplay(
                            rating = p.reviews.map { it.rating }.average().toFloat(),
                            starSize = 14.dp
                        )
                    }
                    Text(
                        text = p.description ?: "Sin descripción",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            RumboButton(
                modifier = Modifier.fillMaxWidth(),
                text = if (isInItinerary) "Eliminar del Itinerario" else "Añadir al Itinerario",
                onClick = {
                    if (isInItinerary) placesViewModel.removeFromItinerary(p)
                    else placesViewModel.addToItinerary(p)
                },
                style = if (isInItinerary) RumboButtonStyle.Secondary else RumboButtonStyle.Primary,
                size = RumboButtonSize.Small,
                icon = painterResource(if (isInItinerary) R.drawable.ic_minus else R.drawable.ic_plus)
            )
        }
    }
}
