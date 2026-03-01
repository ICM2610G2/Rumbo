package com.appnotresponding.rumbo.ui.components.molecules.itinerary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.ui.components.atoms.RumboButton
import com.appnotresponding.rumbo.ui.components.atoms.RumboButtonStyle
import com.appnotresponding.rumbo.ui.theme.RumboTheme


@Composable
fun ItineraryItemCard(p: Place) {

    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .weight(4f)
                .aspectRatio(1f)
                .padding(start=8.dp,end=8.dp,bottom=8.dp,top=2.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = p.imageUrl,
                contentDescription = "Imagen de ${p.name}",
                contentScale = ContentScale.Crop,
                error = {
                    Image(
                        painter = painterResource(R.drawable.ic_picture),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
                    )
                })
        }
        Column(
            modifier = Modifier.weight(6f).padding(2.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = p.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = p.openHours,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = p.price,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            RumboButton(
                text = "Iniciar Desplazamiento",
                onClick = { /* TODO */ },
                style = RumboButtonStyle.Secondary,
                icon = painterResource(R.drawable.ic_map)
            )
        }
    }

}


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
