package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

// TODO: Step 2.0 add style
    val notificationImage = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.ic_assistant_black_24dp
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(notificationImage)
            .bigLargeIcon(null)


    // TODO: Step 2.2 add snooze action
//    val statusIntent = Intent(applicationContext, SnoozeReceiver::class.java)
//    val snoozePendingIntent: PendingIntent =
//            PendingIntent.getBroadcast(applicationContext, REQUEST_CODE, snoozeIntent, FLAGS)

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
            applicationContext,
            // TODO: Step 1.8 use the new 'breakfast' notification channel
            applicationContext.getString(R.string.notification_channel_id)
    )
            // TODO: Step 1.3 set title, text and icon to builder
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)
            // TODO: Step 1.13 set content intent
            //.setContentIntent(contentPendingIntent)
            .setStyle(bigPicStyle)
            // TODO: Step 2.3 add snooze action
            .addAction(
                    R.drawable.ic_assistant_black_24dp,
                    applicationContext.getString(R.string.notification_button),
                    contentPendingIntent
            )
            // TODO: Step 2.5 set priority
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

    // TODO Step 1.4 call notify
    // Deliver the notification
    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}