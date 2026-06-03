package com.appnotresponding.rumbo.ui.components.organisms.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.atoms.Avatar
import com.appnotresponding.rumbo.ui.theme.RumboTheme

private val OnlineGreen = Color(0xFF4CAF50)

/**
 * Barra superior principal para la aplicación Rumbo.
 * Muestra el logotipo de la aplicación a la izquierda y un saludo con el avatar del usuario a la derecha.
 */
@Composable
fun MainTopBar(u: User, controller: NavHostController) {
    val displayName = u.name.replace(Regex(" +$"), "")
    Surface(
        shape = MaterialTheme.shapes.large.copy(
            topStart = androidx.compose.foundation.shape.CornerSize(0.dp),
            topEnd = androidx.compose.foundation.shape.CornerSize(0.dp)
        ),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp),
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
                Avatar(modifier = Modifier.clickable(onClick = {
                    controller.navigate(AppScreens.Profile.name)
                }), user = u)
            }
        }
    }
}

/**
 * Barra superior para la pantalla de chat individual.
 * Muestra el avatar del contacto junto con su nombre y, opcionalmente,
 * su actividad actual (por ejemplo, "Rumbo al Museo Nacional").
 * El estado online se indica con [OnlineGreen], un verde semántico de presencia.
 */
@Composable
fun ChatTopBar(
    u: User,
    activity: String? = null,
    isGroup: Boolean = false,
    isMuted: Boolean = false,
    isOnline: Boolean = false,
    onMuteClick: (() -> Unit)? = null,
    onLeaveClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null
) {
    val displayName = u.name.replace(Regex(" +$"), "")

    val subtitle: String? = when {
        !activity.isNullOrBlank() -> activity
        !isGroup && isOnline -> "En línea"
        else -> null
    }
    val subtitleColor = if (!isGroup && isOnline) OnlineGreen else MaterialTheme.colorScheme.primary

    Surface(
        shape = MaterialTheme.shapes.large.copy(
            topStart = androidx.compose.foundation.shape.CornerSize(0.dp),
            topEnd = androidx.compose.foundation.shape.CornerSize(0.dp)
        ),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBackClick != null) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = "Atrás",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Avatar(user = u, isOnline = isOnline)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelMedium,
                            color = subtitleColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
