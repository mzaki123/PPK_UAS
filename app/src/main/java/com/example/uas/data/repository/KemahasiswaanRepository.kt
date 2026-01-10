package com.example.uas.data.repository

import com.example.uas.model.KemahasiswaanDto
import com.example.uas.model.Pengajuan
import com.example.uas.model.request.PengajuanUpdateStatusRequest
import com.example.uas.service.ApiService
import retrofit2.Response

class KemahasiswaanRepository(private val apiService: ApiService) {

    /**
     * Mengambil semua pengajuan mahasiswa.
     * Token otomatis ditempel oleh Interceptor.
     */
    suspend fun getAllPengajuan(): Response<List<Pengajuan>> {
        return apiService.getAllPengajuan()
    }

    /**
     * Mengubah status pengajuan (SELESAI / DITOLAK).
     */
    suspend fun updatePengajuanStatus(id: Long, status: String): Response<Pengajuan> {
        val request = PengajuanUpdateStatusRequest(status = status)
        return apiService.updateStatusPengajuan(id, request)
    }

    suspend fun getKemahasiswaanProfile(): Response<KemahasiswaanDto> {
        return apiService.getKemahasiswaanProfile()    }
}

