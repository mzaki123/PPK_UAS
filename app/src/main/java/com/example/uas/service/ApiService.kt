package com.example.uas.service

import com.example.uas.service.dto.LoginRequest
import com.example.uas.service.dto.LoginResponse
import com.example.uas.service.dto.PengajuanDto
import com.example.uas.service.dto.UpdateStatusRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginResponse(
    val token: String,
    val message: String
)
interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("pengajuan")
    suspend fun getPengajuanList(@Header("Authorization") token: String): List<PengajuanDto>

    @PUT("pengajuan/{id}")
    suspend fun updatePengajuanStatus(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: UpdateStatusRequest
    ): PengajuanDto
    
}
