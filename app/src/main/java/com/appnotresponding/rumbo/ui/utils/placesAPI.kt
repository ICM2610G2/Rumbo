package com.appnotresponding.rumbo.ui.utils

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.add
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.appnotresponding.rumbo.models.Place
import org.json.JSONArray
import org.json.JSONObject
import com.appnotresponding.rumbo.BuildConfig

fun searchNearbyPlaces(
    latitude: Double,
    longitude: Double,
    onPlacesReceived: (List<Place>) -> Unit,
    onError: (String) -> Unit,
    context: Context
) {
    val apiKey = BuildConfig.MAPS_API_KEY
    val requestQueue = Volley.newRequestQueue(context)

    val url =
        "https://places.googleapis.com/v1/places:searchNearby"

    val body = JSONObject().apply {

        put(
            "includedPrimaryTypes",
            JSONArray().put("tourist_attraction").put("museum").put("historical_landmark").put("cultural_landmark").put("zoo").put("aquarium").put("national_park").put("botanical_garden").put("observation_deck")
        )

        put(
            "maxResultCount",
            20
        )
        put("rankPreference", "POPULARITY")
        put(
            "locationRestriction",
            JSONObject().apply {

                put(
                    "circle",
                    JSONObject().apply {

                        put(
                            "center",
                            JSONObject().apply {
                                put("latitude", latitude)
                                put("longitude", longitude)
                            }
                        )

                        put("radius", 2000.0)
                    }
                )
            }
        )
    }

    val request = object : JsonObjectRequest(
        Request.Method.POST,
        url,
        body,

        Response.Listener { response ->

            try {
                if (!response.has("places")) {
                    onError("La respuesta no contiene lugares")
                    return@Listener
                }
                val placesJson = response.getJSONArray("places")

                val placesList = mutableListOf<Place>()

                for (i in 0 until placesJson.length()) {

                    val item = placesJson.getJSONObject(i)

                    val id =
                        item.optString("id", "")

                    val name =
                        item.getJSONObject("displayName")
                            .optString("text", "Sin nombre")

                    val description =
                        if (item.has("editorialSummary")) {

                            item.getJSONObject("editorialSummary")
                                .optString("text", null)

                        } else null

                    var openHours: List<String>? = null

                    if (item.has("currentOpeningHours")) {

                        val hours =
                            item.getJSONObject("currentOpeningHours")

                        if (hours.has("weekdayDescriptions")) {

                            val descriptions =
                                hours.getJSONArray("weekdayDescriptions")

                            val list = mutableListOf<String>()

                            for (j in 0 until descriptions.length()) {

                                list.add(
                                    descriptions.getString(j)
                                )
                            }

                            openHours = list
                        }
                    }

                    val price =
                        if (item.has("priceLevel"))
                            item.getString("priceLevel")
                        else null

                    val rating =
                        if (item.has("rating"))
                            item.getDouble("rating")
                        else null

                    val location =
                        item.getJSONObject("location")

                    val latitude =
                        location.getDouble("latitude")

                    val longitude =
                        location.getDouble("longitude")
                    val address =
                        item.optString(
                            "formattedAddress",
                            "Sin dirección"
                        )

                    var imageUrl: String? = null

                    if (item.has("photos")) {

                        val photos =
                            item.getJSONArray("photos")

                        if (photos.length() > 0) {

                            val photo =
                                photos.getJSONObject(0)

                            val photoName =
                                photo.optString("name")

                            imageUrl =
                                "https://places.googleapis.com/v1/$photoName/media" +
                                        "?maxHeightPx=400" +
                                        "&maxWidthPx=400" +
                                        "&key=$apiKey"
                        }
                    }

                    placesList.add(

                        Place(
                            id = id,
                            name = name,
                            address = address,
                            description = description,
                            openHours = openHours,
                            price = price,
                            latitude = latitude,
                            longitude = longitude,
                            rating = rating,
                            reviews = emptyList(),
                            imageUrl = imageUrl
                        )
                    )
                }

                onPlacesReceived(placesList)

            } catch (e: Exception) {

                onError(
                    "Error parseando respuesta: ${e.message}"
                )
            }
        },

        Response.ErrorListener { error ->

            Log.e("VOLLEY_ERROR", error.toString())

            if (error.networkResponse != null) {

                val code = error.networkResponse.statusCode

                val data = String(error.networkResponse.data)

                onError(
                    "Error $code\n$data"
                )

            } else {

                onError(
                    error.message ?: "Error desconocido"
                )
            }
        }

    ) {

        override fun getHeaders(): MutableMap<String, String> {

            return hashMapOf(
                "Content-Type" to "application/json",
                "X-Goog-Api-Key" to apiKey,
                "Accept-Language" to "es",

                // MUY IMPORTANTE
                "X-Goog-FieldMask" to
                        "places.id," +
                        "places.displayName," +
                        "places.formattedAddress," +
                        "places.editorialSummary," +
                        "places.currentOpeningHours," +
                        "places.priceLevel," +
                        "places.location," +
                        "places.rating," +
                        "places.photos"
            )
        }
    }

    requestQueue.add(request)
}
