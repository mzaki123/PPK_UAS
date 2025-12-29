package com.example.uas.model.request

import com.squareup.moshi.Json

data class PengajuanUpdateStatusRequest(
    @Json(name = "status")
    val status: String
)
