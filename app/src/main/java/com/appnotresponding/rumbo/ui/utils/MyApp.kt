package com.appnotresponding.rumbo.ui.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging

class MyApp : Application() {
    companion object{
        const val NOTIFICATION_CHANNEL_ID =
            "notificaion_fcm"
        var fcmToken: String? = null
    }
    override fun onCreate() {
        super.onCreate()
        Firebase.messaging.token.addOnSuccessListener {
            Log.i("FirebaseApp"
                , "Token: "+it.toString())
        }
        createNotificationChannel()
        Firebase.messaging.token.addOnSuccessListener { token ->
            fcmToken = token
            Log.i("FirebaseApp"
                , "Token: "+token)
        }
    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "FCM Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description="Channel for FCM Notifications"
            val notManager = getSystemService(NotificationManager::class.java)
            notManager.createNotificationChannel(channel)
        }
    }
}