package com.example.uas.service

import com.example.uas.model.User
import com.example.uas.model.request.LoginRequest
import com.example.uas.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("users")
    suspend fun getAllUsers(): Response<List<User>>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Long): Response<User>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: Long): Response<Unit>

    @GET("pengajuan/all")
    suspend fun getAllPengajuan(): Response<List<com.example.uas.model.Pengajuan>>

    @GET("dashboard/stats")
    suspend fun getDashboardStats(): Response<com.example.uas.model.response.DashboardStatsResponse>

    @PATCH("pengajuan/{id}")
    suspend fun updatePengajuanStatus(
        @Path("id") pengajuanId: Long,
        @Body request: com.example.uas.model.request.PengajuanUpdateStatusRequest
    ): Response<com.example.uas.model.Pengajuan>

    @GET("pengajuan/{id}")
    suspend fun getPengajuanById(@Path("id") pengajuanId: Long): Response<com.example.uas.model.Pengajuan>

    @POST("pengajuan")
    suspend fun createPengajuan(@Body request: com.example.uas.model.request.CreatePengajuanRequest): Response<Unit>

    @GET("pengajuan")
    suspend fun getMyPengajuan(): Response<List<com.example.uas.model.Pengajuan>>
}
