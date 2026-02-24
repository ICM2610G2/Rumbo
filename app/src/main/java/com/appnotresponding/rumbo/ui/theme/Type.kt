package com.appnotresponding.rumbo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
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
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
        weight = FontWeight.Bold
    )
)

// Custom heading styles
val h1 = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 32.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 40.sp,
    letterSpacing = (-0.5).sp
)

val h2 = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 28.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 36.sp,
    letterSpacing = 0.sp
)

val h3 = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 32.sp,
    letterSpacing = 0.sp
)

val h4 = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 28.sp,
    letterSpacing = 0.2.sp
)

// Body text style
val bodyText = TextStyle(
    fontFamily = bodyFontFamily,
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp
)

// Button text style
val buttonText = TextStyle(
    fontFamily = bodyFontFamily,
    fontSize = 14.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp
)

val rumboTypography = Typography(
    displayLarge = h1,
    displayMedium = h2,
    displaySmall = h3,
    headlineLarge = h4,
//    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
//    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
//    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
//    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
//    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
    bodyLarge = bodyText,
//    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
//    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = buttonText,
//    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
//    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)

