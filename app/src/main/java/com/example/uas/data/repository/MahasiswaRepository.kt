package com.example.uas.data.repository

import com.example.uas.model.request.CreatePengajuanRequest
import com.example.uas.service.ApiService

class MahasiswaRepository(private val apiService: ApiService) {

    suspend fun getMyPengajuan() = apiService.getMyPengajuan()

    suspend fun createPengajuan(tujuanSurat: String) =
        apiService.createPengajuan(CreatePengajuanRequest(tujuanSurat))
}
