package com.example.uas.model.response

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "email")
    val email: String,
    @Json(name = "accessToken")
    val accessToken: String,
    @Json(name = "role")
    val role: String
)
