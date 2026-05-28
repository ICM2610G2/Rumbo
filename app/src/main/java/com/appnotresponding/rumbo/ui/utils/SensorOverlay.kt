package com.appnotresponding.rumbo.ui.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation

@Composable
fun SensorOverlay(modifier: Modifier = Modifier) {
    val accelerometerState = rememberAccelerometerManager()
    val compassState = rememberCompassManager()

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Indicador de movimiento con el acelerometro
        CompassWidget(
            degrees = compassState.degrees,
            direction = compassState.direction
        )
    }
}

@Composable
fun CompassWidget(
    degrees: Float,
    direction: String,
    modifier: Modifier = Modifier
) {

    val animatedRotation by animateFloatAsState(
        targetValue = degrees,
        label = "CompassRotation"
    )
    Spacer(modifier = Modifier.height(30.dp))
    Box(
        modifier = modifier
            .size(82.dp)
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                CircleShape
            )
            .border(
                1.5.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        // Norte
        Text(
            text = "N",
            modifier = Modifier.offset(y = (-28).dp),
            color = Color.Red,
            style = MaterialTheme.typography.labelSmall
        )

        // Sur
        Text(
            text = "S",
            modifier = Modifier.offset(y = (28).dp),
            style = MaterialTheme.typography.labelSmall
        )

        // Este
        Text(
            text = "E",
            modifier = Modifier.offset(x = (28).dp),
            style = MaterialTheme.typography.labelSmall
        )

        // Oeste
        Text(
            text = "O",
            modifier = Modifier.offset(x = (-28).dp),
            style = MaterialTheme.typography.labelSmall
        )

        // Flecha
        Icon(
            imageVector = Icons.Default.Navigation,
            contentDescription = "Compass Arrow",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(36.dp)
                .rotate(-animatedRotation)
        )

        // Punto central
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    CircleShape
                )
        )
    }
}

/**
@Composable
fun CompassWidget(
    degrees: Float,
    direction: String,
    modifier: Modifier = Modifier
) {

    val animatedRotation by animateFloatAsState(
        targetValue = degrees,
        label = "CompassRotation"
    )

    Box(
        modifier = modifier
            .size(140.dp)
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                CircleShape
            )
            .border(
                2.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        // Rosa de los vientos
        Text(
            text = "N",
            modifier = Modifier.offset(y = (-50).dp),
            color = Color.Red,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "S",
            modifier = Modifier.offset(y = (50).dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "E",
            modifier = Modifier.offset(x = (50).dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "O",
            modifier = Modifier.offset(x = (-50).dp),
            style = MaterialTheme.typography.bodyMedium
        )

        // Flecha
        Icon(
            Icons.Default.Navigation,
            contentDescription = "Compass Arrow",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(70.dp)
                .rotate(-animatedRotation)
        )

        // Centro
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    CircleShape
                )
        )
    }
}
        */