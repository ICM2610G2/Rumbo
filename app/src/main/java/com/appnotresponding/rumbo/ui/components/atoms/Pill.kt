package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme

enum class RumboPillStyle {
    Filled, Outlined, Tonal
}

/**
 * Componente RumboPill para mostrar opciones seleccionables en forma de "píldora".
 * El cambio de estado [selected] se anima suavemente con [animateColorAsState].
 *
 * @param text El texto a mostrar dentro de la pill
 * @param style El estilo visual de la pill (Filled, Outlined, Tonal)
 * @param selected Indica si la pill está seleccionada, afectando fondo, texto y borde
 * @param onClick Acción a ejecutar al hacer clic (opcional)
 * @param icon Icono opcional a mostrar junto al texto
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
    val targetBg: Color
    val targetText: Color
    val targetBorder: Color

    when (style) {
        RumboPillStyle.Filled -> {
            targetBg = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
            targetText = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
            targetBorder = Color.Transparent
        }
        RumboPillStyle.Outlined -> {
            targetBg = Color.Transparent
            targetText = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            targetBorder = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        }
        RumboPillStyle.Tonal -> {
            targetBg = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh
            targetText = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            targetBorder = Color.Transparent
        }
    }

    val backgroundColor by animateColorAsState(targetBg, tween(200), label = "pillBg")
    val textColor by animateColorAsState(targetText, tween(200), label = "pillText")
    val borderColor by animateColorAsState(targetBorder, tween(200), label = "pillBorder")

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor, CircleShape)
            .then(
                if (style == RumboPillStyle.Outlined)
                    Modifier.border(1.dp, borderColor, CircleShape)
                else Modifier
            )
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
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
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            maxLines = 1
        )
    }
}


@Preview(showBackground = true, name = "Pill - Light")
@Composable
private fun PillLightPreview() {
    RumboTheme(darkTheme = false) {
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
    RumboTheme(darkTheme = true) {
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
