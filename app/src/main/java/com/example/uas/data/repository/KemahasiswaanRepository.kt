package com.example.uas.data.repository

import com.example.uas.model.request.PengajuanUpdateStatusRequest
import com.example.uas.service.ApiService

class KemahasiswaanRepository(private val apiService: ApiService) {

    suspend fun getAllPengajuan() = apiService.getAllPengajuan()

    suspend fun getPengajuanById(id: Long) = apiService.getPengajuanById(id)

    suspend fun updatePengajuanStatus(id: Long, status: String) =
        apiService.updatePengajuanStatus(id, PengajuanUpdateStatusRequest(status))
}
