package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme

enum class RumboButtonStyle {
    Primary, Secondary, Tertiary
}

enum class RumboButtonSize(
    val height: Dp, val horizontalPadding: Dp, val verticalPadding: Dp, val iconSize: Dp
) {
    Small(32.dp, 12.dp, 6.dp, 16.dp),
    Medium(44.dp, 20.dp, 10.dp, 18.dp),
    Large(52.dp, 28.dp, 14.dp, 20.dp)
}

/**
 * Botón personalizable de Rumbo. Incluye:
 * - Scale spring al presionar (0.97) para feedback táctil en todos los estilos.
 * - AnimatedContent en el texto para crossfade cuando cambia (p.ej. "Añadir" → "Eliminar").
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
    val contentPadding = PaddingValues(horizontal = size.horizontalPadding, vertical = size.verticalPadding)
    val shape = MaterialTheme.shapes.medium
    val isInteractable = enabled && !loading
    val textStyle = if (size == RumboButtonSize.Large) MaterialTheme.typography.bodyMedium
                    else MaterialTheme.typography.labelLarge

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && isInteractable) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )

    val scaledModifier = modifier
        .height(size.height)
        .graphicsLayer { scaleX = scale; scaleY = scale }

    when (style) {
        RumboButtonStyle.Primary -> Button(
            onClick = onClick,
            modifier = scaledModifier,
            enabled = isInteractable,
            contentPadding = contentPadding,
            shape = shape,
            interactionSource = interactionSource,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (loading) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                 else MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        ) {
            ButtonContent(text, textStyle, MaterialTheme.colorScheme.onPrimary, loading,
                MaterialTheme.colorScheme.onPrimary, icon, iconContentDescription, size.iconSize, enabled)
        }

        RumboButtonStyle.Secondary -> OutlinedButton(
            onClick = onClick,
            modifier = scaledModifier,
            enabled = isInteractable,
            contentPadding = contentPadding,
            shape = shape,
            interactionSource = interactionSource,
            border = BorderStroke(
                1.dp, when {
                    loading -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    enabled -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = if (loading) MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
                               else MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        ) {
            val contentColor = if (loading) MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
                               else MaterialTheme.colorScheme.onSecondaryContainer
            ButtonContent(text, textStyle, contentColor, loading, MaterialTheme.colorScheme.onSecondaryContainer,
                icon, iconContentDescription, size.iconSize, enabled)
        }

        RumboButtonStyle.Tertiary -> TextButton(
            onClick = onClick,
            modifier = scaledModifier,
            enabled = isInteractable,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            colors = ButtonDefaults.textButtonColors(
                contentColor = if (loading) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
                               else MaterialTheme.colorScheme.tertiary,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        ) {
            val contentColor = if (loading) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
                               else MaterialTheme.colorScheme.tertiary
            ButtonContent(text, textStyle, contentColor, loading, MaterialTheme.colorScheme.tertiary,
                icon, iconContentDescription, size.iconSize, enabled)
        }
    }
}

/**
 * Contenido del botón. El texto usa [AnimatedContent] para un crossfade suave
 * cuando su valor cambia (p.ej. "Añadir" → "Eliminar del itinerario").
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
    if (loading) {
        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = loaderColor, strokeWidth = 2.dp)
        Spacer(modifier = Modifier.width(8.dp))
    }

    if (icon != null && !loading) {
        Icon(painter = icon, contentDescription = iconContentDescription, modifier = Modifier.size(iconSize))
        Spacer(modifier = Modifier.width(8.dp))
    }

    AnimatedContent(
        targetState = text,
        transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(100)) },
        label = "buttonText"
    ) { targetText ->
        Text(
            text = targetText,
            style = textStyle,
            color = if (enabled) textColor else textColor.copy(alpha = 0.38f)
        )
    }
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
            RumboButton(text = "Primary", onClick = {})
            RumboButton(text = "Secondary", onClick = {}, style = RumboButtonStyle.Secondary)
            RumboButton(text = "Tertiary", onClick = {}, style = RumboButtonStyle.Tertiary)
            RumboButton(text = "Disabled", onClick = {}, enabled = false)
            RumboButton(text = "Loading...", onClick = {}, loading = true)
            RumboButton(text = "Small", onClick = {}, size = RumboButtonSize.Small)
            RumboButton(text = "Large", onClick = {}, size = RumboButtonSize.Large)
            RumboButton(text = "Con Ícono", onClick = {}, icon = painterResource(R.drawable.ic_check))
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
            RumboButton(text = "Primary", onClick = {})
            RumboButton(text = "Secondary", onClick = {}, style = RumboButtonStyle.Secondary)
            RumboButton(text = "Tertiary", onClick = {}, style = RumboButtonStyle.Tertiary)
            RumboButton(text = "Disabled", onClick = {}, enabled = false)
            RumboButton(text = "Loading...", onClick = {}, loading = true)
            RumboButton(text = "Small", onClick = {}, size = RumboButtonSize.Small)
            RumboButton(text = "Large", onClick = {}, size = RumboButtonSize.Large)
            RumboButton(text = "Con Ícono", onClick = {}, icon = painterResource(R.drawable.ic_globe))
        }
    }
}
