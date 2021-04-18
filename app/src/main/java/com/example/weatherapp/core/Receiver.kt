package com.example.weatherapp.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherapp.NotificationUtil
import com.example.weatherapp.R
import com.example.weatherapp.core.data.local.room.entity.WeatherResponse
import com.example.weatherapp.core.data.model.Alerts
import com.example.weatherapp.core.data.remote.api.ApiServiceInterface
import com.example.weatherapp.core.util.SettingsProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // We use this to make sure that we execute code, only when this exact
        // Alarm triggered our Broadcast receiver
        if (intent?.action == "MyBroadcastReceiverAction") {
            createNotificationChannel()
            Log.d("ALARM", "RECEIVED")
            if (ConnectivityChecker(context!!).isOnline()) {
                val settings = SettingsProvider(context)
                val location = settings.getLocationLatLong()?.split("/")
                ApiServiceInterface.create()
                    .getWeather(
                        latitude = location!![0].toDouble(),
                        longitude = location!![1].toDouble(),
                        language = settings.getLanguage(),
                        units = settings.getUnitSystem()

                    )
                    .enqueue(object : Callback<WeatherResponse> {
                        override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                            Log.d("RETRO", "onFailure: ")
                        }

                        override fun onResponse(
                            call: Call<WeatherResponse>,
                            response: Response<WeatherResponse>
                        ) {
                            response.body()?.let {
                                Log.d("alert", "onResponse: " + it.lat + it.lon)
                                var alerts: List<Alerts>? = it.alerts
                                if (alerts.isNullOrEmpty()) {
                                    Log.d("alert", "onReceive: NO ALERTS")
                                    var builder = NotificationCompat.Builder(
                                        MyApplication.appContext!!,
                                        "CHANNEL_ID"
                                    )
                                        .setSmallIcon(R.drawable.cloud)
                                        .setContentTitle("No alerts!")
                                        .setContentText("There are no alerts for today!")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    NotificationManagerCompat.from(MyApplication.appContext!!)
                                        .notify(2, builder.build())
                                } else {
                                    Log.d("alert", "onReceive: ALERTS")
                                    var builder = NotificationCompat.Builder(
                                        MyApplication.appContext!!,
                                        "CHANNEL_ID"
                                    )
                                        .setSmallIcon(R.drawable.cloud)
                                        .setContentTitle(it.alerts?.get(0)?.event.toString() + " weather alert!")
                                        .setContentText("")
                                        .setContentText(
                                            "From ${Date(it.alerts?.get(0)?.start!!).toString().dropLast(15)} " +
                                                    "to ${Date(it.alerts?.get(0)?.end!!).toString().dropLast(15)}"
                                        )
                                        .setStyle(
                                            NotificationCompat.BigTextStyle()
                                                .bigText("${Date(it.alerts?.get(0)?.start!!).toString().dropLast(15)} to" +
                                            " ${Date(it.alerts?.get(0)?.end!!).toString().dropLast(15)}")
                                        )
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    NotificationManagerCompat.from(MyApplication.appContext!!)
                                        .notify(2, builder.build())
                                    NotificationUtil(it.alerts?.get(0)!!)
                                }

                            }


                        }


                    })

            } else {
                Log.d("TAG", "onReceive: NO INTERNET FOR ALERT")
                var builder = NotificationCompat.Builder(MyApplication.appContext!!, "CHANNEL_ID")
                    .setSmallIcon(R.drawable.cloud)
                    .setContentTitle("Sorry, we couldn't check your weather alert!")
                    .setContentText("Please stay connected to the internet to get future alarms")
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Please stay connected to the internet to get future alarms")
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                NotificationManagerCompat.from(MyApplication.appContext!!)
                    .notify(2, builder.build())

            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "alerts"
            val descriptionText = "alerts"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                MyApplication.appContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}