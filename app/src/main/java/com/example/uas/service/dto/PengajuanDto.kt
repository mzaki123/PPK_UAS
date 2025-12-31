package com.example.uas.model

import com.squareup.moshi.Json

/**
 * DTO Utama untuk menerima data Pengajuan dari Backend.
 * Menggunakan UserSimpleDto untuk data user yang tersemat (nested).
 */
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

/**
 * DTO untuk data user minimalis di dalam Pengajuan
 */
data class UserSimpleDto(
    @Json(name = "id")
    val id: Long,
    @Json(name = "nama")
    val nama: String,
    @Json(name = "email")
    val email: String
)

/**
 * DTO untuk mengirim data saat membuat pengajuan baru (POST /pengajuan)
 */
data class PengajuanRequestDto(
    @Json(name = "tujuanSurat")
    val tujuanSurat: String
)

/**
 * DTO untuk update status oleh Admin/Kemahasiswaan (PATCH /pengajuan/{id})
 */
data class PengajuanUpdateStatusDto(
    @Json(name = "status")
    val status: String
)

/**
 * DTO untuk menerima pesan sukses/error (Payload)
 */
data class RequestPengajuan(
    @Json(name = "message")
    val message: String,
    @Json(name = "status")
    val status: String
)