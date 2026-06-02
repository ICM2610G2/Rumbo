package com.appnotresponding.rumbo.ui.viewModel

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.VisitedPlace
import com.google.firebase.database.FirebaseDatabase
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val TAG = "ItineraryHistoryVM"
const val VISIT_RADIUS_METERS = 100f

class ItineraryHistoryViewModel : ViewModel() {
    private val dbHistory = FirebaseDatabase.getInstance().getReference("itineraryHistory")
    private val dayFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun markPlaceVisitedIfNeeded(
        userId: String, place: Place?, userLat: Double, userLng: Double, context: Context
    ) {
        if (userId.isBlank() || place == null) return
        if (userLat == 0.0 && userLng == 0.0) return

        val distance = FloatArray(1)
        Location.distanceBetween(
            userLat, userLng, place.latitude, place.longitude, distance
        )
        if (distance.first() > VISIT_RADIUS_METERS) return

        val visitedAt = System.currentTimeMillis()
        val city = resolveCity(context, place)
        val cityKey = toFirebaseKey(city)
        val dayKey = Instant.ofEpochMilli(visitedAt).atZone(ZoneId.systemDefault()).toLocalDate()
            .format(dayFormatter)
        val placeKey = toFirebaseKey(place.id)

        val visitedPlace = VisitedPlace(
            placeId = place.id,
            placeName = place.name,
            address = place.address,
            latitude = place.latitude,
            longitude = place.longitude,
            imageUrl = place.imageUrl,
            city = city,
            visitedAt = visitedAt
        )

        dbHistory.child(userId).child(cityKey).child(dayKey).child(placeKey).setValue(visitedPlace)
            .addOnFailureListener { error ->
                Log.e(TAG, "Error guardando visita: ${error.message}", error)
            }
    }

    private fun resolveCity(context: Context, place: Place): String {
        val cityFromGeocoder = try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION") geocoder.getFromLocation(place.latitude, place.longitude, 1)
                ?.firstOrNull()?.let { address ->
                    address.locality ?: address.subAdminArea ?: address.adminArea
                }
        } catch (error: Exception) {
            Log.w(TAG, "No se pudo resolver ciudad: ${error.message}")
            null
        }

        return cityFromGeocoder?.takeIf { it.isNotBlank() } ?: place.address.split(",")
            .map { it.trim() }.firstOrNull { it.isNotBlank() } ?: "Sin ciudad"
    }

    private fun toFirebaseKey(raw: String): String {
        val cleaned = raw.ifBlank { "sin-id" }.replace(".", "_").replace("#", "_").replace("$", "_")
            .replace("[", "_").replace("]", "_").replace("/", "_")
        return cleaned.ifBlank { "sin-id" }
    }
}
