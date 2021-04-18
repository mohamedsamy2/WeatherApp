package com.example.weatherapp.core.repo

interface RepoInterface {
   suspend  fun fetchWeather(longitude: Double, latitude:Double, language:String, units: String)


}