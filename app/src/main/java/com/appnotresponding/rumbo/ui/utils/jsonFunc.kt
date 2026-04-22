package com.appnotresponding.rumbo.ui.utils

import android.content.Context
import com.appnotresponding.rumbo.models.Place
import com.appnotresponding.rumbo.models.Review
import org.json.JSONArray
import org.json.JSONObject

fun loadPlaces(context: Context): MutableList<Place> {
    val places = mutableListOf<Place>()

    val jsonString = context.assets.open("places.json").bufferedReader().use { it.readText() }
    var placesJsonArray = JSONArray(jsonString)

    for (i in 0..placesJsonArray.length() - 1) {
        val placeObject = placesJsonArray.getJSONObject(i)

        val id = placeObject.getString("id")
        val name = placeObject.getString("name")
        val description = placeObject.getString("description")
        val openHours = placeObject.getString("openHours")
        val price = placeObject.getString("price")
        val latitude = placeObject.getDouble("latitude")
        val longitude = placeObject.getDouble("longitude")
        val rating = placeObject.getDouble("rating").toFloat()
        val reviews = emptyList<Review>()
        val image = placeObject.getString("imageUrl")
        val place = Place(id, name, description, openHours, price, latitude, longitude, rating, reviews, image)
        places.add(place)
    }

    return places
}