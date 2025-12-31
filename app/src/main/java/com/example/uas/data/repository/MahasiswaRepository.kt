package com.example.uas.data.repository

import com.example.uas.model.MahasiswaDto
import com.example.uas.model.Pengajuan
import com.example.uas.model.request.CreatePengajuanRequest
import com.example.uas.service.ApiService
import retrofit2.Response

class MahasiswaRepository(private val apiService: ApiService) {

    /**
     * Mengambil riwayat pengajuan milik mahasiswa yang sedang login
     * Token otomatis ditempel oleh Interceptor
     */
    suspend fun getMyPengajuan(): Response<List<Pengajuan>> {
        return apiService.getMyPengajuan()
    }

    /**
     * Membuat pengajuan surat baru
     */
    suspend fun createPengajuan(tujuan: String): Response<Unit> {
        val request = CreatePengajuanRequest(tujuanSurat = tujuan)
        return apiService.createPengajuan(request)
    }

    suspend fun getMyProfile(): Response<MahasiswaDto> {
        return apiService.getMahasiswaProfile()
    }

}