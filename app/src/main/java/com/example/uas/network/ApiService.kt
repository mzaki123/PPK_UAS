package com.example.uas.network

import com.example.uas.network.dto.LoginRequest
import com.example.uas.network.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginResponse(
    val token: String,
    val message: String
)
interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("pengajuan")
    suspend fun getPengajuanList(@Header("Authorization") token: String): List<PengajuanDto>

    @PUT("pengajuan/{id}")
    suspend fun updatePengajuanStatus(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: UpdateStatusRequest
    ): PengajuanDto
    
}
