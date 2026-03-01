package com.appnotresponding.rumbo.ui.components.molecules.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.components.atoms.RumboTextField
import com.appnotresponding.rumbo.ui.theme.RumboTheme


//PLAIN TEXT FIELD
@Composable
fun AuthPlainText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    RumboTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder
    )
}
/**
 * VisualTransformation que muestra dígitos crudos (ej: +573012345678)
 * con formato: +57 301 234 5678
 *
 * El valor almacenado siempre es solo +dígitos, sin espacios.
 * Los espacios se insertan visualmente después del indicativo (pos 3)
 * y luego cada 3 dígitos del número local.
 */
private class PhoneVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text // ej: "+573012345678"
        val formatted = buildFormattedPhone(raw)

        val origToTransformed = IntArray(raw.length + 1)
        var rawIndex = 0
        var fmtIndex = 0
        while (fmtIndex < formatted.length && rawIndex < raw.length) {
            if (formatted[fmtIndex] == ' ') {
                // Espacio insertado, avanzar solo en formatted
                fmtIndex++
            } else {
                origToTransformed[rawIndex] = fmtIndex
                rawIndex++
                fmtIndex++
            }
        }
        origToTransformed[raw.length] = formatted.length

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return origToTransformed[offset.coerceIn(0, raw.length)]
            }

            override fun transformedToOriginal(offset: Int): Int {
                val spacesBeforeOffset = formatted.take(offset).count { it == ' ' }
                return (offset - spacesBeforeOffset).coerceIn(0, raw.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }

    private fun buildFormattedPhone(raw: String): String {
        if (raw.isEmpty()) return ""

        val hasPlus = raw.startsWith("+")
        val digits = raw.filter { it.isDigit() }
        if (digits.isEmpty()) return if (hasPlus) "+" else ""

        val prefix = if (hasPlus) "+" else ""
        val countryCodeLen = detectCountryCodeLen(digits)
        val countryCode = digits.take(countryCodeLen)
        val local = digits.drop(countryCodeLen)

        if (local.isEmpty()) return "$prefix$countryCode"

        val parts = mutableListOf<String>()
        var i = 0
        val blockSizes = listOf(3, 3, 4)
        for (size in blockSizes) {
            if (i >= local.length) break
            parts.add(local.substring(i, minOf(i + size, local.length)))
            i += size
        }
        if (i < local.length) parts.add(local.substring(i))

        return "$prefix$countryCode ${parts.joinToString(" ")}"
    }

    private fun detectCountryCodeLen(digits: String): Int = when {
        digits.length <= 1 -> digits.length
        digits.startsWith("1") -> 1
        digits.length <= 2 -> digits.length
        digits.startsWith("7") -> 1
        digits.take(2).toIntOrNull()?.let { it in 20..69 } == true -> 2
        else -> 3
    }
}

//PHONE TEXT FIELD
/**
 * Este TextField almacena el número de teléfono en formato E.164 (ej: +573012345678)
 * pero lo muestra formateado como +57 301 234 5678.
 *
 * Solo permite ingresar dígitos y un "+" al inicio. No se permiten espacios en la entrada.
 * El formato visual se aplica automáticamente mientras se escribe.
 * @param value El número de teléfono en formato E.164 (ej: +573012345678)
 * @param onValueChange Callback que recibe el nuevo valor en formato E.164 cada vez que el usuario cambia el texto. El valor siempre es solo +dígitos, sin espacios.
 * @param label Etiqueta del campo (por defecto "Celular")
 * @param placeholder Texto de ejemplo que se muestra cuando el campo está vacío (por defecto "+57 300 123 4567")
 */
@Composable
fun AuthPhoneText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Celular",
    placeholder: String = "+57 300 123 4567"
) {
    RumboTextField(
        value = value,
        onValueChange = { newValue ->
            // Solo almacenar + y dígitos, sin espacios
            val cleaned = newValue.filter { it == '+' || it.isDigit() }
            // Limitar a máx 15 dígitos (estándar E.164) + el "+"
            val maxLen = if (cleaned.startsWith("+")) 16 else 15
            onValueChange(cleaned.take(maxLen))
        },
        label = label,
        placeholder = placeholder,
        validationRegex = Regex("^\\+\\d{10,14}$"),
        errorMessage = "Número de teléfono inválido",
        keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone,
        visualTransformation = PhoneVisualTransformation()
    )
}

//MAIL TEXT FIELD
/**
 * TextField para ingresar un correo electrónico. Valida que el formato sea correcto.
 * @param value El correo electrónico ingresado por el usuario
 * @param onValueChange Callback que recibe el nuevo valor cada vez que el usuario cambia el texto
 * @param label Etiqueta del campo (por defecto "Correo electrónico")
 * @param placeholder Texto de ejemplo que se muestra cuando el campo está vacío (por defecto "Correo electrónico")
 */
@Composable
fun AuthEmailText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Correo electrónico",
    placeholder: String = "johndoe@mail.com"
) {
    RumboTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        validationRegex = Regex("""^[A-Za-z0-9._%+\-]+@[A-Za-z0-9.\-]+\.[A-Za-z]{2,}$"""),
        errorMessage = "Correo electrónico inválido",
        keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
    )
}

//PASSWORD TEXT FIELD
/**
 * TextField para ingresar una contraseña. Valida que tenga al menos 8 caracteres, incluyendo letras mayúsculas, minúsculas, números y símbolos.
 * Incluye un ícono para mostrar u ocultar la contraseña.
 * @param value La contraseña ingresada por el usuario
 * @param onValueChange Callback que recibe el nuevo valor cada vez que el usuario cambia el texto
 * @param label Etiqueta del campo (por defecto "Contraseña")
 * @param placeholder Texto de ejemplo que se muestra cuando el campo está vacío (por defecto "*********")
 */
@Composable
fun AuthPasswordText(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Contraseña",
    placeholder: String = "*********"
) {
    var passwordVisible by remember { mutableStateOf(false) }

    RumboTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        validationRegex = Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$"""),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(
                        id = if (passwordVisible) R.drawable.ic_eye_open else R.drawable.ic_eye_crossed
                    ),
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        errorMessage = "La contraseña debe tener al menos 8 caracteres, incluyendo letras, números y símbolos",
        keyboardType = androidx.compose.ui.text.input.KeyboardType.Password,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Preview(showBackground = true, name = "Auth TextFields - Light")
@Composable
private fun AuthTextFieldsLightPreview() {
    RumboTheme(darkTheme = false) {
        var name by remember { mutableStateOf("Samu P") }
        var phone by remember { mutableStateOf("+573012345678") }
        var email by remember { mutableStateOf("sa.pico@javeriana.edu.co") }
        var password by remember { mutableStateOf("1ManzanaGrande!") }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AuthPlainText(
                value = name,
                onValueChange = { name = it },
                label = "Nombre",
                placeholder = "John Doe"
            )
            AuthPhoneText(value = phone, onValueChange = { phone = it })
            AuthEmailText(value = email, onValueChange = { email = it })
            AuthPasswordText(value = password, onValueChange = { password = it })
        }
    }
}

@Preview(showBackground = true, name = "Auth TextFields - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun AuthTextFieldsDarkPreview() {
    RumboTheme(darkTheme = true) {
        var name by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AuthPlainText(
                value = name,
                onValueChange = { name = it },
                label = "Nombre",
                placeholder = "John Doe"
            )
            AuthPhoneText(value = phone, onValueChange = { phone = it })
            AuthEmailText(value = email, onValueChange = { email = it })
            AuthPasswordText(value = password, onValueChange = { password = it })
        }
    }
}
