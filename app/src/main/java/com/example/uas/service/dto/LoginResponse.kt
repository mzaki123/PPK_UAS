package com.example.uas.service.dto

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "accessToken")
    val accessToken: String,
    @Json(name = "email")
    val email: String,
)