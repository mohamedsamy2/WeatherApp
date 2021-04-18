package com.example.weatherapp.core.data.local.room.entity

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.core.util.MyTypeConverters


@Database(
    version = 1,
    entities = [WeatherResponse::class]
)
@TypeConverters(MyTypeConverters::class)
abstract class WeatherDB : RoomDatabase() {
    companion object {
        fun get(application: Application): WeatherDB {
            return Room.databaseBuilder(application, WeatherDB::class.java, "weatherDB")
                .allowMainThreadQueries().build()
        }

    }

    abstract fun getWeatherDAO(): WeatherDAO

}