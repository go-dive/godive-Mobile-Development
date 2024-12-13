package com.dicoding.godive.data.weather.service

import com.dicoding.godive.data.weather.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WeatherApiService {

    @POST("weather")
    fun getWeather(@Body cityRequest: CityRequest): Call<WeatherResponse>
}
