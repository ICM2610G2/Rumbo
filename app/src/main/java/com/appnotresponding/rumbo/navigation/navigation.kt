package com.appnotresponding.rumbo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

enum class AppScreens{
    Home
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController=navController, startDestination = AppScreens.Home.name){

    }
}