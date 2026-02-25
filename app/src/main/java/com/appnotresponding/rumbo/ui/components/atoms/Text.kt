package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.ui.theme.AppTheme

// Definición de los estilos de texto disponibles en la aplicación
enum class RumboTextStyle {
    H1,
    H2,
    H3,
    H4,
    Body,
    Button
}


/**
 * Componente RumboText para mostrar texto con estilos predefinidos.
 * @param text El texto a mostrar
 * @param modifier Modificador para personalizar la apariencia y comportamiento del texto
 * @param style El estilo de texto a aplicar (H1, H2, H3, H4, Body, Button)
 * @param color El color del texto (por defecto se usa el color onBackground del tema)
 * @param maxLines El número máximo de líneas a mostrar (por defecto es ilimitado)
 */
@Composable
fun RumboText(
    text: String,
    modifier: Modifier = Modifier,
    style: RumboTextStyle = RumboTextStyle.Body,
    color: Color = AppTheme.colorScheme.onBackground,
    maxLines: Int = Int.MAX_VALUE
) {
    val typography = AppTheme.typography
    val textStyle: TextStyle = when (style) {
        RumboTextStyle.H1 -> typography.h1
        RumboTextStyle.H2 -> typography.h2
        RumboTextStyle.H3 -> typography.h3
        RumboTextStyle.H4 -> typography.h4
        RumboTextStyle.Body -> typography.bodyText
        RumboTextStyle.Button -> typography.buttonText
    }

    Text(
        text = text,
        modifier = modifier,
        style = textStyle,
        color = color,
        maxLines = maxLines
    )
}


@Preview(showBackground = true, name = "Text - Light")
@Composable
private fun TextLightPreview() {
    AppTheme(darkTheme = false) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (style in RumboTextStyle.values()) {
                RumboText(
                    text = "Rumbo Typography - ${style.name}",
                    style = style,
                    color = AppTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Text - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun TextDarkPreview() {
    AppTheme(darkTheme = true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (style in RumboTextStyle.values()) {
                RumboText(
                    text = "Rumbo Typography - ${style.name}",
                    style = style,
                    color = AppTheme.colorScheme.primary
                )
            }
        }
    }
}
