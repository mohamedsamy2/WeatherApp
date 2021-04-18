//package com.example.weatherapp.core.util
//
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.util.Log
//import com.google.gson.Gson
//import java.util.*
//
//
//class AlarmHelper(var context: Context) {
//    var intent: Intent? = null
//    var alarmManager: AlarmManager? = null
//    var pendingIntent: PendingIntent? = null
//
//    fun addAlarm(trip: Trip) {
//        val date: String = trip.getDate()
//        val time: String = trip.getTime()
//        Log.i(TAG, "addAlarm: $time")
//        val arr = date.split("/".toRegex()).toTypedArray()
//        val timearr =
//            time.split(" ".toRegex()).toTypedArray()[0].split(":".toRegex()).toTypedArray()
//        val calendar = Calendar.getInstance()
//        if (time.split(" ".toRegex()).toTypedArray()[1] == "pm") {
//            calendar[Calendar.YEAR] = arr[2].toInt()
//            calendar[Calendar.MONTH] = arr[1].toInt() - 1
//            calendar[Calendar.DAY_OF_MONTH] = arr[0].toInt()
//            if (timearr[0].toInt() == 12) {
//                calendar[Calendar.HOUR] = timearr[0].toInt()
//            } else {
//                calendar[Calendar.HOUR] = timearr[0].toInt() + 12
//            }
//            calendar[Calendar.MINUTE] = timearr[1].toInt()
//            calendar[Calendar.SECOND] = 0
//        } else {
//            calendar[Calendar.YEAR] = arr[2].toInt()
//            calendar[Calendar.MONTH] = arr[1].toInt() - 1
//            calendar[Calendar.DAY_OF_MONTH] = arr[0].toInt()
//            if (timearr[0].toInt() == 12) {
//                calendar[Calendar.HOUR] = timearr[0].toInt() - 12
//            } else {
//                calendar[Calendar.HOUR] = timearr[0].toInt()
//            }
//            calendar[Calendar.MINUTE] = timearr[1].toInt()
//            calendar[Calendar.SECOND] = 0
//        }
//        Log.i(TAG, ">>>>>>>>>>addAlarmManager: " + calendar.time.toString())
//        intent = Intent(context, AlarmReciever::class.java)
//        intent!!.putExtra("trip", Gson().toJson(trip))
//        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        Log.i(TAG, "addAlarm: " + trip.getTripID())
//        pendingIntent = PendingIntent.getBroadcast(context, trip.getTripID(), intent, 0)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager!!.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                pendingIntent
//            )
//        } else {
//            alarmManager!![AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
//        }
//    }
//
//    fun cancelAlarm(trip: Trip) {
//        Log.i(TAG, ">>>>>>>>>>>>>>>>cancelAlarm: ")
//        intent = Intent(context, AlarmReciever::class.java)
//        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        pendingIntent = PendingIntent.getBroadcast(
//            context,
//            trip.getTripID(),
//            intent,
//            PendingIntent.FLAG_CANCEL_CURRENT
//        )
//        alarmManager!!.cancel(pendingIntent)
//    }
//
//    companion object {
//        private const val TAG = "main"
//    }
//}
