package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Generic card component for the Rumbo application.
 * Wraps the provided content inside a [Box] container with configurable padding.
 *
 * @param modifier Modifier to customize the card's layout and appearance (defaults to 16.dp padding).
 * @param content The composable content to be displayed inside the card.
 */
@Composable
fun RumboCard(modifier: Modifier = Modifier.padding(16.dp), content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        content()
    }
}