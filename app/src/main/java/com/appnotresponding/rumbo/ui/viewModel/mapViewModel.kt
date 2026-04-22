package com.appnotresponding.rumbo.ui.viewModel

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MyMarker(var position: LatLng,
                    var title: String = "Marker", var snippet: String ="Desc")
data class MapState(
    val userMarker: MyMarker = MyMarker(LatLng(0.0, 0.0)),
    val additionalMarker: MyMarker = MyMarker(LatLng(0.0, 0.0)),
    val additionalMarkerVisible: Boolean = false,
    val routePoints: List<LatLng> = emptyList(),
    val userRoutePoints: List<LatLng> = emptyList(),
    val userRouteVisible: Boolean = false,
    val place: String = "",
    val centerInUserFirstTime: Boolean = true,
    val lastSafeLatLng: LatLng = LatLng(0.0, 0.0)
)

class MapViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(MapState())
    val uiState: StateFlow<MapState> = _uiState.asStateFlow()

    fun updatePlace(place: String) {
        _uiState.update { it.copy(place = place) }
    }

    fun updateCenterInUserFirstTime() {
        _uiState.update { it.copy(centerInUserFirstTime = false) }
    }

    fun updateUserMarker(lat: Double, lng: Double) {
        val newLatLng = LatLng(lat, lng)
        _uiState.update { it.copy(userMarker = MyMarker(newLatLng)) }
    }

    fun updateAdditionalMarker(position: LatLng, title: String) {
        _uiState.update { it.copy(additionalMarker = MyMarker(position), additionalMarkerVisible = true) }
    }

    fun updateRoutePoints(points: List<LatLng>) {
        _uiState.update { it.copy(routePoints = points) }
    }

    fun updateUserRouteVisible() {
        _uiState.update { it.copy(userRouteVisible = !it.userRouteVisible) }
    }

    fun updateLastSafeLatLng(lat: Double, lng: Double) {
        _uiState.update { it.copy(lastSafeLatLng = LatLng(lat, lng)) }
    }

    fun updateUserRoutePoints(points: List<LatLng>) {
        _uiState.update { it.copy(userRoutePoints = points) }
    }

}