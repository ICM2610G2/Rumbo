package com.appnotresponding.rumbo.ui.screens.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.auth
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.templates.ProfileTemplate
import com.appnotresponding.rumbo.ui.utils.MyApp.Companion.fcmToken
import com.appnotresponding.rumbo.ui.viewModel.ProfileViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.messaging

@Composable
fun ProfileScreen(
    controller: NavHostController,
    userViewModel: UserViewModel,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val userState by userViewModel.currentUserState.collectAsState()
    val profileState by profileViewModel.uiState.collectAsState()
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedPhotoUri = uri
    }

    val user = userState
    if (user == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    LaunchedEffect(user.id) {
        profileViewModel.loadItineraryHistory(user.id)
        profileViewModel.loadUserDropNotes(user.id)
    }

    ProfileTemplate(
        user = user,
        itineraryHistory = profileState.itineraryHistory,
        dropNotes = profileState.userDropNotes,
        selectedPhotoUri = selectedPhotoUri,
        isSavingProfile = profileState.isSavingProfile,
        profileError = profileState.profileError,
        profileSuccess = profileState.profileSuccess,
        controller = controller,
        onBackClick = { controller.popBackStack() },
        onPickPhoto = { imagePicker.launch("image/*") },
        onSaveProfile = { name, lastname, phone ->
            profileViewModel.updateProfile(
                user = user,
                name = name,
                lastname = lastname,
                phone = phone,
                photoUri = selectedPhotoUri,
                onSuccess = { selectedPhotoUri = null })
        },
        onSignOut = {
            Firebase.messaging.token.addOnSuccessListener { token ->
                fcmToken = token
                FirebaseDatabase.getInstance().getReference("tokens/" + auth.currentUser!!.uid)
                    .setValue(null).addOnSuccessListener {
                        Log.i("FirebaseApp", "Token guardado correctamente")
                        auth.signOut()
                    }.addOnFailureListener { e ->
                        Log.e("FirebaseApp", "Error guardando token: ${e.message}")
                        auth.signOut()
                    }
            }
            controller.navigate(AppScreens.Splash.name) {
                popUpTo(controller.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        })
}
