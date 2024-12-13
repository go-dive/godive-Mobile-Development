package com.dicoding.godive.data.remote.api

import com.dicoding.godive.data.remote.response.DivingPlaceItem
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {

    @GET("api/descriptions")
    fun getDivingPlaces(): Call<List<DivingPlaceItem>>

    companion object {
        private const val BASE_URL = "https://godive-442206.et.r.appspot.com/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
