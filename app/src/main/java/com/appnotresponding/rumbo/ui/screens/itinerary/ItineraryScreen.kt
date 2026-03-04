package com.appnotresponding.rumbo.ui.screens.itinerary

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.templates.ItineraryTemplate

@Composable
fun ItineraryScreen(controller: NavHostController){
    ItineraryTemplate(
        user = sampleUser,
        itineraryList = listOf(samplePlace, samplePlace, samplePlace),
        controller = controller
    )
}