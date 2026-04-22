package com.appnotresponding.rumbo.ui.screens.plan

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.auth
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.templates.PlanTemplate

@Composable
fun PlanScreen(controller: NavHostController) {
    PlanTemplate(
        user = sampleUser.copy(name = "Ana"), placesList = listOf(
            samplePlace, samplePlace, samplePlace
        ), // Simulamos una lista con 3 lugares
        controller = controller, onProfileClick = {
            auth.signOut()
            controller.navigate(AppScreens.Splash.name) {
                popUpTo(controller.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        })
}