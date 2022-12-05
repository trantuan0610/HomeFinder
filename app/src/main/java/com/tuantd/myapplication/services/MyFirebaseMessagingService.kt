package com.tuantd.myapplication.services

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

class MyFirebaseMessagingService : FirebaseMessagingService() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        if (remoteMessage.data.isEmpty()){
//            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
//        }

        var roomId: String = ""
        if (remoteMessage.data.isNotEmpty()) {
            remoteMessage.data["roomId"]?.let {
                try {
                    roomId = it
                    Log.e("roomId","roomId0: "+ roomId)
                } catch (e: NumberFormatException) {

                }
            }

        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            sendNotification(remoteMessage, roomId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(remoteMessage: RemoteMessage, taskId: String) {

        val intent: Intent?
        if (taskId.isEmpty()) {
            intent = Intent(this, MainActivity::class.java)
        } else {
            intent = Intent(this,
                DetailRoomActivity::class.java).apply {
                putExtra("roomId", taskId)
                Log.e("roomId","roomId1: "+ taskId)
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE)

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
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager.cancel(0)
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        }

    }

//    fun generateNotification(title : String, message : String){
//        val intent = Intent(this,MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this,0, intent,PendingIntent.FLAG_ONE_SHOT)
//        //channelId,channelName
//        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
//            .setSmallIcon(R.drawable.logo_home)
//            .setAutoCancel(true)
//            .setVibrate(longArrayOf(1000,1000,1000,1000))
//            .setOnlyAlertOnce(true)
//            .setContentIntent(pendingIntent)
//
//        builder = builder.setContent(getRemoteView(title,message))
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//
//    }

//    @SuppressLint("RemoteViewLayout")
//    private fun getRemoteView(title: String, message: String): RemoteViews? {
//        val remoteView = RemoteViews("com.tuantd.myapplication.services", R.layout.notification)
//        remoteView.setTextViewText(R.id.title, title)
//        remoteView.setTextViewText(R.id.message, message)
//        remoteView.setImageViewResource(R.id.app_logo, R.drawable.logo_home)
//
//        return remoteView
//    }
    companion object {
        private const val TAG = "HomeFinderFirebaseMessagingService"
    }
}
const val channelId = "notificaton_channel"
const val channelName = "com.tuantd.myapplication.services"