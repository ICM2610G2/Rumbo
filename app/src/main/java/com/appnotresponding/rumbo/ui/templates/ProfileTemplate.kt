package com.appnotresponding.rumbo.ui.templates

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.DropNote
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.VisitedPlace
import com.appnotresponding.rumbo.ui.components.atoms.Avatar
import com.appnotresponding.rumbo.ui.components.atoms.AvatarSize
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthPhoneText
import com.appnotresponding.rumbo.ui.components.molecules.auth.AuthPlainText
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class ProfileMenu {
    EditData, ItineraryHistory, Memories
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTemplate(
    user: User,
    itineraryHistory: Map<String, List<VisitedPlace>>,
    dropNotes: List<DropNote>,
    selectedPhotoUri: Uri?,
    isSavingProfile: Boolean,
    profileError: String,
    profileSuccess: String,
    controller: androidx.navigation.NavHostController,
    onBackClick: () -> Unit,
    onPickPhoto: () -> Unit,
    onSaveProfile: (name: String, lastname: String, phone: String) -> Unit,
    onSignOut: () -> Unit
) {
    var selectedMenu by remember { mutableStateOf(ProfileMenu.EditData) }
    var name by remember(user.id, user.name) { mutableStateOf(user.name) }
    var lastname by remember(user.id, user.lastname) { mutableStateOf(user.lastname) }
    var phone by remember(user.id, user.phone) { mutableStateOf(user.phone) }

    LaunchedEffect(user.id, user.name, user.lastname, user.phone) {
        name = user.name
        lastname = user.lastname
        phone = user.phone
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            ProfileHeader(
                user = user,
                selectedPhotoUri = selectedPhotoUri,
                onBackClick = onBackClick,
                onPickPhoto = onPickPhoto,
                onSignOut = onSignOut
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileMenu.entries.forEach { menu ->
                    FilterChip(
                        shape = MaterialTheme.shapes.large,
                        selected = selectedMenu == menu,
                        onClick = { selectedMenu = menu },
                        label = {
                            Text(
                                text = when (menu) {
                                    ProfileMenu.EditData -> "Datos"
                                    ProfileMenu.ItineraryHistory -> "Historial"
                                    ProfileMenu.Memories -> "Recuerdos"
                                }
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = when (menu) {
                                    ProfileMenu.EditData -> painterResource(R.drawable.ic_user)
                                    ProfileMenu.ItineraryHistory -> painterResource(R.drawable.ic_list)
                                    ProfileMenu.Memories -> painterResource(R.drawable.ic_recuerdos)
                                }, contentDescription = null, modifier = Modifier.size(18.dp)
                            )
                        })
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            ) {
                when (selectedMenu) {
                    ProfileMenu.EditData -> EditProfileSection(
                        name = name,
                        lastname = lastname,
                        phone = phone,
                        onNameChange = { name = it },
                        onLastnameChange = { lastname = it },
                        onPhoneChange = { phone = it },
                        isSaving = isSavingProfile,
                        profileError = profileError,
                        profileSuccess = profileSuccess,
                        onSave = { onSaveProfile(name, lastname, phone) })

                    ProfileMenu.ItineraryHistory -> ItineraryHistorySection(itineraryHistory)
                    ProfileMenu.Memories -> MemoriesSection(dropNotes)
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    user: User,
    selectedPhotoUri: Uri?,
    onBackClick: () -> Unit,
    onPickPhoto: () -> Unit,
    onSignOut: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow, shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(painter = painterResource(R.drawable.ic_arrow_left), contentDescription = "Volver")
                }
                IconButton(onClick = onSignOut) {
                    Icon(painter = painterResource(R.drawable.ic_logout), contentDescription = "Cerrar sesión")
                }
            }

            Box(contentAlignment = Alignment.BottomEnd) {
                if (selectedPhotoUri != null) {
                    AsyncImage(
                        model = selectedPhotoUri,
                        contentDescription = "Foto de perfil seleccionada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Avatar(user = user, size = AvatarSize.Large, modifier = Modifier.size(96.dp))
                }
                Surface(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onPickPhoto),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.ic_camera),
                            contentDescription = "Cambiar foto",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Text(
                text = "${user.name} ${user.lastname}".trim().ifBlank { "Perfil" },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EditProfileSection(
    name: String,
    lastname: String,
    phone: String,
    onNameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    isSaving: Boolean,
    profileError: String,
    profileSuccess: String,
    onSave: () -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Editar datos personales",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            AuthPlainText(
                value = name,
                onValueChange = onNameChange,
                label = "Nombres",
                placeholder = "Tu nombre"
            )
            AuthPlainText(
                value = lastname,
                onValueChange = onLastnameChange,
                label = "Apellidos",
                placeholder = "Tus apellidos"
            )
            AuthPhoneText(value = phone, onValueChange = onPhoneChange)
            if (profileError.isNotBlank()) {
                Text(
                    text = profileError,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            if (profileSuccess.isNotBlank()) {
                Text(
                    text = profileSuccess,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            RumboButton(
                text = "Guardar cambios",
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                loading = isSaving,
                enabled = name.isNotBlank() && lastname.isNotBlank() && Regex("^\\+\\d{10,14}$").matches(
                    phone
                )
            )
        }
    }
}

@Composable
private fun ItineraryHistorySection(
    itineraryHistory: Map<String, List<VisitedPlace>>
) {
    if (itineraryHistory.isEmpty()) {
        EmptyState(text = "Aún no hay lugares visitados.")
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        itineraryHistory.toSortedMap(compareByDescending { it }).forEach { (day, places) ->
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatDateHeader(day),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        val placeCount = places.size
                        Text(
                            text = "$placeCount lugar${if (placeCount > 1) "es" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    places.forEachIndexed { index, place ->
                        VisitedPlaceRow(place)
                        if (index < places.size - 1) {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

private fun formatDateHeader(day: String): String {
    return try {
        val date = java.time.LocalDate.parse(day)
        val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", java.util.Locale("es", "ES"))
        date.format(formatter)
    } catch (e: Exception) {
        day
    }
}

@Composable
private fun VisitedPlaceRow(place: VisitedPlace) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = place.imageUrl,
                contentDescription = "Imagen de ${place.placeName}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = {
                    Image(
                        painter = painterResource(R.drawable.ic_picture),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                        modifier = Modifier.padding(16.dp)
                    )
                })
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = place.placeName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = place.address.ifBlank { "Sin dirección" },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatTimestamp(place.visitedAt),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun MemoriesSection(dropNotes: List<DropNote>) {
    if (dropNotes.isEmpty()) {
        EmptyState(text = "Aún no has creado DropNotes.")
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        dropNotes.forEach { note ->
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = formatTimestamp(note.timestamp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (note.content.isNotBlank()) {
                        Text(
                            text = note.content,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (!note.imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = note.imageUrl,
                            contentDescription = "Imagen del DropNote",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(MaterialTheme.shapes.medium)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(text: String) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    if (timestamp <= 0L) return "Sin fecha"
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).format(formatter)
}
