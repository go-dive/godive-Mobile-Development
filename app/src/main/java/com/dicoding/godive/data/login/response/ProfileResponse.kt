// ProfileResponse.kt
package com.dicoding.godive.data.login.response

data class ProfileResponse(
    val users: List<ProfileUser>
)

data class ProfileUser(
    val id: String,
    val name: String,
    val email: String
)
