package com.example.uas.data.repository

import com.example.uas.service.ApiService

class UserRepository(private val apiService: ApiService) {

    suspend fun getAllUsers() = apiService.getAllUsers()

    suspend fun getUserById(userId: Long) = apiService.getUserById(userId)

    suspend fun deleteUser(userId: Long) = apiService.deleteUser(userId)
}
