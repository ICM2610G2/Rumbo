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
    targetUid: String? = null
) {
    val notManager = getSystemService(context, NotificationManager::class.java)

    val intent = Intent(context, MainActivity::class.java).apply {
        Log.i("NotifExp", targetUid?.toString() ?: "null")
        targetUid?.let {
            putExtra("targetUid", it)
        }
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    val pendingIntent = PendingIntent.getActivity(
        context,
        targetUid?.hashCode() ?: 0,
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