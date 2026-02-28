package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme

// Definición de tipos de botón
enum class RumboButtonStyle {
    Primary,
    Secondary,
    Tertiary
}

//Definición de estilos y tamaños para el botón, con valores de padding e iconos asociados
enum class RumboButtonSize(
    val height: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val iconSize: Dp
) {
    Small(32.dp, 12.dp, 6.dp, 16.dp),
    Medium(44.dp, 20.dp, 10.dp, 18.dp),
    Large(52.dp, 28.dp, 14.dp, 20.dp)
}


/**
 * Componente de botón personalizable para la aplicación Rumbo.
 *
 * @param text El texto que se mostrará dentro del botón.
 * @param onClick La función que se ejecutará cuando el botón sea presionado.
 * @param modifier Modificador para personalizar el diseño del botón.
 * @param style El estilo visual del botón (Primary, Secondary, Tertiary).
 * @param size El tamaño del botón (Small, Medium, Large).
 * @param enabled Indica si el botón está habilitado o deshabilitado.
 * @param loading Indica si el botón está en estado de carga, mostrando un indicador de progreso.
 * @param icon Un ícono opcional que se mostrará junto al texto del botón.
 * @param iconContentDescription Descripción para accesibilidad del ícono.
 */
@Composable
fun RumboButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: RumboButtonStyle = RumboButtonStyle.Primary,
    size: RumboButtonSize = RumboButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: Painter? = null,
    iconContentDescription: String? = null
) {
    val contentPadding =
        PaddingValues(horizontal = size.horizontalPadding, vertical = size.verticalPadding)
    val shape = RoundedCornerShape(8.dp)
    val isInteractable = enabled && !loading
    val textStyle =
        if (size == RumboButtonSize.Large) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.labelLarge

    when (style) {
        // Definicion del estilo del botón primario
        RumboButtonStyle.Primary -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(size.height),
                enabled = isInteractable,
                contentPadding = contentPadding,
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (loading) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            ) {
                ButtonContent(
                    text,
                    textStyle,
                    MaterialTheme.colorScheme.onPrimary,
                    loading,
                    MaterialTheme.colorScheme.onPrimary,
                    icon,
                    iconContentDescription,
                    size.iconSize,
                    enabled
                )
            }
        }

        // Definicion del estilo del botón secundario
        RumboButtonStyle.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.height(size.height),
                enabled = isInteractable,
                contentPadding = contentPadding,
                shape = shape,
                border = BorderStroke(
                    1.dp,
                    if (enabled && !loading) MaterialTheme.colorScheme.outline
                    else if (loading) MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (loading) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            ) {
                ButtonContent(
                    text,
                    textStyle,
                    if (loading) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.primary,
                    loading,
                    MaterialTheme.colorScheme.primary,
                    icon,
                    iconContentDescription,
                    size.iconSize,
                    enabled
                )
            }
        }

        // Definicion del estilo del botón terciario
        RumboButtonStyle.Tertiary -> {
            TextButton(
                onClick = onClick,
                modifier = modifier.height(size.height),
                enabled = isInteractable,
                contentPadding = contentPadding,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (loading) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            ) {
                ButtonContent(
                    text,
                    textStyle,
                    if (loading) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.primary,
                    loading,
                    MaterialTheme.colorScheme.primary,
                    icon,
                    iconContentDescription,
                    size.iconSize,
                    enabled
                )
            }
        }
    }
}

/**
 * Contenido interno del botón, mostrando el texto, ícono y estado de carga.
 *
 * @param text El texto que se mostrará dentro del botón.
 * @param textStyle El estilo de texto a utilizar.
 * @param textColor El color del texto.
 * @param loading Indica si el botón está en estado de carga, mostrando un indicador de progreso.
 * @param loaderColor El color del indicador de carga.
 * @param icon Un ícono opcional que se mostrará junto al texto del botón.
 * @param iconContentDescription Descripción para accesibilidad del ícono.
 * @param iconSize El tamaño del ícono a mostrar.
 * @param enabled Indica si el botón está habilitado o deshabilitado, afectando la opacidad del texto.
 */
@Composable
private fun ButtonContent(
    text: String,
    textStyle: TextStyle,
    textColor: Color,
    loading: Boolean,
    loaderColor: Color,
    icon: Painter?,
    iconContentDescription: String?,
    iconSize: Dp,
    enabled: Boolean
) {
    // Definición del icono de carga
    if (loading) {
        CircularProgressIndicator(
            modifier = Modifier.size(18.dp),
            color = loaderColor,
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
    }

    // Definición del icono del botón, solo se muestra si no está en estado de carga
    if (icon != null && !loading) {
        Icon(
            painter = icon,
            contentDescription = iconContentDescription,
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.width(8.dp))
    }

    // Definición del texto del botón, con opacidad reducida si el botón está deshabilitado
    Text(
        text = text,
        style = textStyle,
        color = if (enabled) textColor else textColor.copy(alpha = 0.38f)
    )
}

@Preview(showBackground = true, name = "Buttons - Light")
@Composable
private fun ButtonLightPreview() {
    RumboTheme(darkTheme = false) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RumboButton(text = "Primary", onClick = {}, style = RumboButtonStyle.Primary)
            RumboButton(text = "Secondary", onClick = {}, style = RumboButtonStyle.Secondary)
            RumboButton(text = "Tertiary", onClick = {}, style = RumboButtonStyle.Tertiary)
            RumboButton(text = "Disabled", onClick = {}, enabled = false)
            RumboButton(text = "Loading...", onClick = {}, loading = true)
            RumboButton(text = "Small", onClick = {}, size = RumboButtonSize.Small)
            RumboButton(text = "Large", onClick = {}, size = RumboButtonSize.Large)
            RumboButton(
                text = "With Icon",
                onClick = {},
                icon = painterResource(id = R.drawable.ic_check),
                iconContentDescription = "Check Icon"
            )
        }
    }
}

@Preview(showBackground = true, name = "Buttons - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun ButtonDarkPreview() {
    RumboTheme(darkTheme = true) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RumboButton(text = "Primary", onClick = {}, style = RumboButtonStyle.Primary)
            RumboButton(text = "Secondary", onClick = {}, style = RumboButtonStyle.Secondary)
            RumboButton(text = "Tertiary", onClick = {}, style = RumboButtonStyle.Tertiary)
            RumboButton(text = "Disabled", onClick = {}, enabled = false)
            RumboButton(text = "Loading...", onClick = {}, loading = true)
            RumboButton(text = "Small", onClick = {}, size = RumboButtonSize.Small)
            RumboButton(text = "Large", onClick = {}, size = RumboButtonSize.Large)
            RumboButton(
                text = "With Icon",
                onClick = {},
                icon = painterResource(id = R.drawable.ic_globe),
                iconContentDescription = "Plus Icon"
            )
        }
    }
}