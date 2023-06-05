package com.example.workinghours

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method will be called when the alarm triggers the notification

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(context, "Working_Hours_APP")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Daily Notification")
            .setContentText("This is your daily notification")
            .setPriority(NotificationCompat.PRIORITY_MAX)

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, notificationBuilder.build())
    }
}
