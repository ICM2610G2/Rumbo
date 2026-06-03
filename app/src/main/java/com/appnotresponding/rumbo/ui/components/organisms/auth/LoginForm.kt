package com.appnotresponding.rumbo.ui.components.organisms.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthEmailText
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthPasswordText
import com.appnotresponding.rumbo.ui.theme.RumboTheme

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    //https://kotlinlang.org/docs/lambdas.html#higher-order-functions
    email: String = "",
    password: String = "",
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    isLoading: Boolean = false,
    errorMessage: String? = null,
    hasBiometricCredentials: Boolean = false,
    onForgotPasswordClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onBiometricClick: () -> Unit = {},
) {
    val emailRegex = Regex("""^[A-Za-z0-9._%+\-]+@[A-Za-z0-9.\-]+\.[A-Za-z]{2,}$""")
    val passwordRegex =
        Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$""")
    val isLoginEnabled = emailRegex.matches(email) && passwordRegex.matches(password) && !isLoading

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AuthEmailText(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo",
            placeholder = "correo@gmail.com"
        )

        Spacer(modifier = Modifier.height(4.dp))

        AuthPasswordText(
            value = password,
            onValueChange = onPasswordChange,
            label = "Contraseña",
            placeholder = "********"
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage, style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.error
                ), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
            )
        }

        // Loading o botones
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            RumboButton(
                text = "Iniciar Sesión",
                onClick = onLoginClick,
                style = RumboButtonStyle.Primary,
                enabled = isLoginEnabled,
                modifier = Modifier.fillMaxWidth(),
            )

            // Solo aparece si ya hizo login antes en este dispositivo
            if (hasBiometricCredentials) {
                RumboButton(
                    text = "Iniciar con Biometricos",
                    onClick = onBiometricClick,
                    style = RumboButtonStyle.Secondary,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Text(
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .clickable(onClick = onForgotPasswordClick)
                .padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true, name = "Login Form - Dark", backgroundColor = 0xFF121212)
@Composable
private fun LoginFormPreviewDark() {
    RumboTheme(darkTheme = true) {
        LoginForm(modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true, name = "Login Form - Con biometría", backgroundColor = 0xFF121212)
@Composable
private fun LoginFormBiometricPreview() {
    RumboTheme(darkTheme = true) {
        LoginForm(
            modifier = Modifier.padding(16.dp), hasBiometricCredentials = true
        )
    }
}

@Preview(showBackground = true, name = "Login Form - Error", backgroundColor = 0xFF121212)
@Composable
private fun LoginFormErrorPreview() {
    RumboTheme(darkTheme = true) {
        LoginForm(
            modifier = Modifier.padding(16.dp), errorMessage = "Correo o contraseña incorrectos"
        )
    }
}