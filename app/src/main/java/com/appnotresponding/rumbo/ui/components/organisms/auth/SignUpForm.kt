package com.appnotresponding.rumbo.ui.components.organisms.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.components.atoms.RumboTextField
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthEmailText
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthPasswordText
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthPhoneText
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthPlainText
import com.appnotresponding.rumbo.ui.theme.RumboTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpForm(
    onSignUpClick: (String, String, String, String, String, Boolean) -> Unit = { _, _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    var expanded by remember { mutableStateOf(false) }
    val countries = listOf("Colombia", "México", "Argentina", "España", "Perú", "Chile")
    var selectedCountry by remember { mutableStateOf(countries[0]) }

 
    var termsAccepted by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo: Nombre Completo
        AuthPlainText(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Nombre Completo",
            placeholder = "Nombres Apellidos"
        )

        // Campo: Celular
        AuthPhoneText(
            value = phone,
            onValueChange = { phone = it },
            label = "Celular",
            placeholder = "+57 312 345 6789"
        )

        // Campo: Correo
        AuthEmailText(
            value = email,
            onValueChange = { email = it },
            label = "Correo",
            placeholder = "correo@gmail.com"
        )

        // Campo: Contraseña
        AuthPasswordText(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            placeholder = "********"
        )

        // Campo: País de Residencia (Dropdown)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
         
            RumboTextField(
                value = selectedCountry,
                onValueChange = {},
                readOnly = true,
                label = "País de Residencia",
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                countries.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption, color = MaterialTheme.colorScheme.onSurface) },
                        onClick = {
                            selectedCountry = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }

        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary, // El verde de la app
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.size(20.dp)
            )
            
        
            val termsText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                    append("He leído y acepto los términos y condiciones de uso.\n")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Términos y condiciones.")
                }
            }
            Text(
                text = termsText,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón: Registrarse
        RumboButton(
            text = "Registrarse",
            onClick = {
                onSignUpClick(fullName, phone, email, password, selectedCountry, termsAccepted)
            },
            style = RumboButtonStyle.Primary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "Sign Up Form - Dark", backgroundColor = 0xFF121212)
@Composable
private fun SignUpFormPreviewDark() {
    RumboTheme(darkTheme = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            SignUpForm()
        }
    }
}
