package com.example.weatherapp.core.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class SettingsProvider(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: SettingsProvider? = null

        @Synchronized
        fun getInstance(context: Context): SettingsProvider =
            INSTANCE ?: SettingsProvider(context).also { INSTANCE = it }
    }


    private val appContext = context.applicationContext
    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    var fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
    lateinit var locationRequest: LocationRequest
    val PERMISSION_ID = 1010


    fun getUnitSystem() =
        preferences.getString("SYSTEM_UNITS", "METRIC").toString()


    fun getLanguage() =
        preferences.getString("LANGUAGE", "en").toString()

    fun getLocationPref() = preferences.getBoolean("USE_DEVICE_LOCATION", true)

    fun getLocationLatLong() = preferences.getString("PROVIDED_LOCATION", "0/0")

}