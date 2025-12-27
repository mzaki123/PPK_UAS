package com.example.uas.network.dto

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "accessToken")
    val accessToken: String,
    @Json(name = "role")
    val role: String
)