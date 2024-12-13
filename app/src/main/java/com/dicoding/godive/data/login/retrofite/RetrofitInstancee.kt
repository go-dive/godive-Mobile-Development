package com.dicoding.godive.data.login.retrofite


import com.dicoding.godive.data.login.interfacee.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstancee {
    private const val BASE_URL = "https://login-dot-godive-442206.et.r.appspot.com/"

    val api: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}


