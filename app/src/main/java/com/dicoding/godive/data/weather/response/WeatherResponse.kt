package com.dicoding.godive.data.weather.response

data class WeatherResponse(
    val city: String,
    val weather: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double
)
