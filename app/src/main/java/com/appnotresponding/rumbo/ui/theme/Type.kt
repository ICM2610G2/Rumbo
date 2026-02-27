package com.appnotresponding.rumbo.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appnotresponding.rumbo.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.Bold
    )
)

val baseline = Typography()
val rumboTypography = Typography(
    //H1
    displayLarge = baseline.displayLarge.copy(
        fontFamily = displayFontFamily,
        fontSize = 57.sp,
        fontWeight = FontWeight.Bold,
    ),
    //H2
    displayMedium = baseline.displayMedium.copy(
        fontFamily = displayFontFamily,
        fontSize = 45.sp,
        fontWeight = FontWeight.Bold
    ),
    //H3
    displaySmall = baseline.displaySmall.copy(
        fontFamily = displayFontFamily,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold
    ),
    //H4
    headlineLarge = baseline.headlineLarge.copy(
        fontFamily = displayFontFamily,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    ),
    //H5
    headlineMedium = baseline.headlineMedium.copy(
        fontFamily = displayFontFamily,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    ),
    //H6
    headlineSmall = baseline.headlineSmall.copy(
        fontFamily = displayFontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    //Subtitle 1
    titleLarge = baseline.titleLarge.copy(
        fontFamily = displayFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold
    ),
    //Subtitle 2
    titleMedium = baseline.titleMedium.copy(
        fontFamily = displayFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    ),
    //Subtitle 3
    titleSmall = baseline.titleSmall.copy(
        fontFamily = displayFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    ),
    //Body 1
    bodyLarge = baseline.bodyLarge.copy(
        fontFamily = bodyFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),
    //Body 2
    bodyMedium = baseline.bodyMedium.copy(
        fontFamily = bodyFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    //Body 3
    bodySmall = baseline.bodySmall.copy(
        fontFamily = bodyFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    ),
    //Label 1
    labelLarge = baseline.labelLarge.copy(
        fontFamily = bodyFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),
    //Label 2
    labelMedium = baseline.labelMedium.copy(
        fontFamily = bodyFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    ),
    //Label 3
    labelSmall = baseline.labelSmall.copy(
        fontFamily = bodyFontFamily,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
    ),
)

@Preview(showBackground = true, name = "Type - Light")
@Composable
fun TypographyPreviewLight() {
    val styles = listOf(
        rumboTypography.displayLarge,
        rumboTypography.displayMedium,
        rumboTypography.displaySmall,
        rumboTypography.headlineLarge,
        rumboTypography.headlineMedium,
        rumboTypography.headlineSmall,
        rumboTypography.titleLarge,
        rumboTypography.titleMedium,
        rumboTypography.titleSmall,
        rumboTypography.bodyLarge,
        rumboTypography.bodyMedium,
        rumboTypography.bodySmall,
        rumboTypography.labelLarge,
        rumboTypography.labelMedium,
        rumboTypography.labelSmall
    )
    RumboTheme(darkTheme = false) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (style in styles) {
                    Text(
                        text = "Typography Preview",
                        style = style,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Type - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
fun TypographyPreview() {
    val styles = listOf(
        rumboTypography.displayLarge,
        rumboTypography.displayMedium,
        rumboTypography.displaySmall,
        rumboTypography.headlineLarge,
        rumboTypography.headlineMedium,
        rumboTypography.headlineSmall,
        rumboTypography.titleLarge,
        rumboTypography.titleMedium,
        rumboTypography.titleSmall,
        rumboTypography.bodyLarge,
        rumboTypography.bodyMedium,
        rumboTypography.bodySmall,
        rumboTypography.labelLarge,
        rumboTypography.labelMedium,
        rumboTypography.labelSmall
    )
    RumboTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (style in styles) {
                    Text(
                        text = "Typography Preview",
                        style = style,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}