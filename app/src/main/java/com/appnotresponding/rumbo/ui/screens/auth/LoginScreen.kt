package com.appnotresponding.rumbo.ui.screens.auth

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.auth
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.organisms.auth.LoginForm
import com.appnotresponding.rumbo.ui.templates.AuthTemplate
import com.appnotresponding.rumbo.ui.theme.RumboTheme


@Composable
fun LogInScreen(
    controller: NavHostController
) {
    LaunchedEffect(Unit) {
        auth.currentUser?.let {
            controller.navigate(AppScreens.Map.name) {
                popUpTo(AppScreens.Splash.name) { inclusive = true }
                launchSingleTop = true
            }
        }

    }
    AuthTemplate {
        LoginForm(
            modifier = Modifier,
            onLoginClick = { email, password ->
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        controller.navigate(AppScreens.Map.name) {
                            popUpTo(AppScreens.Splash.name) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        Log.e("LogInScreen", "Login fallido", it.exception)
                    }
                }
            },
        )
    }
}


@Preview(showBackground = true, name = "Pantalla Login demostración ", backgroundColor = 0xFF121212)
@Composable
private fun LoginScreenPreview() {
    RumboTheme(darkTheme = true) {
        LogInScreen(rememberNavController())
    }
}
