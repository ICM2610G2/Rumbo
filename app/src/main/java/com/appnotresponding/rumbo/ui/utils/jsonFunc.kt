package com.appnotresponding.rumbo.ui.utils

import com.appnotresponding.rumbo.models.User
import com.google.firebase.database.DataSnapshot

fun DataSnapshot.toUser(uid: String): User {
    return User(
        id = uid,
        name = child("name").getValue(String::class.java) ?: "",
        lastname = child("lastname").getValue(String::class.java) ?: "",
        email = child("email").getValue(String::class.java) ?: "",
        phone = child("phone").getValue(String::class.java) ?: "",
        latitude = child("latitude").getValue(Double::class.java) ?: 0.0,
        longitude = child("longitude").getValue(Double::class.java) ?: 0.0,
        altitude = child("altitude").getValue(Double::class.java) ?: 0.0,
        profilePictureUrl = child("profilePictureUrl").getValue(String::class.java),
        sharingLocation = child("sharingLocation").getValue(Boolean::class.java) ?: false,
        activity = child("activity").getValue(String::class.java),
        isOnline = child("isOnline").getValue(Boolean::class.java) ?: false,
        lastSeenAt = child("lastSeenAt").getValue(Long::class.java) ?: 0L
    )
}