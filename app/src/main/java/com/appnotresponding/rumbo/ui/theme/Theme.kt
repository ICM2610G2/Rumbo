package com.appnotresponding.rumbo.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

//THEME WRAPPER

@Composable
fun RumboTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val rumboColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = rumboColorScheme,
        typography = rumboTypography,
        shapes = rumboShapes,
        content = content
    )
}

//HELPERS

@Composable
private fun ColorSwatch(
    label: String,
    background: Color,
    foreground: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(background)
            .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = label,
            color = foreground,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ColorPairRow(
    label: String,
    containerColor: Color,
    contentColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ColorSwatch(
            label = label,
            background = containerColor,
            foreground = contentColor,
            modifier = Modifier.weight(1f).height(36.dp)
        )
        ColorSwatch(
            label = "on $label",
            background = contentColor,
            foreground = containerColor,
            modifier = Modifier.weight(1f).height(36.dp)
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
    )
}

// COLORS

@Composable
private fun ColorsPreviewContent() {
    val cs = MaterialTheme.colorScheme

    Surface(color = cs.background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Color Scheme", style = MaterialTheme.typography.titleLarge, color = cs.onSurface)

            SectionTitle("Primary")
            ColorPairRow("Primary", cs.primary, cs.onPrimary)
            ColorPairRow("PrimaryContainer", cs.primaryContainer, cs.onPrimaryContainer)

            SectionTitle("Secondary")
            ColorPairRow("Secondary", cs.secondary, cs.onSecondary)
            ColorPairRow("SecondaryContainer", cs.secondaryContainer, cs.onSecondaryContainer)

            SectionTitle("Tertiary")
            ColorPairRow("Tertiary", cs.tertiary, cs.onTertiary)
            ColorPairRow("TertiaryContainer", cs.tertiaryContainer, cs.onTertiaryContainer)

            SectionTitle("Error")
            ColorPairRow("Error", cs.error, cs.onError)
            ColorPairRow("ErrorContainer", cs.errorContainer, cs.onErrorContainer)

            SectionTitle("Background & Surface")
            ColorPairRow("Background", cs.background, cs.onBackground)
            ColorPairRow("Surface", cs.surface, cs.onSurface)
            ColorPairRow("SurfaceVariant", cs.surfaceVariant, cs.onSurfaceVariant)

            SectionTitle("Surface Containers")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                listOf(
                    "Lowest" to cs.surfaceContainerLowest,
                    "Low" to cs.surfaceContainerLow,
                    "Def" to cs.surfaceContainer,
                    "High" to cs.surfaceContainerHigh,
                    "Max" to cs.surfaceContainerHighest,
                ).forEach { (name, color) ->
                    ColorSwatch(
                        label = name,
                        background = color,
                        foreground = cs.onSurface,
                        modifier = Modifier.weight(1f).height(40.dp)
                    )
                }
            }

            SectionTitle("Inverse & Outline")
            ColorPairRow("InverseSurface", cs.inverseSurface, cs.inverseOnSurface)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ColorSwatch(
                    label = "Outline",
                    background = cs.outline,
                    foreground = cs.surface,
                    modifier = Modifier.weight(1f).height(36.dp)
                )
                ColorSwatch(
                    label = "OutlineVariant",
                    background = cs.outlineVariant,
                    foreground = cs.onSurface,
                    modifier = Modifier.weight(1f).height(36.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Colors — Light", widthDp = 420, heightDp = 900)
@Composable
fun ColorsPreviewLight() {
    RumboTheme(darkTheme = false) { ColorsPreviewContent() }
}

@Preview(showBackground = true, name = "Colors — Dark", widthDp = 420, heightDp = 900, backgroundColor = 0xFF1E1E1E)
@Composable
fun ColorsPreviewDark() {
    RumboTheme(darkTheme = true) { ColorsPreviewContent() }
}

//TYPE

@Composable
private fun TypographyPreviewContent() {
    val cs = MaterialTheme.colorScheme
    val typo = MaterialTheme.typography

    Surface(color = cs.background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Typography", style = typo.titleLarge, color = cs.onSurface)
            Spacer(modifier = Modifier.height(4.dp))

            @Composable
            fun TypeRow(label: String, style: TextStyle) {
                Text(
                    text = "$label: Rumbo Aa Bb 123",
                    style = style,
                    color = cs.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            TypeRow("displayLarge 57sp", typo.displayLarge)
            TypeRow("displayMedium 45sp", typo.displayMedium)
            TypeRow("displaySmall 36sp", typo.displaySmall)
            Spacer(modifier = Modifier.height(4.dp))
            TypeRow("headlineLarge 32sp", typo.headlineLarge)
            TypeRow("headlineMedium 28sp", typo.headlineMedium)
            TypeRow("headlineSmall 24sp", typo.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            TypeRow("titleLarge 22sp", typo.titleLarge)
            TypeRow("titleMedium 16sp", typo.titleMedium)
            TypeRow("titleSmall 14sp", typo.titleSmall)
            Spacer(modifier = Modifier.height(4.dp))
            TypeRow("bodyLarge 16sp", typo.bodyLarge)
            TypeRow("bodyMedium 14sp", typo.bodyMedium)
            TypeRow("bodySmall 12sp", typo.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            TypeRow("labelLarge 14sp", typo.labelLarge)
            TypeRow("labelMedium 12sp", typo.labelMedium)
            TypeRow("labelSmall 11sp", typo.labelSmall)
        }
    }
}

@Preview(showBackground = true, name = "Typography — Light", widthDp = 1200, heightDp = 600)
@Composable
fun TypoPreviewLight() {
    RumboTheme(darkTheme = false) { TypographyPreviewContent() }
}

@Preview(showBackground = true, name = "Typography — Dark", widthDp = 1200, heightDp = 600, backgroundColor = 0xFF1E1E1E)
@Composable
fun TypoPreviewDark() {
    RumboTheme(darkTheme = true) { TypographyPreviewContent() }
}

//SHAPES

@Composable
private fun ShapeCard(
    label: String,
    shape: androidx.compose.ui.graphics.Shape
) {
    val cs = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(120.dp, 80.dp)
                .clip(shape)
                .background(cs.primaryContainer)
                .border(1.dp, cs.outline, shape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = cs.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = cs.onSurfaceVariant
        )
    }
}

@Composable
private fun ShapesPreviewContent() {
    val cs = MaterialTheme.colorScheme
    val shapes = MaterialTheme.shapes

    Surface(color = cs.background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Shapes", style = MaterialTheme.typography.titleLarge, color = cs.onSurface)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ShapeCard("Extra Small", shapes.extraSmall)
                ShapeCard("Small (4dp)", shapes.small)
                ShapeCard("Medium (8dp)", shapes.medium)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ShapeCard("Large (16dp)", shapes.large)
                ShapeCard("Extra Large", shapes.extraLarge)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Example cards using each shape
            Text("In context", style = MaterialTheme.typography.titleSmall, color = cs.primary)

            listOf(
                "Small — chips, icons" to shapes.small,
                "Medium — cards, dialogs" to shapes.medium,
                "Large — sheets, FABs" to shapes.large,
            ).forEach { (desc, shape) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                        .background(cs.surfaceContainerHigh)
                        .border(0.5.dp, cs.outlineVariant, shape)
                        .padding(16.dp)
                ) {
                    Text(text = desc, style = MaterialTheme.typography.bodyMedium, color = cs.onSurface)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Shapes — Light", widthDp = 420, heightDp = 550)
@Composable
fun ShapesPreviewLight() {
    RumboTheme(darkTheme = false) { ShapesPreviewContent() }
}

@Preview(showBackground = true, name = "Shapes — Dark", widthDp = 420, heightDp = 550, backgroundColor = 0xFF1E1E1E)
@Composable
fun ShapesPreviewDark() {
    RumboTheme(darkTheme = true) { ShapesPreviewContent() }
}