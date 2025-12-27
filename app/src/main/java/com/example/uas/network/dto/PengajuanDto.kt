package com.example.uas.network.dto

import com.squareup.moshi.Json

// Disesuaikan dengan entity Pengajuan.java di backend
data class PengajuanDto(
    @Json(name = "id")
    val id: Long,
    @Json(name = "user")
    val user: UserSimpleDto,
    @Json(name = "tanggalPengajuan")
    val tanggalPengajuan: String,
    @Json(name = "tujuan")
    val tujuan: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "catatan")
    val catatan: String?
)

data class UserSimpleDto(
    @Json(name = "id")
    val id: Long,
    @Json(name = "nama")
    val nama: String,
    @Json(name = "email")
    val email: String
)
