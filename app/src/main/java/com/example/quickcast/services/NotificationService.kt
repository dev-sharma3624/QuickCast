package com.example.quickcast.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.quickcast.R

/**
 * [NotificationService] contains functionalities to create and show notifications.
 * */

class NotificationService {

    /**
     * [channelId] used to create a unique channel and then send notifications to that channel.
     * */
    private val channelId = "NOTIFICATION_CHANNEL"



    /**
     * [createNotificationChannel] creates a notification channel that is used for displaying
     * notifications.
     * */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "General Channel"
            val descriptionText = "Channel for general notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



    /**
     * [showSimpleNotification] creates a notification and then shows it over a notification channel.
     *
     * @param context required for notification builder, notification manager.
     * @param msg the notification message to displayed in the notification.
     * @param pendingIntent a nullable type [PendingIntent] that will be launched when the user taps on notification.
     * It needs to be nullable in case if the app is already in foreground then in that case we don't want any action to
     * be performed when the user taps on notification.
     * */

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showSimpleNotification(
        context: Context,
        msg : String,
        pendingIntent: PendingIntent?
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // must be a valid icon
            .setContentText(msg)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(101, builder.build())
    }


}