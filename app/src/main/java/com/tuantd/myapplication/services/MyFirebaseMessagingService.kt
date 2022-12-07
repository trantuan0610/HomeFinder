package com.tuantd.myapplication.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tuantd.myapplication.R
import com.tuantd.myapplication.mainscreen.MainActivity
import com.tuantd.myapplication.mainscreen.home.DetailRoom.DetailRoomActivity
import com.tuantd.myapplication.mainscreen.posts.DetailPost.DetailPostActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var roomId: String = ""
        var postId: String = ""
        if (remoteMessage.data.isNotEmpty()) {

            remoteMessage.data["roomId"]?.let {
                try {
                    roomId = it
                } catch (e: NumberFormatException) {

                }
            }
            remoteMessage.data["postId"]?.let {
                try {
                    postId = it
                } catch (e: NumberFormatException) {

                }
            }

        }
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            sendNotification(remoteMessage, roomId, postId)

        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(remoteMessage: RemoteMessage, roomId: String, postId: String) {

        val intent: Intent?
        if (roomId == "" && postId == "") {
            intent = Intent(this, MainActivity::class.java)
        } else if (postId =="") {
            intent = Intent(this, DetailRoomActivity::class.java).apply {
                putExtra("roomId", roomId)
            }
        } else {
            intent = Intent(this, DetailPostActivity::class.java).apply {
                putExtra("postId", postId)
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        remoteMessage.notification?.let {
            val body = it.body
            val title = it.title

            val channelId = "notificaton_channel"
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo_home)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Since android Oreo notification channel is needed.
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.cancel(0)
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        }

    }
    companion object {
        private const val TAG = "HomeFinderFirebaseMessagingService"
    }

}