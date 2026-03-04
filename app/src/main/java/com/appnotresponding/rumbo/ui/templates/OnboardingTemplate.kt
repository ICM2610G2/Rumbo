package com.appnotresponding.rumbo.ui.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.components.molecules.onboarding.OnboardingNav
import com.appnotresponding.rumbo.ui.components.molecules.onboarding.OnboardingSlide
import com.appnotresponding.rumbo.ui.theme.RumboTheme

@Composable
fun OnboardingTemplate(onFinishOnboarding: () -> Unit) {

    var currentSlide by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.weight(1f)) {

            when (currentSlide) {
                0 -> {
                    OnboardingSlide(
                        title = "¡Bienvenido a Rumbo!",
                        subtitle = "Tu copiloto de viaje que entiende lo que ves, te traduce al instante y reordena tu dia cuando algo cambia.",
                        illustrationRes = R.mipmap.img_onboaring_1,
                        description = "Rumbo te ayuda a descubrir lugares como un local, traducir al instante lo que ves, reorganizar tu dia cuando algo cambia y navegar con mapas que funcionan incluso sin conexion. Todo en un solo lugar."
                    )
                }

                1 -> {
                    OnboardingSlide(
                        title = "Itinerarios que se adaptan a ti.",
                        subtitle = "Tu dia se reorganiza automaticamente cuando algo cambia.",
                        illustrationRes = R.mipmap.img_onboarding_2,
                        description = "Rumbo crea un plan inteligente segun tus gustos y lo ajusta en tiempo real segun clima, horarios, aforo, estado del transporte y novedades del destino."
                    )
                }

                2 -> {
                    OnboardingSlide(
                        title = "Tu mapa inteligente y sin conexion",
                        subtitle = "Rutas claras, seguras y adaptadas al momento.",
                        illustrationRes = R.mipmap.img_onboarding_3,
                        description = "Explora con un mapa diseñado para viajeros. Funciona sin internet y te muestra un mapa de calor para identificar zonas concurridas, evitar filas y moverte con mas seguridad."
                    )
                }
            }
        }

        OnboardingNav(
            onNext = {
            if (currentSlide < 2) {
                currentSlide++
            } else {
                onFinishOnboarding()
            }
        }, onBack = {
            if (currentSlide > 0) {
                currentSlide--
            }
        }, isFirstPage = (currentSlide == 0), isLastPage = (currentSlide == 2)
        )
    }
}

@Preview(showBackground = true, name = "OnboardingTemplate - Light")
@Composable
private fun OnboardingTemplateLightPreview() {
    RumboTheme(darkTheme = false) {
        OnboardingTemplate(onFinishOnboarding = {})
    }
}

@Preview(showBackground = true, name = "OnboardingTemplate - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun OnboardingTemplateDarkPreview() {
    RumboTheme(darkTheme = true) {
        OnboardingTemplate(onFinishOnboarding = {})
    }
}