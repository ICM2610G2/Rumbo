package com.appnotresponding.rumbo.ui.screens.plan

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.templates.PlanTemplate

@Composable
fun PlanScreen(controller: NavHostController){
    PlanTemplate(
        user = sampleUser,
        placesList = listOf(samplePlace, samplePlace, samplePlace), // Simulamos una lista con 3 lugares
        controller = controller
    )
}