package com.example.uas.data.repository

import com.example.uas.model.request.LoginRequest
import com.example.uas.model.response.LoginResponse
import com.example.uas.service.ApiService
import retrofit2.Response

class AuthRepository(private val apiService: ApiService) {

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return apiService.login(loginRequest)
    }
}
