package com.example.uas.network.dto

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "accessToken")
    val accessToken: String
)