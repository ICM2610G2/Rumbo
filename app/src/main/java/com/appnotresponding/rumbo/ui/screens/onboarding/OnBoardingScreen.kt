package com.appnotresponding.rumbo.ui.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.templates.OnboardingTemplate

@Composable
fun OnBoardingScreen(
    controller: NavHostController
){
    OnboardingTemplate {
        controller.navigate(AppScreens.Map.name) {
            popUpTo(AppScreens.Splash.name) { inclusive = true }
            launchSingleTop = true
        }
    }
}