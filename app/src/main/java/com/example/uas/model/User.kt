package com.example.uas.model

import com.squareup.moshi.Json

data class User(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "role")
    val role: String
)
