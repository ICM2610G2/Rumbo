package com.appnotresponding.rumbo.ui.viewModel

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MyMarker(var position: LatLng,
                    var title: String = "Marker", var snippet: String ="Desc")

data class HeatmapCluster(val position: LatLng, val count: Int)
data class MapState(
    val userMarker: MyMarker = MyMarker(LatLng(0.0, 0.0)),
    val additionalMarker: MyMarker = MyMarker(LatLng(0.0, 0.0)),
    val additionalMarkerVisible: Boolean = false,
    val routePoints: List<LatLng> = emptyList(),
    val userRoutePoints: List<LatLng> = emptyList(),
    val userRouteVisible: Boolean = false,
    val place: String = "",
    val centerInUserFirstTime: Boolean = true,
    val lastSafeLatLng: LatLng = LatLng(0.0, 0.0),
    val isHeatmapVisible: Boolean = false,
    val heatmapClusters: List<HeatmapCluster> = emptyList()
)

class MapViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MapState())
    val uiState: StateFlow<MapState> = _uiState.asStateFlow()

    init {
        fetchHeatmapPoints()
    }

    private fun fetchHeatmapPoints() {
        com.google.firebase.database.FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val points = mutableListOf<LatLng>()
                    for (userSnapshot in snapshot.children) {
                        val lat = userSnapshot.child("latitude").value?.toString()?.toDoubleOrNull()
                        val lng = userSnapshot.child("longitude").value?.toString()?.toDoubleOrNull()
                        if (lat != null && lng != null && (lat != 0.0 || lng != 0.0)) {
                            points.add(LatLng(lat, lng))
                        }
                    }

                    // Clustering algorithm (200 meters radius)
                    val clusters = mutableListOf<HeatmapCluster>()
                    for (p in points) {
                        var added = false
                        for (i in clusters.indices) {
                            val c = clusters[i]
                            val distance = com.google.maps.android.SphericalUtil.computeDistanceBetween(p, c.position)
                            if (distance <= 200.0) {
                                // Merge into this cluster
                                val newLat = (c.position.latitude * c.count + p.latitude) / (c.count + 1)
                                val newLng = (c.position.longitude * c.count + p.longitude) / (c.count + 1)
                                clusters[i] = HeatmapCluster(LatLng(newLat, newLng), c.count + 1)
                                added = true
                                break
                            }
                        }
                        if (!added) {
                            clusters.add(HeatmapCluster(p, 1))
                        }
                    }

                    // Allow isolated users (count >= 1)
                    _uiState.update { it.copy(heatmapClusters = clusters) }
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    android.util.Log.e("MapViewModel", "Error fetching users for heatmap: ${error.message}")
                }
            })
    }

    fun toggleHeatmap() {
        _uiState.update { it.copy(isHeatmapVisible = !it.isHeatmapVisible) }
    }

    fun updatePlace(place: String) {
        _uiState.update { it.copy(place = place) }
    }

    fun updateCenterInUserFirstTime() {
        _uiState.update { it.copy(centerInUserFirstTime = false) }
    }

    fun updateUserMarker(lat: Double, lng: Double) {
        _uiState.update { it.copy(userMarker = MyMarker(LatLng(lat, lng))) }
    }

    fun updateAdditionalMarker(position: LatLng, title: String) { _uiState.update { it.copy(additionalMarker = MyMarker(position), additionalMarkerVisible = true
    )
    }
    }

    fun cancelAdditionalMarkerVisibility() {
        _uiState.update { it.copy(additionalMarkerVisible = false) }
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