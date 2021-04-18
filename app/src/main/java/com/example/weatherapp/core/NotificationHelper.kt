package com.example.weatherapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build.*
import android.os.Build.VERSION.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.weatherapp.core.MyApplication
import com.example.weatherapp.core.data.model.Alerts

class NotificationUtil(var alertsItem: Alerts): ContextWrapper(MyApplication.appContext) {

    val TAG="main"
    val  channelID = "channelID";
    val  channelName = "Channel Name"

    init {

        if (SDK_INT >= VERSION_CODES.O) {

            createChannel()

        }else{

            sendNotification()

        }
    }




    companion object{



    }


    private var mManager: NotificationManager?=null

    @RequiresApi(api = VERSION_CODES.O)
    fun createChannel() {
        val channel= NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    fun getManager():NotificationManager {
        if (mManager == null) {
            mManager =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        return mManager!!;
    }

    fun getChannelNotification(): NotificationCompat.Builder{

        var sound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return NotificationCompat.Builder(getApplicationContext(), channelID)
            .setContentTitle(alertsItem.event)
            .setContentText(alertsItem.description+"\n"+"from:()")
            .setStyle(NotificationCompat.BigTextStyle())
            .setDefaults(Notification.FLAG_SHOW_LIGHTS)
            .setSubText("")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setAutoCancel(false)
            .setSound(sound)
            .setWhen(System.currentTimeMillis())
            .setLights(0xff00ff00.toInt(), 300, 100)
            .setContentIntent(null)
    }

    fun  sendNotification() {

        var  builder:NotificationCompat.Builder =NotificationCompat.Builder(this)
        builder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
        builder.setContentIntent(null)
        builder.setContentTitle(alertsItem.event)
        builder.setStyle(NotificationCompat.BigTextStyle())
        builder.setContentText("alo");
        builder.setSubText("");
        var notificationManager:NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build());
    }
}