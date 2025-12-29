package com.example.uas.model.request

import com.squareup.moshi.Json

data class CreatePengajuanRequest(
    @Json(name = "tujuanSurat")
    val tujuanSurat: String,
    @Json(name = "filePendukung")
    val filePendukung: String? = null
)
