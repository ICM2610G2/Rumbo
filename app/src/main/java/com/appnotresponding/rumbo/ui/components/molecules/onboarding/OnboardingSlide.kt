package com.appnotresponding.rumbo.ui.components.molecules.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Composable representing a single onboarding slide with a title, subtitle, illustration, and description.
 * @param title The main title of the onboarding slide.
 * @param subtitle A brief subtitle providing additional context.
 * @param illustrationRes The resource ID of the illustration to display
 */
@Composable
fun OnboardingSlide(
    title: String, subtitle: String, illustrationRes: Int, description: String
) {

    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
            64.dp, Alignment.CenterVertically
        ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Title
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            //Subtitle
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
        //Illustration
        Image(
            modifier = Modifier
                .fillMaxSize(0.6f),
            painter = painterResource(illustrationRes),
            contentDescription = null
        )
        //Mini Paragraph
        Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }

}

@Preview(showBackground = true, name = "OnboardingSlide - Light")
@Composable
private fun OnboardingSlideLightPreview() {
    RumboTheme(darkTheme = false) {
        OnboardingSlide(
            title = "¡Bienvenido a Rumbo!",
            subtitle = "Tu copiloto de viaje que entiende lo que ves, te traduce al instante y reordena tu día cuando algo cambia.",
            illustrationRes = R.drawable.img_onboaring_1,
            description = "Rumbo te ayuda a descubrir lugares como un local, traducir al instante lo que ves, reorganizar tu día cuando algo cambia y navegar con mapas que funcionan incluso sin conexión. Todo en un solo lugar."
        )
    }
}

@Preview(showBackground = true, name = "OnboardingSlide - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun OnboardingSlideDarkPreview() {
    RumboTheme(darkTheme = true) {
        OnboardingSlide(
            title = "¡Bienvenido a Rumbo!",
            subtitle = "Tu copiloto de viaje que entiende lo que ves, te traduce al instante y reordena tu día cuando algo cambia.",
            illustrationRes = R.drawable.img_onboaring_1,
            description = "Rumbo te ayuda a descubrir lugares como un local, traducir al instante lo que ves, reorganizar tu día cuando algo cambia y navegar con mapas que funcionan incluso sin conexión. Todo en un solo lugar."
        )
    }
}
