package com.example.uas.model.request

import com.squareup.moshi.Json

data class RegisterRequest(
    @Json(name = "nama")
    val nama: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String,

    @Json(name = "role")
    val role: String
)