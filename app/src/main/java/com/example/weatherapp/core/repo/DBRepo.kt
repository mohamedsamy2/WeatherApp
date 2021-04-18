package com.example.weatherapp.core.repo

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.core.data.local.room.entity.WeatherDB
import com.example.weatherapp.core.data.local.room.entity.WeatherResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DBRepo(private val application: Application) {
    companion object {
        @Volatile
        private var INSTANCE: DBRepo? = null

        @Synchronized
        fun getInstance(application: Application): DBRepo =
            INSTANCE ?: DBRepo(application).also { INSTANCE = it }
    }

    var FavoritesLiveData =
        MutableLiveData<ArrayList<WeatherResponse>>()

    fun insertWeather(weatherResponse: WeatherResponse) {

        WeatherDB.get(application).getWeatherDAO().insertWeather(
            weatherResponse
        )

    }

    fun getFavorites() {
        FavoritesLiveData.postValue(
            ArrayList(WeatherDB.get(application).getWeatherDAO().getFavorites())
        )
        WeatherDB.get(application).close()
    }

//    fun getFreshFavorites() {
//        val list: ArrayList<WeatherResponse> =
//            ArrayList(WeatherDB.get(application).getWeatherDAO().getFavorites())
//        val freshList = ArrayList<WeatherResponse>()
//        for (response in freshList) {
//
//        }
//    }


    fun deleteWeather(weatherResponse: WeatherResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            WeatherDB.get(application).getWeatherDAO().deleteFavorite(weatherResponse)
        }
    }
}