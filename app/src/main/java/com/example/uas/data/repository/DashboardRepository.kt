package com.example.uas.data.repository

import com.example.uas.model.Pengajuan
import com.example.uas.model.User
import com.example.uas.service.ApiService
import retrofit2.Response

class DashboardRepository(private val apiService: ApiService) {

    /**
     * Mengambil semua user dari backend.
     * Token sudah ditempel otomatis oleh Interceptor di RetrofitInstance,
     * jadi kita tidak perlu ambil manual SessionManager.getToken() di sini.
     */
    suspend fun getAllUsers(): Response<List<User>> {
        return apiService.getAllUsers()
    }

    /**
     * Mengambil semua pengajuan dari backend.
     */
    suspend fun getAllPengajuan(): Response<List<Pengajuan>> {
        return apiService.getAllPengajuan()
    }

    // Fungsi getDashboardStats DIHAPUS karena backend tidak menyediakan endpoint-nya.
    // Statistik akan dihitung secara manual di ViewModel (Frontend Calculation).
}