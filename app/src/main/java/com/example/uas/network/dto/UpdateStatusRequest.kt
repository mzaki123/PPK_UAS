package com.example.uas.network.dto

import com.squareup.moshi.Json

data class UpdateStatusRequest(
    @Json(name = "status")
    val status: String, // "SELESAI" atau "DITOLAK"
    @Json(name = "catatan")
    val catatan: String?
)
