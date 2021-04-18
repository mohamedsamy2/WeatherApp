package com.example.weatherapp.features.favorites

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.weatherapp.core.ConnectivityChecker
import com.example.weatherapp.core.data.local.room.entity.WeatherDB
import com.example.weatherapp.core.data.local.room.entity.WeatherResponse
import com.example.weatherapp.core.data.remote.api.ApiServiceInterface
import com.example.weatherapp.core.repo.DBRepo
import com.example.weatherapp.core.util.SettingsProvider
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesViewModel(private val application: Application) : ViewModel() {


    companion object {
        @Volatile
        private var INSTANCE: FavoritesViewModel? = null

        @Synchronized
        fun getInstance(application: Application): FavoritesViewModel =
            INSTANCE ?: FavoritesViewModel(application).also { INSTANCE = it }
    }

    private val settingsProvider = SettingsProvider.getInstance(application.applicationContext)
    var favoritesListLiveData = DBRepo.getInstance(application).FavoritesLiveData

    init {
        updateFavorites()
    }

    fun insertLocationIntoDB(place: Place) {
        var weatherResponse: WeatherResponse? = null
        ApiServiceInterface.create().getWeather(
            place.latLng!!.latitude,
            place.latLng!!.longitude, units = settingsProvider.getUnitSystem(), language = settingsProvider.getLanguage()
        )
            .enqueue(object : Callback<WeatherResponse> {
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    response.body()?.let {
                        weatherResponse = it
                        CoroutineScope(Dispatchers.IO).launch {
                            DBRepo.getInstance(application).insertWeather(it);
                            favoritesListLiveData.value?.add(it)
                            DBRepo.getInstance(application).getFavorites()
                        }


                    }
                }

            })

    }

    fun updateFavorites() {
        if (ConnectivityChecker(application.applicationContext).isOnline()) {
            val list = WeatherDB.get(application).getWeatherDAO().getFavorites()
            Log.d("RETRO", "updateFavorites: ONLINE")
            if (list != null) {
                for (response in list) {

                    ApiServiceInterface.create()
                        .getWeather(response.lat, response.lon, language = settingsProvider.getLanguage(), units = settingsProvider.getUnitSystem())
                        .enqueue(object : Callback<WeatherResponse> {
                            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                                Log.d("RETRO", "onFailure: ")
                            }

                            override fun onResponse(
                                call: Call<WeatherResponse>,
                                response: Response<WeatherResponse>
                            ) {
                                response.body()?.let {
                                    Log.d("RETRO", "onResponse: ONLINE FETCHING")
                                    Log.d("RETRO", "${it.toString()}")
                                    DBRepo.getInstance(application).insertWeather(it)
                                    DBRepo.getInstance(application).getFavorites()

                                }


                            }


                        })
                }
            }
            else
                Log.d("RETRO", "updateFavorites: LIST IS NULL")
        } else {
            Log.d("RETRO", "updateFavorites: OFFLINE")
            DBRepo.getInstance(application).getFavorites()
            Toast.makeText(application.applicationContext,"You are offline. Data may not be up to date",Toast.LENGTH_LONG).show()
        }
    }


}
