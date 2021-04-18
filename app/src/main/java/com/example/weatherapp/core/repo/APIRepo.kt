package com.example.weatherapp.core.repo

import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.core.data.local.room.entity.WeatherResponse
import com.example.weatherapp.core.data.model.Alerts
import com.example.weatherapp.core.data.remote.api.ApiServiceInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object APIRepo : RepoInterface {


    var weatherLiveData = MutableLiveData<WeatherResponse>()

    override suspend fun fetchWeather(longitude: Double, latitude: Double, language: String, units: String) {
        ApiServiceInterface.create().getWeather(
            latitude = latitude,
            longitude = longitude,
            language = language,
            units = units
        )
            .enqueue(object : Callback<WeatherResponse> {
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    weatherLiveData.value = response.body()
                }

            })
    }

    fun checkAlerts(longitude: Double, latitude: Double, language: String, units: String): List<Alerts> {
        val response = ApiServiceInterface.create().getWeatherResponse(
            latitude = latitude,
            longitude = longitude,
            language = language,
            units = units
        )
        return response.alerts!!
    }

}