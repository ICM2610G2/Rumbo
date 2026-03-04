package com.appnotresponding.rumbo.ui.components.molecules.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Botones de navegación para las pantallas de introducción (onboarding).
 *
 * @param onNext Callback que se ejecuta cuando se hace clic en el botón "Siguiente".
 * @param onBack Callback que se ejecuta cuando se hace clic en el botón "Atrás".
 * @param isFirstPage Indica si la página actual es la primera (oculta "Atrás" si es true).
 * @param isLastPage Indica si la página actual es la última (oculta "Siguiente" si es true).
 */
@Composable
fun OnboardingNav(
    onNext: () -> Unit, onBack: () -> Unit, isFirstPage: Boolean, isLastPage: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isFirstPage) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        } else {
            Box(modifier = Modifier.size(48.dp))
        }
        if (!isLastPage) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = "Next",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        } else {
            Box(modifier = Modifier.size(48.dp))
        }
    }
}

@Preview(showBackground = true, name = "OnboardingNav First Page - Light")
@Composable
private fun OnboardingNavFirstPageLightPreview() {
    RumboTheme(darkTheme = false) {
        OnboardingNav(
            onNext = {}, onBack = {}, isFirstPage = true, isLastPage = false
        )
    }
}

@Preview(
    showBackground = true, name = "OnboardingNav First Page - Dark", backgroundColor = 0xFF1E1E1E
)
@Composable
private fun OnboardingNavFirstPageDarkPreview() {
    RumboTheme(darkTheme = true) {
        OnboardingNav(
            onNext = {}, onBack = {}, isFirstPage = true, isLastPage = false
        )
    }
}

@Preview(showBackground = true, name = "OnboardingNav Middle Page - Light")
@Composable
private fun OnboardingNavMiddlePageLightPreview() {
    RumboTheme(darkTheme = false) {
        OnboardingNav(
            onNext = {}, onBack = {}, isFirstPage = false, isLastPage = false
        )
    }
}

@Preview(
    showBackground = true, name = "OnboardingNav Middle Page - Dark", backgroundColor = 0xFF1E1E1E
)
@Composable
private fun OnboardingNavMiddlePageDarkPreview() {
    RumboTheme(darkTheme = true) {
        OnboardingNav(
            onNext = {}, onBack = {}, isFirstPage = false, isLastPage = false
        )
    }
}

@Preview(showBackground = true, name = "OnboardingNav Last Page - Light")
@Composable
private fun OnboardingNavLastPageLightPreview() {
    RumboTheme(darkTheme = false) {
        OnboardingNav(
            onNext = {}, onBack = {}, isFirstPage = false, isLastPage = true
        )
    }
}

@Preview(
    showBackground = true, name = "OnboardingNav Last Page - Dark", backgroundColor = 0xFF1E1E1E
)
@Composable
private fun OnboardingNavLastPageDarkPreview() {
    RumboTheme(darkTheme = true) {
        OnboardingNav(
            onNext = {}, onBack = {}, isFirstPage = false, isLastPage = true
        )
    }
}

