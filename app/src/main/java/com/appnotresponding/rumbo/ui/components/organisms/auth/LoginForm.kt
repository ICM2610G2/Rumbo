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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appnotresponding.rumbo.models.LoginViewModel
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthEmailText
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthPasswordText
import com.appnotresponding.rumbo.ui.theme.RumboTheme


@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    //https://kotlinlang.org/docs/lambdas.html#higher-order-functions
    onForgotPasswordClick: () -> Unit = {},
    onLoginClick: (email: String, password: String) -> Unit = { _, _ -> },
    model: LoginViewModel = viewModel()
) {
    val formData by model.loginSate.collectAsState()
    val emailRegex = Regex("""^[A-Za-z0-9._%+\-]+@[A-Za-z0-9.\-]+\.[A-Za-z]{2,}$""")
    val passwordRegex = Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$""")
    val isLoginEnabled = emailRegex.matches(formData.email) && passwordRegex.matches(formData.password)

    Column(
        modifier = modifier
            .fillMaxWidth()

            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        AuthEmailText(
            value = formData.email,
            onValueChange = { model.updateEmail(it) },
            label = "Correo",
            placeholder = "correo@gmail.com"
        )

        Spacer(modifier = Modifier.height(4.dp))


        AuthPasswordText(
            value = formData.password,
            onValueChange = { model.updatePassword(it) },
            label = "Contraseña",
            placeholder = "********"
        )

        Spacer(modifier = Modifier.height(8.dp))


        RumboButton(
            text = "Iniciar Sesión",
            onClick = {
                onLoginClick(formData.email, formData.password)
            },
            style = RumboButtonStyle.Primary,
            enabled = isLoginEnabled,
            modifier = Modifier.fillMaxWidth(),
        )


        Text(
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
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
        LoginForm(
            modifier = Modifier.padding(16.dp)
        )
    }
}
