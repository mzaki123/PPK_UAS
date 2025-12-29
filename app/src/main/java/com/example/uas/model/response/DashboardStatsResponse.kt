package com.example.uas.model.response

import com.squareup.moshi.Json

data class DashboardStatsResponse(
    @Json(name = "totalUsers")
    val totalUsers: Long,
    @Json(name = "mahasiswa")
    val mahasiswa: Long,
    @Json(name = "kemahasiswaan")
    val kemahasiswaan: Long,
    @Json(name = "pengajuan")
    val pengajuan: Long
)
