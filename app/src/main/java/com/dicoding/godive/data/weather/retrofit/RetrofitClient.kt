package com.dicoding.godive.data.weather.retrofit

import com.dicoding.godive.data.weather.service.WeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://weather-dot-godive-442206.et.r.appspot.com/"

    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: WeatherApiService by lazy {
        retrofitInstance.create(WeatherApiService::class.java)
    }
}
