package com.appnotresponding.rumbo.ui.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

//https://stackoverflow.com/questions/54997485/android-notification-during-app-is-in-background-intent-data-is-empty
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.i("FirebaseApp"
            , "Message Received!!!")
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]
        val senderId = remoteMessage.data["senderId"]
        val chatId = remoteMessage.data["chatId"]
        val senderName = remoteMessage.data["senderName"]
        val senderPhotoUrl = remoteMessage.data["senderPhotoUrl"]
        val isOnline = remoteMessage.data["isAvailable"]?.toBoolean()
        if(title != null && body != null){
            Log.i("FirebaseApp"
                , title)
            Log.i("FirebaseApp"
                , body)
            showNotification(title, body, this, senderId, chatId, senderName, senderPhotoUrl, isOnline)
        }
    }
}