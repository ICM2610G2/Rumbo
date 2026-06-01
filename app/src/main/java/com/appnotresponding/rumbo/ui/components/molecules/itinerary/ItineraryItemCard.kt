package com.appnotresponding.rumbo.ui.components.molecules.itinerary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.navigation.AppScreens
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import java.text.Normalizer
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Componente que muestra la información de un lugar en el itinerario, incluyendo su imagen, nombre, horario, precio y un botón para iniciar el desplazamiento.
 *
 * @param p El objeto Place que contiene la información del lugar a mostrar.
 */
@Composable
fun ItineraryItemCard(p: Place, placesViewModel: PlacesViewModel, controller: NavHostController) {

    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .weight(4f)
                .aspectRatio(1f)
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 2.dp)
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
                })
        }
        Column(
            modifier = Modifier
                .weight(6f)
                .padding(top = 2.dp, end = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = p.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = formatOpenHours(p.openHours),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            RumboButton(
                text = "Iniciar Desplazamiento", onClick = {
                    placesViewModel.selectForNavigation(p)
                    controller.navigate(AppScreens.Map.name)
                }, style = RumboButtonStyle.Secondary, icon = painterResource(R.drawable.ic_map)
            )
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
fun formatOpenHours(openHours: List<String>?, now: LocalDateTime = LocalDateTime.now()): String {
    if (openHours.isNullOrEmpty()) {
        return "No hay información de horario"
    }

    val parsedHours = openHours.mapNotNull { parseWeekdayDescription(it) }
    if (parsedHours.isEmpty()) {
        return "No hay información de horario"
    }

    val normalizedHours = parsedHours.associateBy { normalizeDayKey(it.first) }
    val todayKeySpanish = normalizeDayKey(dayOfWeekToSpanish(now.dayOfWeek))
    val todayRanges =
        (normalizedHours[todayKeySpanish])?.second.orEmpty().mapNotNull(::parseTimeRange)
            .sortedBy { it.first }

    val timeNow = now.toLocalTime()
    val openRange = todayRanges.firstOrNull { timeNow >= it.first && timeNow < it.second }
    if (openRange != null) {
        return "Abierto hoy hasta ${openRange.second.format(TIME_FORMATTER)}"
    }

    val nextToday = todayRanges.firstOrNull { timeNow < it.first }
    if (nextToday != null) {
        return "Cerrado ahora. Abre hoy a ${nextToday.first.format(TIME_FORMATTER)}"
    }

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
    if (lowered.contains("cerrado") || lowered.contains("closed")) {
        return day to emptyList()
    }
    // Normaliza guion largo a guion simple antes de separar rangos.
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
private fun parseTime(raw: String): LocalTime? {
    try {
        return LocalTime.parse(raw, TIME_FORMATTER)
    } catch (_: Exception) {
    }
    return null
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
        val ranges = (normalizedHours[normalizeDayKey(dayLabelSpanish)])?.second.orEmpty()
            .mapNotNull(::parseTimeRange).sortedBy { it.first }
        val firstRange = ranges.firstOrNull()
        if (firstRange != null) {
            return dayLabelSpanish.replaceFirstChar { it.titlecase(Locale.getDefault()) } to firstRange.first
        }
    }
    return null
}

/** Convierte un DayOfWeek a su nombre en español, para hacer match con el array que devuelve la api. */
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

/**
@Preview(showBackground = true, name = "ItineraryItemCard - Light")
@Composable
private fun ItineraryItemCardLightPreview() {
RumboTheme(darkTheme = false) {
ItineraryItemCard(p = samplePlace)
}
}

@Preview(showBackground = true, name = "ItineraryItemCard - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun ItineraryItemCardDarkPreview() {
RumboTheme(darkTheme = true) {
ItineraryItemCard(p = samplePlace)
}
}
 */