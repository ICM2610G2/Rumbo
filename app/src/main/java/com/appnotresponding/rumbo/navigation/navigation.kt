package com.appnotresponding.rumbo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.ui.screens.auth.LoginScreen

enum class AppScreens{
    LogIn,
    SignUp,
    Map,
    Chat,
    ChatThread,
    Plan,
    Itinerary
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController=navController, startDestination = AppScreens.LogIn.name){
        composable(route = AppScreens.LogIn.name){
            LoginScreen(navController)
        }
    }
}