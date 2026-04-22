package com.appnotresponding.rumbo.ui.screens.auth

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.auth
import com.appnotresponding.rumbo.models.AuthResult
import com.appnotresponding.rumbo.models.LoginViewModel
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.organisms.auth.LoginForm
import com.appnotresponding.rumbo.ui.templates.AuthTemplate
import com.appnotresponding.rumbo.ui.theme.RumboTheme

@Composable
fun LogInScreen(
    controller: NavHostController, viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val state by viewModel.loginState.collectAsState()

    // Inicializar prefs con contexto (solo la primera vez)
    LaunchedEffect(Unit) {
        viewModel.initPrefs(context)

        // Si ya hay sesión activa, saltar directo al mapa
        auth.currentUser?.let {
            controller.navigate(AppScreens.Map.name) {
                popUpTo(AppScreens.Splash.name) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Navegar cuando el login sea exitoso
    LaunchedEffect(state.authResult) {
        if (state.authResult is AuthResult.Success) {
            controller.navigate(AppScreens.Map.name) {
                popUpTo(AppScreens.Splash.name) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    AuthTemplate {
        LoginForm(
            modifier = Modifier,
            email = state.email,
            password = state.password,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            isLoading = state.authResult is AuthResult.Loading,
            errorMessage = (state.authResult as? AuthResult.Error)?.message,
            hasBiometricCredentials = state.hasBiometricCredentials,
            onLoginClick = { viewModel.loginWithEmailPassword() },
            onBiometricClick = { viewModel.loginWithBiometric(activity) },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun LoginScreenPreview() {
    RumboTheme(darkTheme = true) {
        LogInScreen(rememberNavController())
    }
}