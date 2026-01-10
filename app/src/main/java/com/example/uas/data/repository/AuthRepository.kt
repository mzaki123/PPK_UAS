package com.example.uas.data.repository

import com.example.uas.model.request.LoginRequest
import com.example.uas.model.request.RegisterRequest
import com.example.uas.model.response.LoginResponse
import com.example.uas.model.response.RegisterResponse
import com.example.uas.service.ApiService
import retrofit2.Response

class AuthRepository(private val apiService: ApiService) {

    /**
     * Fungsi untuk Login
     */
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return apiService.login(loginRequest)
    }


    suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> {
        return apiService.register(registerRequest)
    }
}