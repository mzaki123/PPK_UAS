package com.example.uas.model

import com.squareup.moshi.Json

// 1. Model untuk Mahasiswa (Sesuai MahasiswaDto di Backend)
data class MahasiswaDto(
    @Json(name = "nama") val nama: String,
    @Json(name = "nim") val nim: String,
    @Json(name = "kelas") val kelas: String? = null,
    @Json(name = "jurusan") val jurusan: String? = null,
    @Json(name = "email") val email: String? = null
)

// 2. Model untuk Staff (Sesuai KemahasiswaanDto di Backend)
data class KemahasiswaanDto(
    @Json(name = "nama") val nama: String,
    @Json(name = "nip") val nip: String,
    @Json(name = "email") val email: String? = null
)

// 3. Model Request Ganti Password
data class ChangePasswordRequest(
    @Json(name = "oldPassword") val oldPassword: String,
    @Json(name = "newPassword") val newPassword: String
)

// 4. Model Response Standard (Sesuai ProfilMahasiswa/ProfilKemahasiswaan payload)
data class StandardResponse(
    @Json(name = "message") val message: String,
    @Json(name = "status") val status: String
)