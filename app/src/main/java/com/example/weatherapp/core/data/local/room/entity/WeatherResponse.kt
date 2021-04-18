package com.example.weatherapp.core.data.local.room.entity


import androidx.room.Entity
import com.example.weatherapp.core.data.model.Alerts
import com.example.weatherapp.core.data.model.CurrentWeather
import com.example.weatherapp.core.data.model.Daily
import com.example.weatherapp.core.data.model.Hourly
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weatherTable", primaryKeys = ["lat", "lon"])
data class WeatherResponse(
    val current: CurrentWeather,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int,
    val alerts : List<Alerts>?
)