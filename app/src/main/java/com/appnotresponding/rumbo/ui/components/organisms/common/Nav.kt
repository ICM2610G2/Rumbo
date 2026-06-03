package com.appnotresponding.rumbo.ui.components.organisms.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import com.appnotresponding.rumbo.ui.viewModel.UnreadCountViewModel

enum class NavItem {
    Map, Chat, Plan, Itinerary
}

/**
 * Barra de navegación inferior para la aplicación Rumbo.
 * El conteo de no leídos se delega a [UnreadCountViewModel] para mantener el composable libre
 * de lógica de negocio. El color activo/inactivo se anima con [animateColorAsState].
 */
@Composable
fun Nav(
    controller: NavController,
    unreadViewModel: UnreadCountViewModel = viewModel()
) {
    val unreadCount by unreadViewModel.unreadCount.collectAsState()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val activeItem = when (currentRoute) {
        AppScreens.Map.name -> NavItem.Map
        AppScreens.Chat.name, AppScreens.ChatThread.name, AppScreens.Friends.name -> NavItem.Chat
        AppScreens.Plan.name -> NavItem.Plan
        AppScreens.Itinerary.name -> NavItem.Itinerary
        else -> NavItem.Map
    }

    Box {
        // Gradiente sutil para dar contraste con el contenido del mapa bajo la nav bar
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            MaterialTheme.colorScheme.scrim.copy(alpha = 0.75f)
                        )
                    )
                )
        )
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.padding(16.dp),
            shape = MaterialTheme.shapes.large,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItemButton(
                    label = "Mapa",
                    icon = painterResource(R.drawable.ic_map),
                    isActive = activeItem == NavItem.Map,
                    onClick = { controller.navigate(AppScreens.Map.name) }
                )
                NavItemButton(
                    label = "Chat",
                    icon = painterResource(R.drawable.ic_messages),
                    isActive = activeItem == NavItem.Chat,
                    badge = unreadCount,
                    onClick = { controller.navigate(AppScreens.Chat.name) }
                )
                NavItemButton(
                    label = "Sitios",
                    icon = painterResource(R.drawable.ic_search),
                    isActive = activeItem == NavItem.Plan,
                    onClick = { controller.navigate(AppScreens.Plan.name) }
                )
                NavItemButton(
                    label = "Itin.",
                    icon = painterResource(R.drawable.ic_list),
                    isActive = activeItem == NavItem.Itinerary,
                    onClick = { controller.navigate(AppScreens.Itinerary.name) }
                )
            }
        }
    }
}

/**
 * Botón individual de la barra de navegación.
 * El color de ícono y texto se anima suavemente al cambiar de estado activo/inactivo.
 * @param badge Número a mostrar como badge; 0 oculta el badge.
 */
@Composable
private fun NavItemButton(
    label: String,
    icon: Painter,
    isActive: Boolean,
    onClick: () -> Unit,
    badge: Int = 0,
) {
    val color by animateColorAsState(
        targetValue = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(durationMillis = 800),
        label = "navItemColor"
    )

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Icon(
                    painter = icon,
                    contentDescription = label,
                    tint = color
                )
                if (badge > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-4).dp)
                            .size(18.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (badge > 9) "9+" else badge.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
            Text(text = label, color = color)
        }
    }
}

@Preview(showBackground = true, name = "Nav - Light")
@Composable
private fun NavLightPreview() {
    RumboTheme(darkTheme = false) {
        Nav(controller = rememberNavController())
    }
}

@Preview(showBackground = true, name = "Nav - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun NavDarkPreview() {
    RumboTheme(darkTheme = true) {
        Nav(controller = rememberNavController())
    }
}
