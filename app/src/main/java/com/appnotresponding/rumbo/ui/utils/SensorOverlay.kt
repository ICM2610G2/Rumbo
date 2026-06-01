package com.appnotresponding.rumbo.ui.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.ui.tooling.preview.Preview
import com.appnotresponding.rumbo.ui.theme.RumboTheme

@Composable
fun SensorOverlay(modifier: Modifier = Modifier) {
    val accelerometerState = rememberAccelerometerManager()
    val compassState = rememberCompassManager()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Indicador de movimiento con el acelerometro
        CompassWidget(
            degrees = compassState.degrees,
        )
    }
}

@Composable
fun CompassWidget(
    degrees: Float,
    modifier: Modifier = Modifier
) {

    val animatedRotation by animateFloatAsState(
        targetValue = degrees,
        label = "CompassRotation"
    )
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .shadow(4.dp, CircleShape)
                .size(48.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f),
                    CircleShape
                )
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            // Norte
            Text(
                text = "N",
                modifier = Modifier.offset(y = (-16).dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall
            )

            // Sur
            Text(
                text = "S",
                modifier = Modifier.offset(y = (16).dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Este
            Text(
                text = "E",
                modifier = Modifier.offset(x = (16).dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Oeste
            Text(
                text = "O",
                modifier = Modifier.offset(x = (-16).dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Flecha
            Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = "Compass Arrow",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(animatedRotation)
            )

            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CompassWidgetPreview() {
    RumboTheme() {
        CompassWidget(
            degrees = 45f
        )
    }
}
