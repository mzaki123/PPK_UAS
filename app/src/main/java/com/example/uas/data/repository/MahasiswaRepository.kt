package com.example.uas.data.repository

import com.example.uas.service.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MahasiswaRepository(private val apiService: ApiService) {

    suspend fun getMyProfile() = apiService.getMahasiswaProfile()

    suspend fun getMyPengajuan() = apiService.getMyPengajuan()

    // SINKRONISASI: Menerima Multipart & RequestBody dari ViewModel
    suspend fun createPengajuanWithFile(tujuan: RequestBody, file: MultipartBody.Part) =
        apiService.createPengajuan(tujuan, file)
}