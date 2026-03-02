package com.appnotresponding.rumbo.ui.components.molecules.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
 * Circular floating button to create a new DropNote on the map.
 * Displays a pencil icon over a circular background with the theme's primary color.
 *
 * @param onClick Callback invoked when the user presses the button.
 */
@Composable
fun WriteDropNote(onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(R.drawable.ic_pencil),
                contentDescription = "Write Drop Note",
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

/**
 * Circular floating button to center the map on the user's current location.
 * Displays a crosshair icon over a circular background with the theme's primary color.
 *
 * @param onClick Callback invoked when the user presses the button.
 */
@Composable
fun LocateMe(onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(R.drawable.ic_location_crosshairs),
                contentDescription = "Locate Me",
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Preview(showBackground = true, name = "MapFloatingActions - Light")
@Composable
private fun MapFloatingActionsLightPreview() {
    RumboTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.size(56.dp)) {
                WriteDropNote()
            }
            Box(modifier = Modifier.size(56.dp)) {
                LocateMe()
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E, name = "MapFloatingActions - Dark")
@Composable
private fun MapFloatingActionsDarkPreview() {
    RumboTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.size(56.dp)) {
                WriteDropNote()
            }
            Box(modifier = Modifier.size(56.dp)) {
                LocateMe()
            }
        }
    }
}
