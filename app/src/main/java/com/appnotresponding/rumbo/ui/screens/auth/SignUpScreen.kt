package com.appnotresponding.rumbo.ui.screens.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.organisms.auth.SignUpForm
import com.appnotresponding.rumbo.ui.templates.AuthTemplate
import com.appnotresponding.rumbo.ui.theme.RumboTheme


@Composable
fun SignUpScreen(
    controller: NavController
) {
    AuthTemplate {

        val scrollState = rememberScrollState()

        SignUpForm(
            onClick = { controller.navigate(AppScreens.OnBoarding.name) },
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
        )
    }
}


@Preview(
    showBackground = true, name = "antalla Registro demostración ", backgroundColor = 0xFF121212
)
@Composable
private fun SignUpScreenPreview() {
    RumboTheme(darkTheme = true) {
        SignUpScreen(controller = rememberNavController())
    }
}
