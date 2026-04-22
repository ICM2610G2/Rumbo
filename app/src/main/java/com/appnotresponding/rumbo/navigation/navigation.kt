package com.appnotresponding.rumbo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.ui.components.organisms.chat.ChatPreviewData
import com.appnotresponding.rumbo.ui.components.organisms.chat.ChatThread
import com.appnotresponding.rumbo.ui.screens.auth.LoginScreen
import com.appnotresponding.rumbo.ui.screens.auth.SignUpScreen
import com.appnotresponding.rumbo.ui.screens.chat.ChatListScreen
import com.appnotresponding.rumbo.ui.screens.chat.ChatThreadScreen
import com.appnotresponding.rumbo.ui.screens.itinerary.ItineraryScreen
import com.appnotresponding.rumbo.ui.screens.map.MapScreen
import com.appnotresponding.rumbo.ui.screens.onboarding.OnBoardingScreen
import com.appnotresponding.rumbo.ui.screens.plan.PlanScreen
import com.appnotresponding.rumbo.ui.screens.splash.SplashScreen

enum class AppScreens{
    Splash,
    LogIn,
    SignUp,
    Map,
    Chat,
    ChatThread,
    Plan,
    Itinerary,
    OnBoarding
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController=navController, startDestination = AppScreens.Splash.name){
        composable (route = AppScreens.Splash.name){
            SplashScreen(navController)
        }
        composable(route = AppScreens.LogIn.name){
            LoginScreen(navController)
        }
        composable (route = AppScreens.SignUp.name){
            SignUpScreen(navController)
        }
        composable (route = AppScreens.Map.name) {
            MapScreen(navController)
        }
        composable (route = AppScreens.Chat.name) {
            ChatListScreen(navController)
        }
        composable(route = AppScreens.ChatThread.name){
            ChatThreadScreen(navController)
        }
        composable(route = AppScreens.Plan.name){
            PlanScreen(navController)
        }
        composable(route = AppScreens.Itinerary.name){
            ItineraryScreen(navController)
        }
        composable(route = AppScreens.OnBoarding.name){
            OnBoardingScreen(navController)
        }
    }
}