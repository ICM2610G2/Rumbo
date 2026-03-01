package com.appnotresponding.rumbo.ui.components.molecules.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LocationHeader(
    locationName: String = "Desconocida"
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Planea Tu Día",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
        )
        Text(
            text = "Tu Ubicación Actual: $locationName",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}

@Composable
fun DayHeader(
) {
    var date by remember {
        mutableStateOf(
            SimpleDateFormat(
                "EEEE, d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-ES")
            ).format(Date())
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Planea Tu Día",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
        )
        Text(
            text = date,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}


@Preview(showBackground = true, name = "DayHeader - Light")
@Composable
private fun DayHeaderLightPreview() {
    RumboTheme(darkTheme = false) {
        DayHeader()
    }
}

@Preview(showBackground = true, name = "DayHeader - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun DayHeaderDarkPreview() {
    RumboTheme(darkTheme = true) {
        DayHeader()
    }
}

@Preview(showBackground = true, name = "LocationHeader - Light")
@Composable
private fun DayHeaderDefaultLightPreview() {
    RumboTheme(darkTheme = false) {
        LocationHeader("Bogotá, Colombia")
    }
}

@Preview(showBackground = true, name = "LocationHeader - Dark", backgroundColor = 0xFF1E1E1E)
@Composable
private fun DayHeaderDefaultDarkPreview() {
    RumboTheme(darkTheme = true) {
        LocationHeader("Bogotá, Colombia")
    }
}

