package com.example.weatherapp.features.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.example.weatherapp.core.ConnectivityChecker
import com.example.weatherapp.core.data.local.room.entity.WeatherResponse
import com.example.weatherapp.core.repo.APIRepo
import com.example.weatherapp.core.util.SettingsProvider
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val context: Context) : ViewModel() {

    private val settingsProvider = SettingsProvider.getInstance(context)
    private val units = settingsProvider.getUnitSystem()
    private val language = settingsProvider.getLanguage()
    val weatherLiveData = APIRepo.weatherLiveData
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    init {
        updateWeather()
    }

    fun updateWeather() {

        val longitude = sharedPreferences.getString("longitude", "0.0")!!.toDouble()
        val latitude = sharedPreferences.getString("latitude", "0.0")!!.toDouble()
        if (ConnectivityChecker(context).isOnline()) {
            CoroutineScope(Dispatchers.IO).launch {
                APIRepo.fetchWeather(
                    longitude,
                    latitude,
                    units = units,
                    language = language
                )
            }
        } else {
            val json = sharedPreferences.getString("cachedweather", "")
            Log.d("TAG", "updateWeather: ${json.toString()}")
            if (!json.isNullOrEmpty()) {
                Toast.makeText(
                    context.applicationContext,
                    "No internet connection. Data may not be up to date",
                    Toast.LENGTH_LONG
                ).show()

                val response = Gson().fromJson(json, WeatherResponse::class.java)
                weatherLiveData.postValue(response)
            } else {
                Toast.makeText(
                    context.applicationContext,
                    "No internet connection. Please connect to the internet to get weather data",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }


}