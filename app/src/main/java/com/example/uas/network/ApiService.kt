package com.example.uas.network

import com.example.uas.network.dto.LoginRequest
import com.example.uas.network.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    
}
