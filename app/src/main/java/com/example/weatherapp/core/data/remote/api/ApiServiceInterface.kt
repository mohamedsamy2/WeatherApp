package com.example.weatherapp.core.data.remote.api

import com.example.weatherapp.core.data.local.room.entity.WeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val KEY = "593d2c382779430ad3d6a139343a7d5c"

//https://api.openweathermap.org/data/2.5/onecall?lat=30.013056&lon=31.208853&exclude=minutely&appid=593d2c382779430ad3d6a139343a7d5c&lang=en

interface ApiServiceInterface {

    @GET("data/2.5/onecall?")
    fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String,
        @Query("appid") key: String = KEY,
        @Query("exclude") exc: String = "minutely",
        @Query("units") units: String
    ): Call<WeatherResponse>

    @GET("data/2.5/onecall?")
    fun getWeatherResponse(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String,
        @Query("appid") key: String = KEY,
        @Query("exclude") exc: String = "minutely",
        @Query("units") units: String
    ): WeatherResponse

    companion object {
        fun create(): ApiServiceInterface {
            return Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(ApiServiceInterface::class.java)
        }
    }

}
