package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Componente de tarjeta genérico para la aplicación Rumbo.
 * Envuelve el contenido proporcionado dentro de un contenedor [Box] con padding configurable.
 *
 * @param modifier Modificador para personalizar el diseño y la apariencia de la tarjeta (por defecto 16.dp de padding).
 * @param content El contenido composable que se mostrará dentro de la tarjeta.
 */
@Composable
fun RumboCard(modifier: Modifier = Modifier.padding(16.dp), content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        content()
    }
}