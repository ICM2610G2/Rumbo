package com.appnotresponding.rumbo.ui.components.molecules.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Base para todos los botones FAB circulares del mapa.
 * El orden clip → clickable garantiza que el ripple quede acotado al círculo.
 */
@Composable
private fun CircleFAB(
    backgroundColor: Color,
    iconRes: Int,
    contentDescription: String,
    iconTint: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = iconTint
        )
    }
}

/** Botón para escribir una DropNote en el mapa. */
@Composable
fun WriteDropNote(onClick: () -> Unit = {}) {
    CircleFAB(
        backgroundColor = MaterialTheme.colorScheme.primary,
        iconRes = R.drawable.ic_pencil,
        contentDescription = "Write Drop Note",
        iconTint = MaterialTheme.colorScheme.onPrimary,
        onClick = onClick
    )
}

/** Botón para centrar el mapa en la ubicación actual del usuario. */
@Composable
fun LocateMe(onClick: () -> Unit = {}) {
    CircleFAB(
        backgroundColor = MaterialTheme.colorScheme.primary,
        iconRes = R.drawable.ic_location_crosshairs,
        contentDescription = "Locate Me",
        iconTint = MaterialTheme.colorScheme.onPrimary,
        onClick = onClick
    )
}

/** Botón para cancelar la ruta activa. Usa color error para indicar acción destructiva. */
@Composable
fun CancelRoute(onClick: () -> Unit = {}) {
    CircleFAB(
        backgroundColor = MaterialTheme.colorScheme.error,
        iconRes = R.drawable.ic_cancel,
        contentDescription = "Cancel Route",
        iconTint = MaterialTheme.colorScheme.onError,
        onClick = onClick
    )
}

/**
 * Botón de activación del heatmap.
 * El fondo y el tinte del ícono se animan al cambiar entre activo (tertiary) e inactivo (primary).
 */
/**
 * Botón de activación del heatmap.
 * Activo: primaryContainer/onPrimaryContainer — visualmente distinto en light y dark.
 * Inactivo: primary/onPrimary — igual al resto de FABs.
 * (tertiary == primary en este tema, por eso se usa primaryContainer como estado activo)
 */
@Composable
fun ToggleHeatmap(isHeatmapActive: Boolean = false, onClick: () -> Unit = {}) {
    val bgColor by animateColorAsState(
        targetValue = if (isHeatmapActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
        animationSpec = tween(durationMillis = 300),
        label = "heatmapBgColor"
    )
    val iconTint by animateColorAsState(
        targetValue = if (isHeatmapActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
        animationSpec = tween(durationMillis = 300),
        label = "heatmapIconTint"
    )
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(bgColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.ic_heat),
            contentDescription = "Toggle Heatmap",
            tint = iconTint
        )
    }
}

/**
 * Botón de visibilidad de la ubicación del usuario.
 * Activo: primaryContainer/onPrimaryContainer — mismo lenguaje que ToggleHeatmap y las cards activas.
 */
@Composable
fun ToggleUserVisibility(isUserVisible: Boolean = false, onClick: () -> Unit = {}) {
    val bgColor by animateColorAsState(
        targetValue = if (isUserVisible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
        animationSpec = tween(durationMillis = 300),
        label = "userRouteBgColor"
    )
    val iconTint by animateColorAsState(
        targetValue = if (isUserVisible) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
        animationSpec = tween(durationMillis = 300),
        label = "userRouteIconTint"
    )
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(bgColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(if (isUserVisible) R.drawable.ic_eye_open else R.drawable.ic_eye_crossed),
            contentDescription = "Toggle User Route",
            tint = iconTint
        )
    }
}

/**
 * Menú FAB expandible del mapa. Los items aparecen con slide + fade en 300ms
 * para una animación natural sin rebote. El botón principal alterna el ícono +/-.
 */
@Composable
fun ExpandableFAB(
    isHeatmapActive: Boolean,
    onHeatmapClick: () -> Unit,
    onDropNoteClick: () -> Unit,
    onLocateMeClick: () -> Unit,
    isUserVisible: Boolean,
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
            enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it },
            exit = fadeOut(tween(250)) + slideOutVertically(tween(250)) { it }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ToggleHeatmap(isHeatmapActive = isHeatmapActive, onClick = onHeatmapClick)
                ToggleUserVisibility(isUserVisible = isUserVisible, onClick = onUserRouteClick)
                LocateMe(onClick = onLocateMeClick)
                WriteDropNote(onClick = onDropNoteClick)
            }
        }

        CircleFAB(
            backgroundColor = MaterialTheme.colorScheme.primary,
            iconRes = if (isExpanded) R.drawable.ic_minus else R.drawable.ic_plus,
            contentDescription = "Expand / Collapse Menu",
            iconTint = MaterialTheme.colorScheme.onPrimary,
            onClick = { isExpanded = !isExpanded }
        )
    }
}

@Preview(showBackground = true, name = "MapFloatingActions - Light")
@Composable
private fun MapFloatingActionsLightPreview() {
    RumboTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.size(56.dp)) { WriteDropNote() }
            Box(modifier = Modifier.size(56.dp)) { LocateMe() }
            Box(modifier = Modifier.size(56.dp)) { CancelRoute() }
            Box(modifier = Modifier.size(56.dp)) { ToggleHeatmap(isHeatmapActive = false) }
            Box(modifier = Modifier.size(56.dp)) { ToggleHeatmap(isHeatmapActive = true) }
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
            Box(modifier = Modifier.size(56.dp)) { WriteDropNote() }
            Box(modifier = Modifier.size(56.dp)) { LocateMe() }
            Box(modifier = Modifier.size(56.dp)) { CancelRoute() }
            Box(modifier = Modifier.size(56.dp)) { ToggleUserVisibility(isUserVisible = true) }
        }
    }
}
