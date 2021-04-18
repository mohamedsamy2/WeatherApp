package com.example.weatherapp.core.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.weatherapp.core.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response


class ConnectivityInterceptorImpl(
    context: Context
) : ConnectivityInterceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!hasInternet())
            throw NoConnectionException()

        return chain.proceed(chain.request())
    }

    private fun hasInternet(): Boolean {
        val cm =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc!!.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        }

        return false
    }
}