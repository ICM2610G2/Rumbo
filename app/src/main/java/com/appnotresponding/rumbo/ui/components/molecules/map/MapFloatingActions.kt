package com.appnotresponding.rumbo.ui.components.molecules.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Botón flotante circular para crear una nueva DropNote en el mapa.
 * Muestra un ícono de lápiz sobre un fondo circular con el color primario del tema.
 *
 * @param onClick Callback que se invoca cuando el usuario presiona el botón.
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
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_pencil),
                contentDescription = "Write Drop Note",
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

/**
 * Botón flotante circular para centrar el mapa en la ubicación actual del usuario.
 * Muestra un ícono de mira (crosshair) sobre un fondo circular con el color primario del tema.
 *
 * @param onClick Callback que se invoca cuando el usuario presiona el botón.
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
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_location_crosshairs),
                contentDescription = "Locate Me",
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun CancelRoute(onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_cancel),
                contentDescription = "Cancel Route",
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun ToggleHeatmap(isHeatmapActive: Boolean = false, onClick: () -> Unit = {}) {
    val bgColor = if (isHeatmapActive) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
    val iconTint = if (isHeatmapActive) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(bgColor), contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(R.drawable.ic_map), // Reusing map icon or any suitable icon
                contentDescription = "Toggle Heatmap",
                tint = iconTint,
            )
        }
    }
}

@Composable
fun ToggleUserRoute(isUserRouteActive: Boolean = false, onClick: () -> Unit = {}) {
    val bgColor = if (isUserRouteActive) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
    val iconTint = if (isUserRouteActive) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary
    val iconRes = if (isUserRouteActive) R.drawable.ic_eye_open else R.drawable.ic_eye_crossed
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(bgColor), contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(iconRes),
                contentDescription = "Toggle User Route",
                tint = iconTint,
            )
        }
    }
}

@Composable
fun ExpandableFAB(
    isHeatmapActive: Boolean,
    onHeatmapClick: () -> Unit,
    onDropNoteClick: () -> Unit,
    onLocateMeClick: () -> Unit,
    isUserRouteActive: Boolean,
    onUserRouteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ToggleHeatmap(isHeatmapActive = isHeatmapActive, onClick = onHeatmapClick)
                WriteDropNote(onClick = onDropNoteClick)
                LocateMe(onClick = onLocateMeClick)
                ToggleUserRoute(isUserRouteActive = isUserRouteActive, onClick = onUserRouteClick)
            }
        }

        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(if (isExpanded) R.drawable.ic_minus else R.drawable.ic_plus),
                    contentDescription = "Expand / Collapse Menu",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
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
            Box(modifier = Modifier.size(56.dp)) {
                CancelRoute()
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
            Box(modifier = Modifier.size(56.dp)) {
                CancelRoute()
            }
        }
    }
}
