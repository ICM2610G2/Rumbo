package com.appnotresponding.rumbo.ui.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationState(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val hasPermission: Boolean,
    val showRationale: Boolean,
    val requestPermission: () -> Unit
)

private fun startLocationUpdates(
    context: android.content.Context,
    locationClient: FusedLocationProviderClient,
    locationRequest: LocationRequest,
    locationCallback: LocationCallback
) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        try {
            locationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.e("LocationManager", "Error al solicitar ubicacion: ${e.message}")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberLocationManager(): LocationState {
    val context = LocalContext.current
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var altitude by remember { mutableDoubleStateOf(0.0) }

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    var showRationaleButton by remember { mutableStateOf(false) }

    SideEffect {
        if (!locationPermissionState.status.isGranted) {
            if (locationPermissionState.status.shouldShowRationale) {
                showRationaleButton = true
            } else {
                showRationaleButton = false
                locationPermissionState.launchPermissionRequest()
            }
        }
    }

    val locationRequest = remember {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(5000).build()
    }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.lastLocation?.let {
                    latitude = it.latitude
                    longitude = it.longitude
                    altitude = it.altitude
                }
            }
        }
    }

    DisposableEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            startLocationUpdates(context, locationClient, locationRequest, locationCallback)
        }
        onDispose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

    return LocationState(
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        hasPermission = locationPermissionState.status.isGranted,
        showRationale = showRationaleButton,
        requestPermission = { locationPermissionState.launchPermissionRequest() })
}
