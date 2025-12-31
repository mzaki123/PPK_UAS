package com.example.uas.data.repository

import com.example.uas.model.Pengajuan
import com.example.uas.model.request.PengajuanUpdateStatusRequest
import com.example.uas.service.ApiService
import retrofit2.Response

class PengajuanRepository(private val apiService: ApiService) {

    /**
     * Mengambil semua pengajuan (Role: ADMIN/KEMAHASISWAAN)
     */
    suspend fun getAllPengajuan(): Response<List<Pengajuan>> {
        return apiService.getAllPengajuan()
    }

    /**
     * Mengambil riwayat pengajuan saya (Role: MAHASISWA)
     */
    suspend fun getMyPengajuan(): Response<List<Pengajuan>> {
        return apiService.getMyPengajuan()
    }

    /**
     * Mengupdate status pengajuan (Role: ADMIN/KEMAHASISWAAN)
     * Contoh status: "SELESAI", "DITOLAK"
     */
    suspend fun updateStatus(id: Long, status: String): Response<Pengajuan> {
        val request = PengajuanUpdateStatusRequest(status = status)
        return apiService.updateStatusPengajuan(id, request)
    }
}