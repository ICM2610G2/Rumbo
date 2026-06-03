package com.appnotresponding.rumbo.ui.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.ui.components.molecules.common.LocationHeader
import com.appnotresponding.rumbo.ui.components.organisms.common.MainTopBar
import com.appnotresponding.rumbo.ui.components.organisms.common.Nav
import com.appnotresponding.rumbo.ui.components.organisms.plan.PlanPOIList
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.appnotresponding.rumbo.ui.components.atoms.RumboTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.fillMaxWidth
import com.appnotresponding.rumbo.R

/**
 *
 *
 * Plantilla principal de la pantalla de planificacion.
 * con la barra superior, la barra de navegacion inferior, el encabezado de ubicacion
 * y la lista de lugares disponibles.
 *
 * @param user El usuario actual, utilizado para mostrar su información en la barra superior.
 * @param placesList La lista de lugares (Place) que se mostraran en el planificador.
 */

@Composable
fun PlanTemplate(
    user: User,
    placesList: List<Place>,
    controller: NavHostController,
    onProfileClick: () -> Unit = {},
    placesViewModel: PlacesViewModel
) {
    val placesState by placesViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = { MainTopBar(u = user, onProfileClick = onProfileClick) },
        bottomBar = { Nav(controller) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            LocationHeader(title = "Planea Tu Día", locationName = "Bogotá")

            RumboTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                value = placesState.searchQuery,
                onValueChange = { query ->
                    placesViewModel.onSearchQueryChanged(query, context)
                },
                placeholder = "Buscar atractivos turísticos...",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Icono de búsqueda",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PlanPOIList(places = placesList, placesViewModel, controller)
        }
    }
}

/**
@Preview(showBackground = true, name = "PlanTemplate - Light")
@Composable
private fun PlanTemplateLightPreview() {
RumboTheme(darkTheme = false) {
PlanTemplate(
user = sampleUser,
placesList = listOf(samplePlace, samplePlace, samplePlace),
controller = rememberNavController()
)
}
}

@Preview(showBackground = true, name = "PlanTemplate - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun PlanTemplateDarkPreview() {
RumboTheme(darkTheme = true) {
PlanTemplate(
user = sampleUser,
placesList = listOf(samplePlace, samplePlace, samplePlace),
controller = rememberNavController()
)
}
}
 */