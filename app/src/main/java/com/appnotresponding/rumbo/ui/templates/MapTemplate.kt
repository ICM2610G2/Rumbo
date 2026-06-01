package com.appnotresponding.rumbo.ui.templates

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.isDarkTheme
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleReview
import com.appnotresponding.rumbo.roadManager
import com.appnotresponding.rumbo.ui.components.atoms.Avatar
import com.appnotresponding.rumbo.ui.components.atoms.AvatarSize
import com.appnotresponding.rumbo.ui.components.atoms.UserProfileBubble
import com.appnotresponding.rumbo.ui.components.molecules.map.CancelRoute
import com.appnotresponding.rumbo.ui.components.molecules.map.LocateMe
import com.appnotresponding.rumbo.ui.components.molecules.map.WriteDropNote
import com.appnotresponding.rumbo.ui.components.organisms.common.MainTopBar
import com.appnotresponding.rumbo.ui.components.organisms.common.Nav
import com.appnotresponding.rumbo.ui.components.organisms.map.DropNoteComposer
import com.appnotresponding.rumbo.ui.components.organisms.map.PlacePreviewCard
import com.appnotresponding.rumbo.ui.utils.SensorOverlay
import com.appnotresponding.rumbo.ui.utils.createLocationRequest
import com.appnotresponding.rumbo.ui.utils.rememberLocationManager
import com.appnotresponding.rumbo.ui.utils.rememberMediaHardwareManager
import com.appnotresponding.rumbo.ui.viewModel.MapViewModel
import com.appnotresponding.rumbo.ui.viewModel.PlacesViewModel
import com.appnotresponding.rumbo.ui.viewModel.UserLocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import org.osmdroid.util.GeoPoint


val locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
var locationRequest: LocationRequest = createLocationRequest()

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapTemplate(
    user: User,
    controller: NavHostController,
    onProfileClick: () -> Unit = {},
    viewModel: MapViewModel = viewModel(),
    placesViewModel: PlacesViewModel,
    locationViewModel: UserLocationViewModel
) {
    Log.d("RECOMPOSE", "MapTemplate recomposed")

    val context = LocalContext.current

    val state by viewModel.uiState.collectAsState()
    val userLocationState by locationViewModel.uiState.collectAsState()
    val placesState by placesViewModel.uiState.collectAsState()

    var popupStateDNComposer by remember { mutableStateOf(false) }
    var popupStateReview by remember { mutableStateOf(false) }
    val locationState = rememberLocationManager()
    val mediaManager = rememberMediaHardwareManager()
    var noteText by remember { mutableStateOf("") }
    val markerKey = remember(user.profilePictureUrl) { user.profilePictureUrl ?: "" }
    var profileBitmap by remember(user.profilePictureUrl) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(user.profilePictureUrl) {
        if (user.profilePictureUrl.isNullOrEmpty()) return@LaunchedEffect
        val request = ImageRequest.Builder(context)
            .data(user.profilePictureUrl)
            .allowHardware(false)
            .build()
        val result = ImageLoader(context).execute(request)
        if (result is SuccessResult) {
            profileBitmap = result.image.toBitmap().asImageBitmap()
        }
    }

    var latitude by remember { mutableDoubleStateOf(4.627293) }
    var longitude by remember { mutableDoubleStateOf(-74.063228) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 15f)
    }
    var currentMapStyle by remember { mutableIntStateOf(MapColorScheme.FOLLOW_SYSTEM) }
    val mapId =
        stringResource(R.string.map_id)

    var permission = rememberPermissionState(locationPermission)
    var showButton by remember { mutableStateOf(false) }
    SideEffect {
        if (!permission.status.isGranted) {
            if (permission.status.shouldShowRationale) {
                showButton = true
            } else {
                showButton = false
                permission.launchPermissionRequest()
            }
        }
    }

    if (permission.status.isGranted) {
        if (!locationViewModel.permissionGranted) locationViewModel.updateVel()
    }

    LaunchedEffect(
        userLocationState.latitude,
        userLocationState.longitude,
        state.centerInUserFirstTime
    ) {
        Log.d("RECOMPOSE", "Enntrando en launch")
        val tieneUbicacionReal = userLocationState.latitude != 0.0 || userLocationState.longitude != 0.0

        if (tieneUbicacionReal) {
            viewModel.updateUserMarker(userLocationState.latitude, userLocationState.longitude)
        }

        if (state.centerInUserFirstTime) {
            if (tieneUbicacionReal && placesState.selectedPlace == null) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(userLocationState.latitude, userLocationState.longitude), 18f
                )
                viewModel.updateCenterInUserFirstTime()
            } else if (tieneUbicacionReal && placesState.selectedPlace != null) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(
                        placesState.selectedPlace!!.latitude,
                        placesState.selectedPlace!!.longitude
                    ), 14f
                )
                viewModel.updateCenterInUserFirstTime()
            }
            // Si aún no hay ubicación real, la cámara se queda en Bogotá (valor inicial)
        }

        if (tieneUbicacionReal && placesState.selectedPlace != null) {
            val startPoint = GeoPoint(userLocationState.latitude, userLocationState.longitude)
            val destination = GeoPoint(
                placesState.selectedPlace!!.latitude, placesState.selectedPlace!!.longitude
            )
            val points = arrayListOf(startPoint, destination)
            val road = roadManager.getRoad(points)
            val routePoints = road.mRouteHigh.map { geoPoint ->
                LatLng(geoPoint.latitude, geoPoint.longitude)
            }
            viewModel.updateRoutePoints(routePoints)
        }


        if (placesState.selectedPlace != null) {
            viewModel.updateAdditionalMarker(
                LatLng(
                    placesState.selectedPlace!!.latitude, placesState.selectedPlace!!.longitude
                ), placesState.selectedPlace!!.name
            )
            GeoPoint(
                placesState.selectedPlace!!.latitude, placesState.selectedPlace!!.longitude
            )
        }
    }

    LaunchedEffect(isDarkTheme) {
        currentMapStyle = if (isDarkTheme) MapColorScheme.DARK else MapColorScheme.LIGHT
    }


    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = { MainTopBar(user, onProfileClick = onProfileClick) },
        floatingActionButton = {
            Column(
                modifier = Modifier
                    .width(45.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom)
            ) {
                if (permission.status.isGranted) {
                    if (placesState.selectedPlace != null) {
                        CancelRoute {
                            placesViewModel.clearForNavigation()
                            viewModel.updateRoutePoints(emptyList())
                            viewModel.cancelAdditionalMarkerVisibility()
                        }
                    }
                    WriteDropNote {
                        popupStateDNComposer = !popupStateDNComposer
                    }
                    LocateMe {
                        if (locationState.hasPermission) {
                            cameraPositionState.position =
                                CameraPosition.fromLatLngZoom(state.userMarker.position, 16f)
                            Log.d(
                                "MapTemplate",
                                "Ubicacion: ${locationState.latitude}, ${locationState.longitude}"
                            )
                        } else {
                            locationState.requestPermission()
                        }
                    }

                }
            }
        },
        bottomBar = { Nav(controller) }) { paddingValues ->
        if (permission.status.isGranted) {
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
                    MarkerComposable(
                        state = rememberUpdatedMarkerState(
                            LatLng(
                                userLocationState.latitude,
                                userLocationState.longitude
                            )
                        ), title = "${user.name} (Tú)"
                    ){
                        Avatar(user = user, modifier = Modifier.border(1.dp, Color.White, CircleShape))
                    }
                    Marker(
                        state = rememberUpdatedMarkerState(state.additionalMarker.position),
                        title = state.additionalMarker.title,
                        visible = state.additionalMarkerVisible
                    )
                    if (state.routePoints.isNotEmpty()) {
                        Polyline(
                            points = state.routePoints, color = MaterialTheme.colorScheme.primary, width = 10f
                        )
                    }

                    if (state.userRouteVisible) {
                        Polyline(
                            points = state.userRoutePoints, color = MaterialTheme.colorScheme.tertiary, width = 10f
                        )
                    }
                }
                SensorOverlay(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 64.dp)
                        .padding(16.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var message =
                    "No se puede acceder a esta funcionalidad sin el permiso de localización"
                if (showButton) {
                    message =
                        "Esta función le permite visualizar un mapa para ver rutas. Es indispensable que permita el acceso."

                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        message, textAlign = TextAlign.Center, fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(), onClick = {
                            permission.launchPermissionRequest()
                        }) { Text("Solicitar Permiso") }
                } else {

                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        message, textAlign = TextAlign.Center, fontSize = 15.sp
                    )
                }
            }
        }
    }
    if (popupStateDNComposer) {
        Dialog(
            onDismissRequest = {
                popupStateDNComposer = false
            }, properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .padding(20.dp), contentAlignment = Alignment.Center
            ) {

                DropNoteComposer(
                    value = noteText, onValueChange = { noteText = it },

                    onImageClick = {
                        mediaManager.launchCamera()
                    },

                    onGalleryClick = {
                        mediaManager.launchGallery()
                    },

                    onSendClick = {
                        // TODO enviar nota

                        noteText = ""
                        mediaManager.clearImage()
                        popupStateDNComposer = false
                    },

                    imageUri = mediaManager.imageUri
                )
            }
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