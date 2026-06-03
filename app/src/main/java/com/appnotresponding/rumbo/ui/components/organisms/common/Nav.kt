package com.appnotresponding.rumbo.ui.components.organisms.common

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

enum class NavItem {
    Map, Chat, Plan, Itinerary
}

/**
 * Barra de navegación inferior para la aplicación Rumbo.
 * Muestra cuatro botones de navegación (Mapa, Chat, Plan, Itinerario) con íconos y texto,
 * resaltando el elemento activo con el color primario del tema.
 * El estado de navegación se gestiona internamente con [NavItem].
 */
@Composable
fun Nav(
    controller: NavController
) {
    var unreadCount by remember { mutableIntStateOf(0) }
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val activeItem = when (currentRoute) {
        AppScreens.Map.name -> NavItem.Map
        AppScreens.Chat.name, AppScreens.ChatThread.name, AppScreens.Friends.name -> NavItem.Chat
        AppScreens.Plan.name -> NavItem.Plan
        AppScreens.Itinerary.name -> NavItem.Itinerary
        else -> NavItem.Map
    }

    DisposableEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            onDispose {}
        } else {
            val db = FirebaseDatabase.getInstance()
            val directRef = db.getReference("chats")
            val groupRef = db.getReference("groupChats")
            var directUnread = 0
            var groupUnread = 0

            fun readUnread(snapshot: DataSnapshot): Int {
                return snapshot.children.sumOf { child ->
                    child.child("unreadCounts").child(uid).getValue(Int::class.java)
                        ?: child.child("unreadCounts").child(uid).getValue(Long::class.java)?.toInt()
                        ?: 0
                }
            }

            val directListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    directUnread = readUnread(snapshot)
                    unreadCount = directUnread + groupUnread
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            val groupListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    groupUnread = readUnread(snapshot)
                    unreadCount = directUnread + groupUnread
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            directRef.addValueEventListener(directListener)
            groupRef.addValueEventListener(groupListener)
            onDispose {
                directRef.removeEventListener(directListener)
                groupRef.removeEventListener(groupListener)
            }
        }
    }
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
                    onClick = {
                        controller.navigate(AppScreens.Map.name)
                    },
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
                    onClick = {
                        controller.navigate(AppScreens.Chat.name)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box {
                                Icon(
                                    painter = painterResource(R.drawable.ic_messages),
                                    contentDescription = "Chat",
                                    tint = if (activeItem == NavItem.Chat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                if (unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = (8).dp, y = (-4).dp)
                                            .size(18.dp)
                                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Chat",
                                color = if (activeItem == NavItem.Chat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                //Plan
                Button(
                    onClick = {
                        controller.navigate(AppScreens.Plan.name)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_search),
                                contentDescription = "Places",
                                tint = if (activeItem == NavItem.Plan) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Sitios",
                                color = if (activeItem == NavItem.Plan) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                //Itinerary
                Button(
                    onClick = {
                        controller.navigate(AppScreens.Itinerary.name)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
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
                                text = "Itin.",
                                color = if (activeItem == NavItem.Itinerary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
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

