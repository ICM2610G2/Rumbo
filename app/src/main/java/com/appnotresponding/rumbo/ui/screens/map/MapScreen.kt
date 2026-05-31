package com.appnotresponding.rumbo.ui.screens.map

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.auth
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.navigation.placesViewModel
import com.appnotresponding.rumbo.ui.templates.MapTemplate
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserLocationViewModel

@Composable
fun MapScreen(
    controller: NavHostController, placesViewModel: PlacesViewModel, locationViewModel: UserLocationViewModel
) {
    MapTemplate(
        user = sampleUser.copy(name = "Ana"), controller = controller, onProfileClick = {
            auth.signOut()
            controller.navigate(AppScreens.Splash.name) {
                popUpTo(controller.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        },
        placesViewModel = placesViewModel,
        locationViewModel = locationViewModel)
}