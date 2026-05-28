package com.appnotresponding.rumbo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.ui.screens.auth.LogInScreen
import com.appnotresponding.rumbo.ui.screens.auth.SignUpScreen
import com.appnotresponding.rumbo.ui.screens.chat.ChatListScreen
import com.appnotresponding.rumbo.ui.screens.chat.ChatThreadScreen
import com.appnotresponding.rumbo.ui.screens.itinerary.ItineraryScreen
import com.appnotresponding.rumbo.ui.screens.map.MapScreen
import com.appnotresponding.rumbo.ui.screens.onboarding.OnBoardingScreen
import com.appnotresponding.rumbo.ui.screens.plan.PlanScreen
import com.appnotresponding.rumbo.ui.screens.splash.SplashScreen
import com.appnotresponding.rumbo.ui.utils.loadPlaces
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import androidx.lifecycle.ViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserLocationViewModel

val placesViewModel: PlacesViewModel = PlacesViewModel()

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
fun Navigation(locationViewModel: UserLocationViewModel = viewModel()){
    val context = LocalContext.current
    var lista = loadPlaces(context)
    placesViewModel.updatePlaces(lista)
    val navController = rememberNavController()
    NavHost(navController=navController, startDestination = AppScreens.Splash.name){
        composable (route = AppScreens.Splash.name){
            SplashScreen(navController)
        }
        composable(route = AppScreens.LogIn.name){
            LogInScreen(navController)
        }
        composable (route = AppScreens.SignUp.name){
            SignUpScreen(navController)
        }
        composable (route = AppScreens.Map.name) {
            MapScreen(navController, placesViewModel, locationViewModel)
        }
        composable (route = AppScreens.Chat.name) {
            ChatListScreen(navController)
        }
        composable(route = AppScreens.ChatThread.name){
            ChatThreadScreen(navController)
        }
        composable(route = AppScreens.Plan.name){
            PlanScreen(navController, placesViewModel)
        }
        composable(route = AppScreens.Itinerary.name){
            ItineraryScreen(navController, placesViewModel)
        }
        composable(route = AppScreens.OnBoarding.name){
            OnBoardingScreen(navController)
        }
    }
}