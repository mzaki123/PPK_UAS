package com.example.uas.model

import com.squareup.moshi.Json

data class Pengajuan(
    @Json(name = "id")
    val id: Long,
    @Json(name = "tujuanSurat")
    val tujuanSurat: String,
    @Json(name = "tanggalPengajuan")
    val tanggalPengajuan: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "mahasiswaNama")
    val mahasiswaNama: String,
    @Json(name = "mahasiswaNim")
    val mahasiswaNim: String
)
