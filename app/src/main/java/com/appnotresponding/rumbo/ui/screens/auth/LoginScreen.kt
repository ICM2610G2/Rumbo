package com.appnotresponding.rumbo.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.organisms.auth.LoginForm
import com.appnotresponding.rumbo.ui.templates.AuthTemplate
import com.appnotresponding.rumbo.ui.theme.RumboTheme


@Composable
fun LoginScreen(
    controller: NavHostController
) {
    AuthTemplate {
        LoginForm(
            modifier = Modifier,
            onLoginClick = { controller.navigate(AppScreens.OnBoarding.name) },
        )
    }
}


@Preview(showBackground = true, name = "Pantalla Login demostración ", backgroundColor = 0xFF121212)
@Composable
private fun LoginScreenPreview() {
    RumboTheme(darkTheme = true) {
        LoginScreen(controller = rememberNavController())
    }
}
