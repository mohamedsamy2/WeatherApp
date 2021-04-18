package com.example.weatherapp.core.util

import androidx.room.TypeConverter
import com.example.weatherapp.core.data.model.Alerts
import com.example.weatherapp.core.data.model.CurrentWeather
import com.example.weatherapp.core.data.model.Daily
import com.example.weatherapp.core.data.model.Hourly
import com.google.gson.Gson

class MyTypeConverters {

    @TypeConverter
    fun dailyToString(daily: List<Daily>): String = Gson().toJson(daily)

    @TypeConverter
    fun stringToDaily(string: String): List<Daily> = Gson().fromJson(string, Array<Daily>::class.java).asList()

    @TypeConverter
    fun currentToString(current: CurrentWeather): String = Gson().toJson(current)

    @TypeConverter
    fun stringToCurrent(string: String): CurrentWeather = Gson().fromJson(string, CurrentWeather::class.java)

    @TypeConverter
    fun hourlyToString(hourly: List<Hourly>): String = Gson().toJson(hourly)

    @TypeConverter
    fun stringToHourly(string: String): List<Hourly> = Gson().fromJson(string, Array<Hourly>::class.java).asList()

    @TypeConverter
    fun alertsToString(alerts: List<Alerts>?): String = Gson().toJson(alerts)

    @TypeConverter
    fun stringToAlerts(string: String): List<Alerts>? = Gson().fromJson(string, Array<Alerts>::class.java)?.asList()


}