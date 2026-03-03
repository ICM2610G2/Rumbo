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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthEmailText
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthPasswordText
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Organismo que representa el formulario completo de inicio de sesión.
 * Incluye campos de correo, contraseña, botón de inicio de sesión y opción para recuperar la contraseña.
 */
@Composable
fun LoginForm(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onForgotPasswordClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            // Configuración del contenedor tipo "Card" oscura (según mockup)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo de Correo
        AuthEmailText(
            value = email,
            onValueChange = { email = it },
            label = "Correo",
            placeholder = "correo@gmail.com"
        )
        
        Spacer(modifier = Modifier.height(4.dp))

        // Campo de Contraseña
        AuthPasswordText(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            placeholder = "********"
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        // Botón Iniciar Sesión
        RumboButton(
            text = "Iniciar Sesión",
            onClick = { onLoginClick(email, password) },
            style = RumboButtonStyle.Primary,
            modifier = Modifier.fillMaxWidth(),
        )

        // Botón/Texto Recuperar contraseña
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