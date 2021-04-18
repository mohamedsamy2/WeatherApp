package com.example.weatherapp.core.data.model


data class WeatherCondition(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)