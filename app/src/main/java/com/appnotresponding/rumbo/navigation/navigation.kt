package com.appnotresponding.rumbo.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.appnotresponding.rumbo.ui.screens.profile.ProfileScreen
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
    Friends,
    Profile
}

@Composable
fun Navigation(
    openChat: Boolean = false,
    senderId: String? = null,
    chatId: String? = null,
    senderName: String? = null,
    senderPhotoUrl: String? = null,
    isOnline: Boolean = false,
    locationViewModel: UserLocationViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
) {
    val navController = rememberNavController()

    LaunchedEffect(openChat, chatId) {

        if (
            openChat &&
            !chatId.isNullOrBlank() &&
            !senderName.isNullOrBlank()
        ) {

            chatViewModel.selectDirectChat(
                chatId = chatId,
                chatTitle = senderName,
                photoUrl = senderPhotoUrl ?: "",
                isOnline = isOnline
            )

            navController.navigate(AppScreens.ChatThread.name)
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash.name,
        enterTransition = { fadeIn(tween(280)) },
        exitTransition = { fadeOut(tween(180)) },
        popEnterTransition = { fadeIn(tween(280)) },
        popExitTransition = { fadeOut(tween(180)) }
    ) {
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
        composable(route = AppScreens.Profile.name) {
            ProfileScreen(navController, userViewModel)
        }
    }
}
