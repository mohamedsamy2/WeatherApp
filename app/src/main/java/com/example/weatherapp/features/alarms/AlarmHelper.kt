package com.example.weatherapp.alarms.Alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.weatherapp.core.Receiver


fun setAlarm(context:Context, timeInMillis: Long) {
     val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
     val intent = Intent(context, Receiver::class.java)
     val msUntilTriggerHour: Long = 5000
     val alarmTimeAtUTC: Long = System.currentTimeMillis() + msUntilTriggerHour
     intent.action = "MyBroadcastReceiverAction"
     val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         alarmManager.setExactAndAllowWhileIdle(
             AlarmManager.RTC_WAKEUP,
             alarmTimeAtUTC,
             pendingIntent
         )
     } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
         alarmManager.setExact(
             AlarmManager.RTC_WAKEUP,
             alarmTimeAtUTC,
             pendingIntent
         )
     } else {
         alarmManager.set(
             AlarmManager.RTC_WAKEUP,
             alarmTimeAtUTC,
             pendingIntent
         )
     }
     Log.d("alarm", "Alarm is set")
     Log.d("alarm", "Alarm is ${alarmManager.nextAlarmClock?.triggerTime}")

}