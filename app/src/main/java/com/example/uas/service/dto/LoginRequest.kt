package com.example.uas.service.dto

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)