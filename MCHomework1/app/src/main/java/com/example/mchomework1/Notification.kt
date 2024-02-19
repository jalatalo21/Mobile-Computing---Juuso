package com.example.mchomework1;

import android.app.NotificationManager;
import android.app.PendingIntent
import android.content.Context;
import android.content.Intent
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class Notification(private val context: Context) {
    private val notifManager = context.getSystemService(NotificationManager::class.java)

    fun showNotification() {
        val intent = Intent(context,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, "1")
            .setContentTitle("Messages")
            .setContentText("There are new messages")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notifManager.notify(
            Random.nextInt(),
            notification
        )
    }
}
