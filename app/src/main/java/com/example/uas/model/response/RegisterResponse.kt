package com.example.uas.model.response

import com.squareup.moshi.Json


data class RegisterResponse(
    @Json(name = "message")
    val message: String,
    @Json(name = "status")
    val status: String
)