package com.example.weatherapp.core.data.model


import com.google.gson.annotations.SerializedName

data class Daily(
    val clouds: Double,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val dt: Double,
    @SerializedName("feels_like")
    val feelsLike: FeelsLike,
    val humidity: Double,
    val pop: Double,
    val pressure: Double,
    val sunrise: Double,
    val sunset: Double,
    val temp: Temp,
    val uvi: Double,
    val weather: List<WeatherCondition>,
    @SerializedName("wind_deg")
    val windDeg: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double
)