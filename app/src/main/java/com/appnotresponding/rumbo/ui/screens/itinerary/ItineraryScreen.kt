package com.appnotresponding.rumbo.ui.screens.itinerary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.templates.ItineraryTemplate
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel

@Composable
fun ItineraryScreen(controller: NavHostController, placesViewModel: PlacesViewModel) {
    val state by placesViewModel.uiState.collectAsState()
    ItineraryTemplate(
        user = sampleUser.copy(name = "Ana"),
        itineraryList = state.itinerary,
        controller = controller,
        onProfileClick = {
            auth.signOut()
            controller.navigate(AppScreens.Splash.name) {
                popUpTo(controller.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }, placesViewModel)
}