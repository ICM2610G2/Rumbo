package com.appnotresponding.rumbo.ui.components.organisms.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.components.atoms.Avatar
import com.appnotresponding.rumbo.ui.theme.RumboTheme

/**
 * Barra superior principal para la aplicación Rumbo.
 * Muestra el logotipo de la aplicación a la izquierda y un saludo con el avatar del usuario a la derecha.
 *
 * @param u El objeto [User] del usuario actual, utilizado para obtener el nombre
 * y la foto de perfil para el avatar.
 */
@Composable
fun MainTopBar(u: User, onProfileClick: () -> Unit = {}) {
    val bottomRoundedShape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
    val displayName = u.name.replace(Regex(" +$"), "")
    Surface(
        shape = bottomRoundedShape, color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.mipmap.img_logo_top),
                contentDescription = null,
                modifier = Modifier.height(20.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¡Hola, ${displayName}!",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Avatar(modifier = Modifier.clickable(onClick = onProfileClick), user = u)
            }
        }
    }
}

/**
 * Barra superior para la pantalla de chat individual.
 * Muestra el avatar del contacto junto con su nombre y, opcionalmente,
 * su actividad actual (por ejemplo, "Rumbo al Museo Nacional").
 *
 * @param u El objeto [User] del contacto con el que se está chateando,
 * utilizado para obtener el nombre y la foto de perfil.
 * @param activity Texto opcional que describe la actividad actual del usuario
 * (por ejemplo, "Rumbo al Museo Nacional").
 */
@Composable
fun ChatTopBar(
    u: User, 
    activity: String? = null,
    isGroup: Boolean = false,
    isMuted: Boolean = false,
    onMuteClick: (() -> Unit)? = null,
    onLeaveClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null
) {
    val bottomRoundedShape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
    val displayName = u.name.replace(Regex(" +$"), "")
    Surface(
        shape = bottomRoundedShape, color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onBackClick != null) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Avatar(user = u)
                Column {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (!activity.isNullOrBlank()) {
                        Text(
                            text = activity,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            if (isGroup) {
                Row {
                    if (onMuteClick != null) {
                        IconButton(onClick = onMuteClick) {
                            Icon(
                                imageVector = if (isMuted) Icons.Filled.NotificationsOff else Icons.Filled.Notifications,
                                contentDescription = if (isMuted) "Desilenciar" else "Silenciar",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (onLeaveClick != null) {
                        IconButton(onClick = onLeaveClick) {
                            Icon(
                                imageVector = Icons.Filled.ExitToApp,
                                contentDescription = "Salir",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "MainTopBar - Light")
@Composable
private fun MainTopBarLightPreview() {
    RumboTheme(darkTheme = false) {
        MainTopBar(u = sampleUser)
    }
}

@Preview(showBackground = true, name = "MainTopBar - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun MainTopBarDarkPreview() {
    RumboTheme(darkTheme = true) {
        MainTopBar(u = sampleUser)
    }
}

@Preview(showBackground = true, name = "ChatTopBar - Light")
@Composable
private fun ChatTopBarLightPreview() {
    RumboTheme(darkTheme = false) {
        ChatTopBar(u = sampleUser, activity = "Rumbo al Museo Nacional")
    }
}

@Preview(showBackground = true, name = "ChatTopBar - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun ChatTopBarDarkPreview() {
    RumboTheme(darkTheme = true) {
        ChatTopBar(u = sampleUser, activity = "Rumbo al Museo Nacional")
    }
}
