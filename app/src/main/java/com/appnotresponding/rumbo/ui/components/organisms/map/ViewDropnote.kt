package com.appnotresponding.rumbo.ui.components.organisms.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.components.atoms.Avatar
import com.appnotresponding.rumbo.ui.components.atoms.AvatarSize
import com.appnotresponding.rumbo.ui.theme.RumboTheme

import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.appnotresponding.rumbo.R

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val now = Calendar.getInstance()
    val timeCalendar = Calendar.getInstance().apply { timeInMillis = timestamp }

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    return when {
        now.get(Calendar.YEAR) == timeCalendar.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == timeCalendar.get(Calendar.DAY_OF_YEAR) -> {
            "Hoy ${timeFormat.format(date)}"
        }
        now.get(Calendar.YEAR) == timeCalendar.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) - timeCalendar.get(Calendar.DAY_OF_YEAR) == 1 -> {
            "Ayer ${timeFormat.format(date)}"
        }
        else -> {
            dateFormat.format(date)
        }
    }
}

@Composable
fun ViewDropNote(
    modifier: Modifier = Modifier,
    user: User,
    content: String = "",
    imageUrl: String? = null,
    isPrivate: Boolean = false,
    timestamp: Long = 0L,
    showDeleteOption: Boolean = false,
    onDeleteClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar(user = user, size = AvatarSize.Medium)

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (timestamp > 0L) {
                            Text(
                                text = formatTimestamp(timestamp),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                // Botón delete solo si aplica
                if (showDeleteOption) {
                    var showConfirmDialog by remember { mutableStateOf(false) }

                    IconButton(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_destroy),
                            contentDescription = "Eliminar DropNote",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }

                    if (showConfirmDialog) {
                        AlertDialog(
                            onDismissRequest = { showConfirmDialog = false },
                            title = {
                                Text(
                                    text = "¿Eliminar DropNote?",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            },
                            text = {
                                Text(
                                    text = "¿Estás seguro de que quieres eliminar esta nota? Esta acción no se puede deshacer.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    showConfirmDialog = false
                                    onDeleteClick()
                                }) {
                                    Text(
                                        text = "Eliminar",
                                        color = MaterialTheme.colorScheme.error,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showConfirmDialog = false }) {
                                    Text(
                                        text = "Cancelar",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!imageUrl.isNullOrEmpty()) {
                var isImageExpanded by remember { mutableStateOf(false) }

                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Imagen de la DropNote",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { isImageExpanded = true },
                    contentScale = ContentScale.Crop
                )

                if (isImageExpanded) {
                    Dialog(onDismissRequest = { isImageExpanded = false }) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Imagen ampliada",
                            modifier = Modifier
                                .fillMaxWidth().clip(RoundedCornerShape(12.dp))
                                .clickable { isImageExpanded = false },
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "DropNote Real"
)
@Composable
private fun ViewDropNoteRealPreview() {
    RumboTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ViewDropNote(
                user = sampleUser,
                content = "Esta es una DropNote real con imagen, fecha y acciones.",
                imageUrl = " ",
                timestamp = System.currentTimeMillis(),
                showDeleteOption = true
            )
        }
    }
}