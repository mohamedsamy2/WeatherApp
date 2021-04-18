package com.example.weatherapp.core.util

import android.content.Context

class MyLocationProvider(myContext: Context) {

    val context = myContext
    private val settingsProvider = SettingsProvider(context.applicationContext)

}