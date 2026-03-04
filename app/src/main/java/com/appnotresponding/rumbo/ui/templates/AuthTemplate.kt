package com.appnotresponding.rumbo.ui.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Plantilla (Template) para las pantallas de Autenticación.
 * Define la estructura visual base: el fondo estilo «Aurora» verde y negro (según mockup),
 * y centra el contenido que se le pase por parámetro.
 *
 * @param modifier Modificador para el contenedor principal.
 * @param content El contenido interno (por ejemplo, el LoginForm o el SignUpForm).
 */
@Composable
fun AuthTemplate(
    modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit
) {

    val highlightGreen = Color(0xFFC4F031)
    val darkBackground = Color(0xFF151515)


    val backgroundBrush = Brush.radialGradient(
        colors = listOf(
            highlightGreen.copy(alpha = 0.35f), darkBackground, Color.Black
        ), center = Offset(x = 100f, y = 800f), radius = 1200f
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
