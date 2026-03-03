package com.appnotresponding.rumbo.ui.screens.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.appnotresponding.rumbo.ui.components.organisms.auth.SignUpForm
import com.appnotresponding.rumbo.ui.templates.AuthTemplate
import com.appnotresponding.rumbo.ui.theme.RumboTheme


@Composable
fun SignUpScreen(
    onNavigateBack: () -> Unit = {},
    onSignUpComplete: () -> Unit = {}
) {
    AuthTemplate {

        val scrollState = rememberScrollState()

        SignUpForm(
            onSignUpClick = { fullName, phone, email, password, country, termsAccepted ->
                // Aquí iría la validación lógica y la llamada a Firebase Auth o API.
                // Por razones de demostración, simulamos la navegación si completaron:
                onSignUpComplete()
            },
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
        )
    }
}


@Preview(showBackground = true, name = "antalla Registro demostración ", backgroundColor = 0xFF121212)
@Composable
private fun SignUpScreenPreview() {
    RumboTheme(darkTheme = true) {
        SignUpScreen()
    }
}
