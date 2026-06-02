package com.appnotresponding.rumbo.ui.screens.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.templates.MapTemplate
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserLocationViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserViewModel

@Composable
fun MapScreen(
    controller: NavHostController,
    placesViewModel: PlacesViewModel,
    locationViewModel: UserLocationViewModel,
    userViewModel: UserViewModel
) {
    val userState by userViewModel.currentUserState.collectAsState()
    val user = userState ?: sampleUser.copy(name = "Cargando...")

    MapTemplate(
        user = user, controller = controller, onProfileClick = {
            controller.navigate(AppScreens.Profile.name)
        }, placesViewModel = placesViewModel, locationViewModel = locationViewModel
    )
}
