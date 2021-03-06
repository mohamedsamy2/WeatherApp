package com.example.weatherapp.core.data.model


import com.google.gson.annotations.SerializedName

data class Hourly(
    val clouds: Double,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val dt: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val humidity: Double,
    val pop: Double,
    val pressure: Double,
    val temp: Double,
    val uvi: Double,
    val visibility: Double,
    val weather: List<WeatherCondition>,
    @SerializedName("wind_deg")
    val windDeg: Double,
    @SerializedName("wind_gust")
    val windGust: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double
)