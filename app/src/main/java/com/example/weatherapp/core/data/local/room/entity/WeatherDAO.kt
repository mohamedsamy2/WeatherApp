package com.example.weatherapp.core.data.local.room.entity

import androidx.room.*


@Dao
interface WeatherDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherResponse: WeatherResponse)
//
//    @Update()
//    fun insertCurrentWeather(weatherResponse: WeatherResponse)

    @Query("SELECT * FROM weatherTable")
    fun getFavorites(): List<WeatherResponse>

    @Delete
    fun deleteFavorite(weatherResponse: WeatherResponse)

}