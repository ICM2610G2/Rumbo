package com.appnotresponding.rumbo.ui.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleReview
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.components.molecules.map.LocateMe
import com.appnotresponding.rumbo.ui.components.molecules.map.WriteDropNote
import com.appnotresponding.rumbo.ui.components.organisms.common.MainTopBar
import com.appnotresponding.rumbo.ui.components.organisms.common.Nav
import com.appnotresponding.rumbo.ui.components.organisms.map.DropNoteComposer
import com.appnotresponding.rumbo.ui.components.organisms.map.PlacePreviewCard
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import com.appnotresponding.rumbo.ui.utils.SensorOverlay
import com.appnotresponding.rumbo.ui.utils.rememberLocationManager
import com.appnotresponding.rumbo.ui.utils.rememberMediaHardwareManager
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapTemplate(user: User,
                controller: NavHostController,
                onProfileClick: () -> Unit = {}) {

    var popupStateDNComposer by remember { mutableStateOf(false) }
    var popupStateReview by remember { mutableStateOf(false) }
    val locationState = rememberLocationManager()
    val mediaManager = rememberMediaHardwareManager()
    val context = LocalContext.current

    var latitude by remember { mutableDoubleStateOf(4.627293) }
    var longitude by remember { mutableDoubleStateOf(-74.063228) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 15f)
    }
    var currentMapStyle by remember { mutableIntStateOf(MapColorScheme.FOLLOW_SYSTEM) }
    val mapId = stringResource(R.string.map_id)                                                             // Controla el estilo de color del mapa (claro, oscuro o seguir el sistema) y se actualiza dinámicamente según los cambios en el sensor de luz ambiental

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = { MainTopBar(user, onProfileClick = onProfileClick) },
        floatingActionButton = {
            Column(
                modifier = Modifier
                    .height(120.dp)
                    .width(45.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                WriteDropNote {
                    popupStateDNComposer = !popupStateDNComposer
                }
                LocateMe {
                    if (locationState.hasPermission) {
                        // TODO: integrar con el mapa para centrar la camara en la ubicacion del usuario
                        Log.d("MapTemplate", "Ubicacion: ${locationState.latitude}, ${locationState.longitude}")
                    } else {
                        locationState.requestPermission()
                    }
                }
            }
        },
        bottomBar = { Nav(controller) }) { paddingValues ->
        // Main content area with the map
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding() / 2)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                contentDescription = "Mapa de Rumbo",
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false,
                    mapToolbarEnabled = false
                ),
                googleMapOptionsFactory = {
                    GoogleMapOptions().apply {
                        mapId(mapId)
                        mapType(GoogleMap.MAP_TYPE_NORMAL)

                    }
                }) {
                // https://medium.com/@ferobregon03/compose-multiplatform-displaying-and-updating-geojson-on-a-mapbox-96f025d8024a
                // https://gitee.com/coolleizhu/android-maps-compose#obtaining-access-to-the-raw-googlemap-experimental
                // https://googlemaps.github.io/android-maps-compose/maps-compose/com.google.maps.android.compose/-map-effect.html
                MapEffect(currentMapStyle) { googleMap ->
                    googleMap.mapColorScheme = currentMapStyle
                }


            }
            SensorOverlay(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )
        }
    }
    if (popupStateDNComposer) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.75f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            DropNoteComposer(
                onImageClick = { mediaManager.launchCamera() },
                imageUri = mediaManager.imageUri
            )
        }
    }
    if (popupStateReview) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .offset(y = -(90).dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            PlacePreviewCard(place = samplePlace, reviews = listOf(sampleReview))
        }
    }
}

@Preview(showBackground = true, name = "PlacePreviewCard - Light")
@Composable
private fun MapTemplateLightPreview() {
    RumboTheme(darkTheme = true) {
        MapTemplate(
            sampleUser, controller = rememberNavController()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E, name = "PlacePreviewCard - Dark")
@Composable
private fun MapTemplateDarkPreview() {
    RumboTheme(darkTheme = false) {
        MapTemplate(
            sampleUser, controller = rememberNavController()
        )
    }
}
