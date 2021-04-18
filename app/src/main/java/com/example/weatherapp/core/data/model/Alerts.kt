package com.example.weatherapp.core.data.model

data class Alerts (

	val sender_name : String,
	val event : String,
	val start : Long,
	val end : Long,
	val description : String
)