package com.example.uas.data.repository

import com.example.uas.service.ApiService

class PengajuanRepository(private val apiService: ApiService) {

    suspend fun getAllPengajuan() = apiService.getAllPengajuan()
}
