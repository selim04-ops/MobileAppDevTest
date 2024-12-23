package com.example.mobileappdevtest

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")

        // Show a notification
        showNotification(context, title ?: "Todo Reminder", description ?: "You have a task due!")
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, title: String, description: String) {
        val channelId = "todo_reminder_channel"

        // Create a notification channel for Android 8.0 and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Todo Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description
                enableLights(true) // Optional: Enable lights for the notification
                lightColor = android.graphics.Color.BLUE // Optional: Set the light color
                enableVibration(true) // Optional: Enable vibration
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_alarme) // Replace with your app's valid icon
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Display the notification
        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), notification)
        }
    }
}
