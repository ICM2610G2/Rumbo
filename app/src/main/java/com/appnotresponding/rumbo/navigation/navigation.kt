package com.appnotresponding.rumbo.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.ui.screens.auth.LogInScreen
import com.appnotresponding.rumbo.ui.screens.auth.SignUpScreen
import com.appnotresponding.rumbo.ui.screens.chat.ChatListScreen
import com.appnotresponding.rumbo.ui.screens.chat.ChatThreadScreen
import com.appnotresponding.rumbo.ui.screens.friends.FriendsScreen
import com.appnotresponding.rumbo.ui.screens.itinerary.ItineraryScreen
import com.appnotresponding.rumbo.ui.screens.map.MapScreen
import com.appnotresponding.rumbo.ui.screens.onboarding.OnBoardingScreen
import com.appnotresponding.rumbo.ui.screens.plan.PlanScreen
import com.appnotresponding.rumbo.ui.screens.splash.SplashScreen
import com.appnotresponding.rumbo.ui.viewModel.ChatThreadViewModel
import com.appnotresponding.rumbo.ui.viewModel.ChatViewModel
import com.appnotresponding.rumbo.ui.viewModel.FriendsViewModel
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserLocationViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserViewModel

val placesViewModel: PlacesViewModel = PlacesViewModel()
val chatViewModel: ChatViewModel = ChatViewModel()
val chatThreadViewModel: ChatThreadViewModel = ChatThreadViewModel()
val friendsViewModel: FriendsViewModel = FriendsViewModel()

enum class AppScreens {
    Splash,
    LogIn,
    SignUp,
    Map,
    Chat,
    ChatThread,
    Plan,
    Itinerary,
    OnBoarding,
    Friends
}

@Composable
fun Navigation(
    locationViewModel: UserLocationViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.Splash.name) {
        composable(route = AppScreens.Splash.name) {
            SplashScreen(navController)
        }
        composable(route = AppScreens.LogIn.name) {
            LogInScreen(navController)
        }
        composable(route = AppScreens.SignUp.name) {
            SignUpScreen(navController)
        }
        composable(route = AppScreens.Map.name) {
            MapScreen(navController, placesViewModel, locationViewModel, userViewModel, friendsViewModel)
        }
        composable(route = AppScreens.Chat.name) {
            ChatListScreen(navController, userViewModel, chatViewModel, placesViewModel)
        }
        composable(route = AppScreens.ChatThread.name) {
            ChatThreadScreen(navController, chatViewModel, chatThreadViewModel, userViewModel, locationViewModel, placesViewModel)
        }
        composable(route = AppScreens.Plan.name) {
            PlanScreen(navController, placesViewModel, locationViewModel, userViewModel)
        }
        composable(route = AppScreens.Itinerary.name) {
            ItineraryScreen(navController, placesViewModel, userViewModel)
        }
        composable(route = AppScreens.OnBoarding.name) {
            OnBoardingScreen(navController)
        }
        composable(route = AppScreens.Friends.name) {
            FriendsScreen(navController, userViewModel, friendsViewModel, chatViewModel)
        }
    }
}
