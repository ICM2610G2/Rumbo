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
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.navigation.AppScreens
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
fun MainTopBar(u: User, controller: NavHostController) {
    val bottomRoundedShape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
    val displayName = u.name.replace(Regex(" +$"), "")
    Surface(
        shape = bottomRoundedShape, color = MaterialTheme.colorScheme.surfaceContainerLow,
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
    isOnline: Boolean = false,
    onMuteClick: (() -> Unit)? = null,
    onLeaveClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null
) {
    val bottomRoundedShape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
    val displayName = u.name.replace(Regex(" +$"), "")

    val subtitle: String? = when {
        !activity.isNullOrBlank() -> activity
        !isGroup && isOnline -> "En línea"
        else -> null
    }
    val subtitleColor = if (!isGroup && isOnline)
        androidx.compose.ui.graphics.Color(0xFF4CAF50)
    else
        MaterialTheme.colorScheme.primary

    Surface(
        shape = bottomRoundedShape, color = MaterialTheme.colorScheme.surfaceContainerLow,
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

/*
@Preview(showBackground = true, name = "MainTopBar - Light")
@Composable
private fun MainTopBarLightPreview() {
    RumboTheme(darkTheme = false) {
        MainTopBar(u = sampleUser,)
    }
}

@Preview(showBackground = true, name = "MainTopBar - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun MainTopBarDarkPreview() {
    RumboTheme(darkTheme = true) {
        MainTopBar(u = sampleUser,)
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
}*/
