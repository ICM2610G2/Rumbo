package com.appnotresponding.rumbo.ui.screens.plan

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.templates.PlanTemplate
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel

@Composable
fun PlanScreen(controller: NavHostController, placesViewModel: PlacesViewModel) {
    val state by placesViewModel.uiState.collectAsState()
    PlanTemplate(
        user = sampleUser.copy(name = "Ana"),
        placesList = state.availablePlaces,
        controller = controller, onProfileClick = {
            auth.signOut()
            controller.navigate(AppScreens.Splash.name) {
                popUpTo(controller.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }, placesViewModel)
}