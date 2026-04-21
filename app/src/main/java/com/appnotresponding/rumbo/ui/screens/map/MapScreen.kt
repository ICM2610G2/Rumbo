package com.appnotresponding.rumbo.ui.screens.map

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.auth
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.templates.MapTemplate

@Composable
fun MapScreen(
    controller: NavHostController
){
    MapTemplate(
        user = sampleUser.copy(name = "Ana"),
        controller = controller,
        onProfileClick = {
            auth.signOut()
            controller.navigate(AppScreens.Splash.name) {
                popUpTo(controller.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    )
}