package com.appnotresponding.rumbo.ui.viewModel

import android.app.Application
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.appnotresponding.rumbo.ui.utils.createLocationCallback
import com.appnotresponding.rumbo.ui.utils.createLocationRequest
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserLocationState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double = 0.0,
)

// https://stackoverflow.com/questions/51451819/how-to-get-context-in-android-mvvm-viewmodel
class UserLocationViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(UserLocationState())
    val uiState: StateFlow<UserLocationState> = _uiState.asStateFlow()

    val context = getApplication<Application>()
    val locationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationCallback = createLocationCallback { result ->

        result.lastLocation?.let { location ->

            _uiState.update {
                it.copy(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    altitude = location.altitude
                )
            }
            Log.i("ULViewModel", "Ubicación recibida: ${location.latitude}, ${location.longitude}")

        }
    }

    var locationRequest: LocationRequest = createLocationRequest()
    var permissionGranted = false
    var vel: Task<Void>? = null


    fun updateVel() {
        Log.i("Informativo", "Logrado")
        if (!permissionGranted) {
            permissionGranted = true
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i("Informativo", "Logrado2")
                vel = locationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationClient.removeLocationUpdates(locationCallback)
    }
}