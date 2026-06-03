package com.appnotresponding.rumbo.ui.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.appnotresponding.rumbo.MainActivity
import com.appnotresponding.rumbo.R

fun showNotification(
    title: String,
    message: String,
    context: Context,
    senderId: String? = null,
    chatId: String? = null,
    senderName: String? = null,
    senderPhotoUrl: String? = null,
    isOnline: Boolean? = null
) {
    val notManager = getSystemService(context, NotificationManager::class.java)

    val intent = Intent(context, MainActivity::class.java).apply {
        senderId?.let {
            putExtra("senderId", it)
        }
        chatId?.let {
            putExtra("chatId", it)
        }
        senderName?.let {
            putExtra("senderName", it)
        }
        senderPhotoUrl?.let {
            putExtra("senderPhotoUrl", it)
        }
        isOnline?.let {
            putExtra("isOnline", it)
        }
        putExtra("openChat", true)

        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    val requestCode = chatId?.hashCode() ?: System.currentTimeMillis().toInt()
    val pendingIntent = PendingIntent.getActivity(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(
        context,
        MyApp.NOTIFICATION_CHANNEL_ID
    )
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(R.drawable.brand)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()

    notManager?.notify(1, notification)
}