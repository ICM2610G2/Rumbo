package com.appnotresponding.rumbo.ui.components.molecules.itinerary

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonSize
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import java.text.Normalizer
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Tarjeta de un lugar en el itinerario activo.
 * El fondo y borde se animan según si la ruta a este lugar está activa,
 * usando primaryContainer para el estado activo (lenguaje visual unificado).
 */
@Composable
fun ItineraryItemCard(p: Place, placesViewModel: PlacesViewModel, controller: NavHostController) {
    val placesState by placesViewModel.uiState.collectAsState()
    val selectedPlace = placesState.selectedPlace
    val isActiveRoute = selectedPlace?.id == p.id

    var showReplaceRouteDialog by remember { mutableStateOf(false) }

    if (showReplaceRouteDialog) {
        AlertDialog(
            onDismissRequest = { showReplaceRouteDialog = false },
            title = { Text("Ruta activa") },
            text = { Text("Tienes una ruta activa en curso. ¿Deseas reemplazarla?") },
            confirmButton = {
                TextButton(onClick = {
                    showReplaceRouteDialog = false
                    placesViewModel.selectForNavigation(p)
                    controller.navigate(AppScreens.Map.name)
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showReplaceRouteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    val containerColor by animateColorAsState(
        targetValue = if (isActiveRoute) MaterialTheme.colorScheme.primaryContainer
                      else MaterialTheme.colorScheme.surfaceContainerLow,
        animationSpec = tween(durationMillis = 300),
        label = "itineraryCardBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isActiveRoute) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.outlineVariant,
        animationSpec = tween(durationMillis = 300),
        label = "itineraryCardBorder"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isActiveRoute) 4.dp else 2.dp
        ),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(4f)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = p.imageUrl,
                        contentDescription = "Imagen de ${p.name}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        error = {
                            Image(
                                painter = painterResource(R.drawable.ic_picture),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                                contentScale = ContentScale.Crop
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(6f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = p.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f, fill = false),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (isActiveRoute) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = MaterialTheme.shapes.extraSmall
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Activa",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                    Text(
                        text = formatOpenHours(p.openHours),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RumboButton(
                    modifier = Modifier.weight(1f),
                    text = if (isActiveRoute) "Ver Ruta Activa" else "Iniciar Desplazamiento",
                    size = RumboButtonSize.Small,
                    onClick = {
                        if (isActiveRoute) {
                            controller.navigate(AppScreens.Map.name)
                        } else if (selectedPlace != null) {
                            showReplaceRouteDialog = true
                        } else {
                            placesViewModel.selectForNavigation(p)
                            controller.navigate(AppScreens.Map.name)
                        }
                    },
                    style = if (isActiveRoute) RumboButtonStyle.Primary else RumboButtonStyle.Secondary,
                    icon = painterResource(R.drawable.ic_map)
                )
                IconButton(onClick = { placesViewModel.removeFromItinerary(p) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_destroy),
                        contentDescription = "Eliminar del Itinerario",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * Construye un texto de estado de apertura para el dia y hora actuales.
 *
 * Reglas:
 * - Sin datos o sin rangos validos: devuelve un mensaje generico.
 * - Si ahora esta dentro de un rango: "Abierto hoy hasta ...".
 * - Si hay un rango mas tarde hoy: "Cerrado ahora. Abre hoy a ...".
 * - Si no hay mas rangos hoy: busca el proximo dia con apertura.
 */
fun formatOpenHours(
    openHours: List<String>?, now: LocalDateTime = LocalDateTime.now()
): String {
    if (openHours.isNullOrEmpty()) return "No hay información de horario"

    val parsedHours = openHours.mapNotNull { parseWeekdayDescription(it) }
    if (parsedHours.isEmpty()) return "No hay información de horario"

    val normalizedHours = parsedHours.associateBy { normalizeDayKey(it.first) }
    val todayKeySpanish = normalizeDayKey(dayOfWeekToSpanish(now.dayOfWeek))
    val todayRanges =
        normalizedHours[todayKeySpanish]?.second.orEmpty().mapNotNull(::parseTimeRange).sortedBy { it.first }

    val timeNow = now.toLocalTime()
    val openRange = todayRanges.firstOrNull { timeNow >= it.first && timeNow < it.second }
    if (openRange != null) return "Abierto hoy hasta ${openRange.second.format(TIME_FORMATTER)}"

    val nextToday = todayRanges.firstOrNull { timeNow < it.first }
    if (nextToday != null) return "Cerrado ahora. Abre hoy a ${nextToday.first.format(TIME_FORMATTER)}"

    val nextOpening = findNextOpening(normalizedHours, now.dayOfWeek)
    return if (nextOpening != null) {
        val (dayLabel, startTime) = nextOpening
        "Cerrado. Próxima apertura $dayLabel ${startTime.format(TIME_FORMATTER)}"
    } else {
        "Cerrado"
    }
}

/** Formato de horas esperado en los rangos (p.ej. "9:00", "17:30"). */
private val TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm")

/**
 * Parsea una linea tipo "lunes: 9:00-14:00, 16:00-20:00".
 * Retorna el dia y los rangos como texto; si dice cerrado, retorna lista vacia.
 */
private fun parseWeekdayDescription(raw: String): Pair<String, List<String>>? {
    val parts = raw.split(":", limit = 2)
    if (parts.size != 2) return null
    val day = parts[0].trim()
    val hoursText = parts[1].trim()
    if (hoursText.isEmpty()) return day to emptyList()
    val lowered = hoursText.lowercase(Locale.getDefault())
    if (lowered.contains("cerrado") || lowered.contains("closed")) return day to emptyList()
    val cleaned = hoursText.replace("–", "-")
    val ranges = cleaned.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    return day to ranges
}

/**
 * Parsea un rango de horas "HH:mm-HH:mm" y valida que el fin sea posterior.
 */
private fun parseTimeRange(raw: String): Pair<LocalTime, LocalTime>? {
    val parts = raw.split("-").map { it.trim() }
    if (parts.size != 2) return null
    val start = parseTime(parts[0]) ?: return null
    val end = parseTime(parts[1]) ?: return null
    return if (end <= start) null else start to end
}

/**
 * Intenta parsear una hora con el formato configurado.
 */
private fun parseTime(raw: String): LocalTime? = try {
    LocalTime.parse(raw, TIME_FORMATTER)
} catch (_: Exception) {
    null
}

/**
 * Busca el proximo dia (hasta 7 dias) con un rango de apertura y devuelve su inicio.
 */
private fun findNextOpening(
    normalizedHours: Map<String, Pair<String, List<String>>>, today: DayOfWeek
): Pair<String, LocalTime>? {
    for (offset in 1..7) {
        val nextDay = today.plus(offset.toLong())
        val dayLabelSpanish = dayOfWeekToSpanish(nextDay)
        val ranges = normalizedHours[normalizeDayKey(dayLabelSpanish)]?.second.orEmpty()
            .mapNotNull(::parseTimeRange).sortedBy { it.first }
        val firstRange = ranges.firstOrNull()
        if (firstRange != null) {
            return dayLabelSpanish.replaceFirstChar { it.titlecase(Locale.getDefault()) } to firstRange.first
        }
    }
    return null
}

/** Convierte un DayOfWeek a su nombre en español. */
private fun dayOfWeekToSpanish(day: DayOfWeek): String = when (day) {
    DayOfWeek.MONDAY -> "lunes"
    DayOfWeek.TUESDAY -> "martes"
    DayOfWeek.WEDNESDAY -> "miércoles"
    DayOfWeek.THURSDAY -> "jueves"
    DayOfWeek.FRIDAY -> "viernes"
    DayOfWeek.SATURDAY -> "sábado"
    DayOfWeek.SUNDAY -> "domingo"
}

/**
 * Normaliza el nombre del dia: minusculas y sin acentos (para llaves consistentes).
 */
private fun normalizeDayKey(day: String): String {
    val lowered = day.trim().lowercase(Locale.getDefault())
    val normalized = Normalizer.normalize(lowered, Normalizer.Form.NFD)
    return normalized.replace(Regex("\\p{Mn}+"), "")
}
