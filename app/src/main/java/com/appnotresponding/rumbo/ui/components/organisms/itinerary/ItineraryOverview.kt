package com.appnotresponding.rumbo.ui.components.organisms.itinerary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.ui.components.molecules.itinerary.ItineraryItemCard
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 *
 *
 * Organismo que representa la vista general del itinerario del usuario.
 * Utiliza un LazyColumn para mostrar la lista de lugares programados de manera desplazable.
 *
 * @param itineraryList La lista de lugares (Place) agregados al itinerario del dia.
 */

@Composable
fun ItineraryOverview(itineraryList: List<Place>) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(itineraryList) { place ->
            ItineraryItemCard(p = place)
        }
    }
}

@Preview(showBackground = true, name = "ItineraryOverview - Light")
@Composable
private fun ItineraryOverviewLightPreview() {
    RumboTheme(darkTheme = false) {
        ItineraryOverview(
            itineraryList = listOf(samplePlace, samplePlace, samplePlace)
        )
    }
}

@Preview(showBackground = true, name = "ItineraryOverview - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun ItineraryOverviewDarkPreview() {
    RumboTheme(darkTheme = true) {
        ItineraryOverview(
            itineraryList = listOf(samplePlace, samplePlace, samplePlace)
        )
    }
}