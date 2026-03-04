package com.appnotresponding.rumbo.ui.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.components.molecules.common.DayHeader
import com.appnotresponding.rumbo.ui.components.organisms.common.MainTopBar
import com.appnotresponding.rumbo.ui.components.organisms.common.Nav
import com.appnotresponding.rumbo.ui.components.organisms.itinerary.ItineraryOverview
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 *
 *
 * Plantilla principal de la pantalla de itinerario.
 * Integra la barra superior, la navegacion inferior, el encabezado con la fecha actual
 * y la lista de lugares que el usuario ha seleccionado para viaje.
 *
 * @param user El usuario actual, utilizado para el encabezado de la aplicacion.
 * @param itineraryList La lista de lugares (Place) que conforman el itinerario actual.
 */

@Composable
fun ItineraryTemplate(user: User, itineraryList: List<Place>) {
    Scaffold(
        topBar = { MainTopBar(u = user) },
        bottomBar = { Nav() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DayHeader(title = "Así Se Ve Tu Día")
            Spacer(modifier = Modifier.height(16.dp))

            ItineraryOverview(itineraryList = itineraryList)
        }
    }
}

@Preview(showBackground = true, name = "ItineraryTemplate - Light")
@Composable
private fun ItineraryTemplateLightPreview() {
    RumboTheme(darkTheme = false) {
        ItineraryTemplate(
            user = sampleUser,
            itineraryList = listOf(samplePlace, samplePlace, samplePlace)
        )
    }
}

@Preview(showBackground = true, name = "ItineraryTemplate - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun ItineraryTemplateDarkPreview() {
    RumboTheme(darkTheme = true) {
        ItineraryTemplate(
            user = sampleUser,
            itineraryList = listOf(samplePlace, samplePlace, samplePlace)
        )
    }
}