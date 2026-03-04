package com.appnotresponding.rumbo.ui.components.organisms.plan

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
import com.appnotresponding.rumbo.ui.components.molecules.plan.PlanItemCard
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 *
 *
 * Organismo que representa la lista de puntos de interes (POI) en el planificador.
 *
 *
 * @param places La lista de lugares (Place) que se van a mostrar en la pantalla.
 */

@Composable
fun PlanPOIList(places: List<Place>) {

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(places) { place ->
            PlanItemCard(p = place)
        }
    }
}

@Preview(showBackground = true, name = "PlanPOIList - Light")
@Composable
private fun PlanPOIListLightPreview() {
    RumboTheme(darkTheme = false) {
        PlanPOIList(
            places = listOf(samplePlace, samplePlace, samplePlace)
        )
    }
}

@Preview(showBackground = true, name = "PlanPOIList - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun PlanPOIListDarkPreview() {
    RumboTheme(darkTheme = true) {
        PlanPOIList(
            places = listOf(samplePlace, samplePlace, samplePlace)
        )
    }
}