package com.appnotresponding.rumbo.ui.screens.itinerary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.templates.ItineraryTemplate
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserViewModel

@Composable
fun ItineraryScreen(
    controller: NavHostController, placesViewModel: PlacesViewModel, userViewModel: UserViewModel
) {
    val state by placesViewModel.uiState.collectAsState()
    val userState by userViewModel.currentUserState.collectAsState()
    val user = userState ?: sampleUser.copy(name = "Cargando...")

    ItineraryTemplate(
        user = user, itineraryList = state.itinerary, controller = controller, placesViewModel = placesViewModel
    )
}
