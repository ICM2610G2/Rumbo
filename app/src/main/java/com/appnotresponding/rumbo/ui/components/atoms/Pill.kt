package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.AppTheme

// Definición de los estilos de las pills
enum class RumboPillStyle {
    Filled,
    Outlined,
    Tonal
}

/**
 * Componente RumboPill para mostrar opciones seleccionables en forma de "píldora".
 * @param text El texto a mostrar dentro de la pill
 * @param modifier Modificador para personalizar la apariencia y comportamiento de la pill
 * @param style El estilo visual de la pill (Filled, Outlined, Tonal)
 * @param selected Indica si la pill está seleccionada o no, afectando su apariencia
 * @param onClick Acción a ejecutar al hacer clic en la pill (opcional)
 * @param icon Icono opcional para mostrar junto al texto dentro de la pill
 * @param iconContentDescription Descripción para accesibilidad del icono (opcional)
 */
@Composable
fun RumboPill(
    text: String,
    modifier: Modifier = Modifier,
    style: RumboPillStyle = RumboPillStyle.Filled,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    icon: Painter? = null,
    iconContentDescription: String? = null
) {
    val shape = RoundedCornerShape(50)

    val backgroundColor: Color
    val textColor: Color
    val borderColor: Color

    when (style) {
        //Definicion del estillo relleno
        RumboPillStyle.Filled -> {
            backgroundColor =
                if (selected) AppTheme.colorScheme.primary else AppTheme.colorScheme.primaryContainer
            textColor =
                if (selected) AppTheme.colorScheme.onPrimary else AppTheme.colorScheme.onPrimaryContainer
            borderColor = Color.Transparent
        }

        //Definicion del estillo de contorno
        RumboPillStyle.Outlined -> {
            backgroundColor = Color.Transparent
            textColor =
                if (selected) AppTheme.colorScheme.primary else AppTheme.colorScheme.onSurfaceVariant
            borderColor =
                if (selected) AppTheme.colorScheme.primary else AppTheme.colorScheme.outline
        }

        //Definicion del estilo de tono
        RumboPillStyle.Tonal -> {
            backgroundColor =
                if (selected) AppTheme.colorScheme.secondaryContainer else AppTheme.colorScheme.surfaceContainerHigh
            textColor =
                if (selected) AppTheme.colorScheme.onSecondaryContainer else AppTheme.colorScheme.onSurfaceVariant
            borderColor = Color.Transparent
        }
    }

    Row(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor, shape)
            .then(
                if (borderColor != Color.Transparent) Modifier.border(1.dp, borderColor, shape)
                else Modifier
            )
            .then(
                if (onClick != null) Modifier.clickable { onClick() }
                else Modifier
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = iconContentDescription,
                modifier = Modifier.size(16.dp),
                tint = textColor
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        RumboText(
            text = text,
            style = RumboTextStyle.Button,
            color = textColor,
            maxLines = 1
        )
    }
}



@Preview(showBackground = true, name = "Pill - Light")
@Composable
private fun PillLightPreview() {
    AppTheme(darkTheme = false) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RumboPill(text = "Playa", style = RumboPillStyle.Filled, selected = true, icon = painterResource(id = R.drawable.ic_globe))
                RumboPill(text = "Montaña", style = RumboPillStyle.Filled)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RumboPill(text = "Ciudad", style = RumboPillStyle.Outlined, selected = true)
                RumboPill(text = "Rural", style = RumboPillStyle.Outlined)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RumboPill(text = "Aventura", style = RumboPillStyle.Tonal, selected = true)
                RumboPill(text = "Relax", style = RumboPillStyle.Tonal)
            }
        }
    }
}

@Preview(showBackground = true, name = "Pill - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun PillDarkPreview() {
    AppTheme(darkTheme = true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RumboPill(text = "Playa", style = RumboPillStyle.Filled, selected = true, icon = painterResource(id = R.drawable.ic_globe))
                RumboPill(text = "Montaña", style = RumboPillStyle.Filled)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RumboPill(text = "Ciudad", style = RumboPillStyle.Outlined, selected = true)
                RumboPill(text = "Rural", style = RumboPillStyle.Outlined)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RumboPill(text = "Aventura", style = RumboPillStyle.Tonal, selected = true)
                RumboPill(text = "Relax", style = RumboPillStyle.Tonal)
            }
        }
    }
}