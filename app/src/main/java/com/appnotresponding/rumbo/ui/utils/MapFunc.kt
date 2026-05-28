package com.appnotresponding.rumbo.ui.utils

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

fun createLocationRequest() : LocationRequest{
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 2000)
        .setWaitForAccurateLocation(true)
        .setMinUpdateIntervalMillis(1000)   //.setMinUpdateDistanceMeters(10f)
        .build()
    return locationRequest
}

fun createLocationCallback(onLocationChange: (LocationResult) -> Unit): LocationCallback {
    val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            onLocationChange(locationResult)
        }
    }
    return callback
}