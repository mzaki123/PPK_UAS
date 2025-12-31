package com.example.uas.data.repository

import com.example.uas.model.User
import com.example.uas.service.ApiService
import retrofit2.Response

class UserRepository(private val apiService: ApiService) {

    /**
     * Mengambil semua user (Role: ADMIN)
     * Token otomatis ditempel oleh OkHttpClient Interceptor
     */
    suspend fun getAllUsers(): Response<List<User>> {
        return apiService.getAllUsers()
    }

    /**
     * Menghapus user berdasarkan ID
     * Sesuai dengan @DeleteMapping("/users/{id}") di UserController.java backend
     */
    suspend fun deleteUser(id: Long): Response<Unit> {
        return apiService.deleteUser(id)
    }

}