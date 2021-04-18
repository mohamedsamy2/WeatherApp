package com.example.weatherapp.features.home

import DailyWeatherAdapter
import HourlyWeatherAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.core.util.SettingsProvider
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.*
import com.google.gson.Gson

import java.time.Instant
import java.util.*
import kotlin.time.ExperimentalTime

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var currentDegree: TextView
    private lateinit var locationName: TextView
    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var dailyRecyclerView: RecyclerView
    private lateinit var weatherIcon: ImageView
    private lateinit var weatherDesc: TextView
    private lateinit var humidity: TextView
    private lateinit var feelsLike: TextView
    private lateinit var currentDate: TextView
    private lateinit var currentTime: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var shimmer: ShimmerFrameLayout
    val PERMISSION_ID = 1010


    @SuppressLint("CommitPrefEdits")
    @ExperimentalTime
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(
                this,
                HomeViewModelFactory(this.requireContext())
            ).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        initComponents(root)
        shimmer.startShimmer()

        dailyRecyclerView.setHasFixedSize(true)
        hourlyRecyclerView.setHasFixedSize(true)
        val hourlyLinearLayoutManager = LinearLayoutManager(root.context)
        hourlyLinearLayoutManager.orientation = RecyclerView.HORIZONTAL
        hourlyRecyclerView.layoutManager = hourlyLinearLayoutManager
        val hourlyAdapter = HourlyWeatherAdapter()
        hourlyRecyclerView.adapter = hourlyAdapter

        val dailyWeatherAdapter = DailyWeatherAdapter(arrayListOf())
        dailyRecyclerView.adapter = dailyWeatherAdapter
        val dailyLinearLayoutManager = LinearLayoutManager(root.context)
        dailyLinearLayoutManager.orientation = RecyclerView.VERTICAL
        dailyRecyclerView.isNestedScrollingEnabled = false
        dailyRecyclerView.layoutManager = dailyLinearLayoutManager
        homeViewModel.updateWeather()


        homeViewModel.weatherLiveData.observe(viewLifecycleOwner, Observer {
            val response = it;
            Log.d("TAG", "onCreateView: ${it.current}")
            val editor =
                PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)
                    ?.edit()
            editor?.let {
                it.putString("cachedweather", Gson().toJson(response)).apply()
            }
            if (it.current != null) {
                currentDegree.text = "${it.current.temp.toInt()}°"
                locationName.text = it.timezone.substringAfter("/").replace("_", " ").capitalize()
                feelsLike.text = resources.getString(R.string.feelslike)+" ${it.current.feelsLike.toInt()}°"
                humidity.text = "${resources.getString(R.string.humidity)} ${it.current.humidity.toInt()}%"
                weatherDesc.text = it.current.weather[0].description.capitalize()
                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/${it.current.weather[0].icon}@4x.png")
                    .centerCrop()
                    .into(weatherIcon)
                val instant = Instant.ofEpochSecond(it.current.dt.toLong());
                val date = Date.from(instant);
                val arr = date.toString().split(" ")
                currentDate.text = "${arr[0]} ${arr[1]} ${arr[2]}"
                currentTime.text = "${resources.getString(R.string.lastupdate)}: ${arr[3].dropLast(3)}"
                hourlyAdapter.setList(it.hourly)
                dailyWeatherAdapter.setList(ArrayList(it.daily))
            }
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
        })






        return root
    }

    private fun initComponents(root: View) {
        shimmer = root.findViewById(R.id.shimmerLayout)
        currentDegree = root.findViewById(R.id.currentDegree)
        locationName = root.findViewById(R.id.locationName)
        weatherIcon = root.findViewById(R.id.weatherIcon)
        weatherDesc = root.findViewById(R.id.weatherDesc)
        hourlyRecyclerView = root.findViewById(R.id.hourlyRecyclerview)
        dailyRecyclerView = root.findViewById(R.id.dailyRecyclerview)
        feelsLike = root.findViewById(R.id.feelsLike)
        humidity = root.findViewById(R.id.currentHumidity)
        currentDate = root.findViewById(R.id.currentDate)
        currentTime = root.findViewById(R.id.currentTime)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity().applicationContext)

        updateLocation()

    }

    override fun onResume() {
        super.onResume()
//        testProvider = SettingsProvider(this)
        updateLocation()
    }

    fun updateLocation() {
        if ((SettingsProvider(requireActivity().applicationContext).getLocationPref())) {
            if (CheckPermission())
                getLastLocation()
            else
                RequestPermission()


        } else {
            val location =
                SettingsProvider(requireActivity().applicationContext).getLocationLatLong()
            val LatLong = location?.split("/")
            val editor =
                PreferenceManager.getDefaultSharedPreferences(this.context)?.edit()
            if (editor != null) {
                editor.putString("latitude", LatLong?.get(0)).apply()
                editor.putString("longitude", LatLong?.get(1)).apply()
            }

        }

    }

    fun RequestPermission() {

        ActivityCompat.requestPermissions(
            activity as Activity,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled(): Boolean {
        var locationManager =
            requireActivity().applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun CheckPermission(): Boolean {

        if (
            ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false

    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (CheckPermission()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        NewLocationData()
                    } else {
                        val editor =
                            PreferenceManager.getDefaultSharedPreferences(this.context)?.edit()
                        if (editor != null) {
                            editor.putString("latitude", location.latitude.toString()).apply()
                            editor.putString("longitude", location.longitude.toString()).apply()
                        }
                        homeViewModel.updateWeather()

                    }
                }
            } else {
                Toast.makeText(
                    requireActivity().applicationContext,
                    "Please turn on location services",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            RequestPermission()
        }
    }


    @SuppressLint("MissingPermission")
    fun NewLocationData() {
        locationRequest =
            LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(0).setFastestInterval(0).setNumUpdates(1)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity().applicationContext)
        fusedLocationClient!!.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback() {
        @SuppressLint("CommitPrefEdits")
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            val editor =
                PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)
                    ?.edit()
            editor?.let {
                it.putString("latitude", lastLocation.latitude.toString()).apply()
                it.putString("longitude", lastLocation.longitude.toString()).apply()
            }

            homeViewModel.updateWeather()

        }
    }


}
