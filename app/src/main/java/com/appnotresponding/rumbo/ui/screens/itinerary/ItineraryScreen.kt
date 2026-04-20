package com.appnotresponding.rumbo.ui.screens.itinerary

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.auth
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.templates.ItineraryTemplate

@Composable
fun ItineraryScreen(controller: NavHostController){
    ItineraryTemplate(
        user = sampleUser,
        itineraryList = listOf(samplePlace, samplePlace, samplePlace),
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