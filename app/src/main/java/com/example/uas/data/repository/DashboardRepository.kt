package com.example.uas.data.repository

import com.example.uas.service.ApiService

class DashboardRepository(private val apiService: ApiService) {

    suspend fun getDashboardStats() = apiService.getDashboardStats()
}
