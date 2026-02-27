package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Componente RumboTextField para campos de texto con validación y estilos personalizados.
 * @param value El valor actual del campo de texto
 * @param onValueChange Función a ejecutar cuando el valor del campo de texto cambia
 * @param modifier Modificador para personalizar la apariencia y comportamiento del campo de texto
 * @param placeholder Texto de marcador de posición que se muestra cuando el campo está vacío
 * @param label Etiqueta que se muestra encima del campo de texto (opcional)
 * @param enabled Indica si el campo de texto está habilitado o deshabilitado
 * @param readOnly Indica si el campo de texto es solo lectura o editable
 * @param singleLine Indica si el campo de texto es de una sola línea o multilinea
 * @param maxLines Número máximo de líneas a mostrar (solo relevante si singleLine es false)
 * @param validationRegex Expresión regular para validar el formato del texto (opcional)
 * @param errorMessage Mensaje de error personalizado a mostrar cuando la validación falla (opcional)
 * @param keyboardType Tipo de teclado a mostrar (por ejemplo, Email, Number, etc.)
 * @param visualTransformation Transformación visual para ocultar o formatear el texto (por ejemplo, para contraseñas)
 * @param leadingIcon Icono opcional para mostrar al inicio del campo de texto
 * @param trailingIcon Icono opcional para mostrar al final del campo de texto
 */
@Composable
fun RumboTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    label: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    validationRegex: Regex? = null,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val isValidationError =
        validationRegex != null && value.isNotEmpty() && !validationRegex.matches(value)
    val isError = isValidationError || errorMessage != null

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        maxLines = maxLines,
        label = label?.let { { Text(text = it, style = MaterialTheme.typography.bodyMedium) } },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        },
        isError = isError,
        supportingText = if (isError) {
            {
                val msg = errorMessage ?: "Formato inválido"
                Text(
                    text = msg,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        ),
        textStyle = MaterialTheme.typography.bodyMedium
    )
}


@Preview(showBackground = true, name = "TextField - Light")
@Composable
private fun TextFieldLightPreview() {
    RumboTheme(darkTheme = false) {
        var text by remember { mutableStateOf("") }
        var emailText by remember { mutableStateOf("bad-email") }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RumboTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = "Escribe aquí...",
                label = "Nombre"
            )
            RumboTextField(
                value = emailText,
                onValueChange = { emailText = it },
                placeholder = "correo@ejemplo.com",
                label = "Email",
                validationRegex = Regex("^[\\w.-]+@[\\w.-]+\\.\\w+$"),
                keyboardType = KeyboardType.Email,
                trailingIcon = { Icon(painter = painterResource(id = R.drawable.ic_eye_open), contentDescription = "Eye Icon", tint = MaterialTheme.colorScheme.onSurface) }
            )
            RumboTextField(
                value = "",
                onValueChange = {},
                placeholder = "Deshabilitado",
                enabled = false
            )
            RumboTextField(
                value = "Error externo",
                onValueChange = {},
                errorMessage = "Este campo es requerido"
            )
        }
    }
}

@Preview(showBackground = true, name = "TextField - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun TextFieldDarkPreview() {
    RumboTheme(darkTheme = true) {
        var text by remember { mutableStateOf("") }
        var emailText by remember { mutableStateOf("bad-email") }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RumboTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = "Escribe aquí...",
                label = "Nombre"
            )
            RumboTextField(
                value = emailText,
                onValueChange = { emailText = it },
                placeholder = "correo@ejemplo.com",
                label = "Email",
                validationRegex = Regex("^[\\w.-]+@[\\w.-]+\\.\\w+$"),
                keyboardType = KeyboardType.Email,
                trailingIcon = { Icon(painter = painterResource(id = R.drawable.ic_eye_open), contentDescription = "Eye Icon", tint = MaterialTheme.colorScheme.onSurface) }
            )
            RumboTextField(
                value = "",
                onValueChange = {},
                placeholder = "Deshabilitado",
                enabled = false
            )
            RumboTextField(
                value = "Error externo",
                onValueChange = {},
                errorMessage = "Este campo es requerido"
            )
        }
    }
}