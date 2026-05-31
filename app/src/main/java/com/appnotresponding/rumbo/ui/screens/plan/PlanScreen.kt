package com.appnotresponding.rumbo.ui.screens.plan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.auth
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.templates.PlanTemplate
import com.appnotresponding.rumbo.ui.utils.searchNearbyPlaces
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserLocationViewModel

@Composable
fun PlanScreen(controller: NavHostController, placesViewModel: PlacesViewModel, locationViewModel: UserLocationViewModel) {
    val context = LocalContext.current

    val userLocationState by locationViewModel.uiState.collectAsState()
    val placesState by placesViewModel.uiState.collectAsState()

    LaunchedEffect(
        userLocationState.latitude,
        userLocationState.longitude
    ) {

        val latitude = userLocationState.latitude
        val longitude = userLocationState.longitude

        // Evitar llamadas con coordenadas inválidas
        if (latitude != 0.0 && longitude != 0.0) {

            searchNearbyPlaces(
                latitude = latitude,
                longitude = longitude,

                onPlacesReceived = { places ->

                    placesViewModel.updatePlaces(
                        places
                    )
                },

                onError = { error ->

                    println("ERROR PLACES: $error")
                },

                context = context
            )
        }
    }

    PlanTemplate(
        user = sampleUser.copy(name = "Ana"),
        placesList = placesState.availablePlaces,
        controller = controller, onProfileClick = {
            auth.signOut()
            controller.navigate(AppScreens.Splash.name) {
                popUpTo(controller.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }, placesViewModel)
}