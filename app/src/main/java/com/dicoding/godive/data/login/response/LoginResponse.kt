package com.dicoding.godive.data.login.response

import com.dicoding.godive.data.login.user.User

data class LoginResponse(
    val message: String,
    val user: User
)

data class User(
    val id: String,
    val email: String,
    val token: String
)



