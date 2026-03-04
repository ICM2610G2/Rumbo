package com.appnotresponding.rumbo.ui.components.organisms.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.ui.theme.RumboTheme

enum class NavItem {
    Map, Chat, Plan, Itinerary
}

/**
 * Bottom navigation bar for the Rumbo application.
 * Displays four navigation buttons (Map, Chat, Plan, Itinerary) with icons and text, highlighting the active item with the theme's primary color.
 * Navigation state is managed internally with [NavItem].
 */
@Composable
fun Nav() {
    var activeItem by remember { mutableStateOf(NavItem.Map) }
    Box {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.75f)
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
                //Map
                Button(
                    onClick = { activeItem = NavItem.Map },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_map),
                            contentDescription = "Mapa",
                            tint = if (activeItem == NavItem.Map) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Mapa",
                            color = if (activeItem == NavItem.Map) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                //Chat
                Button(
                    onClick = { activeItem = NavItem.Chat },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_messages),
                            contentDescription = "Chat",
                            tint = if (activeItem == NavItem.Chat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Chat",
                            color = if (activeItem == NavItem.Chat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                //Plan
                Button(
                    onClick = { activeItem = NavItem.Plan },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "Plan",
                            tint = if (activeItem == NavItem.Plan) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Plan",
                            color = if (activeItem == NavItem.Plan) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                //Itinerary
                Button(
                    onClick = { activeItem = NavItem.Itinerary },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_list),
                            contentDescription = "Itinerario",
                            tint = if (activeItem == NavItem.Itinerary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Itinerario",
                            color = if (activeItem == NavItem.Itinerary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Nav - Light")
@Composable
private fun NavLightPreview() {
    RumboTheme(darkTheme = false) {
        Nav()
    }
}

@Preview(showBackground = true, name = "Nav - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun NavDarkPreview() {
    RumboTheme(darkTheme = true) {
        Nav()
    }
}

