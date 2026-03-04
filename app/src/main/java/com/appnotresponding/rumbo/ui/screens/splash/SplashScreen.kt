package com.appnotresponding.rumbo.ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import kotlinx.coroutines.delay

/**
 * Pantalla de bienvenida (Splash) de la aplicación Rumbo.

 * Animación de entrada de los botones
 * Los botones se hacen visibles usando [AnimatedVisibility] con una combinación de dos efectos:
 *
 * - **[fadeIn]**: Opacidad de 0 a 1 en 500 ms.
 *   Documentación: https://developer.android.com/reference/kotlin/androidx/compose/animation/package-summary#fadeIn
 *
 * - **[slideInVertically]**: Desplazamiento vertical desde la mitad inferior del componente
 *   (`initialOffsetY = { it / 2 }`) hasta su posición final, también en 500 ms.
 *   Documentación: https://developer.android.com/reference/kotlin/androidx/compose/animation/package-summary#slideInVertically
 *
 * Ambos efectos usan [tween] como especificación de animación, con duración de 500 ms y por defecto (`FastOutSlowIn`).
 * Documentación de tween: https://developer.android.com/reference/kotlin/androidx/compose/animation/core/package-summary#tween
 *
 * El estado `ctaVisible` va a `true` una sola vez al entrar a la composición.
 *
 */

@Composable
fun SplashScreen(
    controller: NavHostController
) {
    var ctaVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(800)
        ctaVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Image(
            painter = painterResource(id = R.mipmap.img_logo_splash),
            contentDescription = "Rumbo logo",
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.55f)
        )

        // Botones animados en la parte inferior
        AnimatedVisibility(
            visible = ctaVisible,
            enter = fadeIn(animationSpec = tween(500)) +
                    slideInVertically(
                        animationSpec = tween(500),
                        initialOffsetY = { it / 2 }
                    ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 48.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RumboButton(
                    text = "Iniciar Sesión",
                    onClick = {controller.navigate(AppScreens.LogIn.name)},
                    modifier = Modifier.fillMaxWidth()
                )
                RumboButton(
                    text = "Registrarse",
                    style = RumboButtonStyle.Secondary,
                    onClick = {controller.navigate(AppScreens.SignUp.name)},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Pantalla Splash - Dark", backgroundColor = 0xFF121212)
@Composable
private fun SplashScreenDarkPreview() {
    RumboTheme(darkTheme = true) {
        SplashScreen(controller = rememberNavController())
    }
}

