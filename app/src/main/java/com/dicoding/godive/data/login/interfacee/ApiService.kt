package com.dicoding.godive.data.login.interfacee

import com.dicoding.godive.data.login.LoginRequest
import com.dicoding.godive.data.login.response.LoginResponse
import com.dicoding.godive.data.login.response.ProfileResponse
import com.dicoding.godive.data.login.response.ProfileUser
import com.dicoding.godive.data.register.RegisterRequest
import com.dicoding.godive.data.register.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("api/users")
    suspend fun getUsers(@Header("Authorization") token: String): Response<List<ProfileUser>>
}




