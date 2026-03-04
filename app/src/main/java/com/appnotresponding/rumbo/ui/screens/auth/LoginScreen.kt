package com.appnotresponding.rumbo.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.appnotresponding.rumbo.ui.components.organisms.auth.LoginForm
import com.appnotresponding.rumbo.ui.templates.AuthTemplate
import com.appnotresponding.rumbo.ui.theme.RumboTheme


@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {}
) {
    AuthTemplate {

        LoginForm(
            onLoginClick = { email, password ->

                onNavigateToHome()
            },
            onForgotPasswordClick = {
                onNavigateToForgotPassword()
            },
            modifier = Modifier
        )
    }
}


@Preview(showBackground = true, name = "Pantalla Login demostración ", backgroundColor = 0xFF121212)
@Composable
private fun LoginScreenPreview() {
    RumboTheme(darkTheme = true) {
        LoginScreen()
    }
}
