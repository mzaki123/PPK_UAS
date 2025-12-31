package com.example.uas.service

import com.example.uas.model.ChangePasswordRequest
import com.example.uas.model.KemahasiswaanDto
import com.example.uas.model.MahasiswaDto
import com.example.uas.model.Pengajuan
import com.example.uas.model.StandardResponse
import com.example.uas.model.User
import com.example.uas.model.request.*
import com.example.uas.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // --- USER MANAGEMENT ---
    @GET("users")
    suspend fun getAllUsers(): Response<List<User>>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: Long): Response<Unit>

    // --- PENGAJUAN (ADMIN/KEMAHASISWAAN) ---
    // Sesuai dengan @GetMapping("/pengajuan/all") di PengajuanController.java
    @GET("pengajuan/all")
    suspend fun getAllPengajuan(): Response<List<Pengajuan>>

    // Sesuai dengan @PatchMapping("/pengajuan/{id}")
    @PATCH("pengajuan/{id}")
    suspend fun updateStatusPengajuan(
        @Path("id") id: Long,
        @Body request: PengajuanUpdateStatusRequest
    ): Response<Pengajuan>

    // --- PENGAJUAN (MAHASISWA) ---
    @GET("pengajuan")
    suspend fun getMyPengajuan(): Response<List<Pengajuan>>

    @POST("pengajuan")
    suspend fun createPengajuan(@Body request: CreatePengajuanRequest): Response<Unit>

    @POST("changepassword")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<StandardResponse>

    // --- 2. MAHASISWA PROFILE ---
    // Sesuai MahasiswaController.java @RequestMapping("/mahasiswa")
    @GET("mahasiswa")
    suspend fun getMahasiswaProfile(): Response<MahasiswaDto>

    @PATCH("mahasiswa")
    suspend fun updateMahasiswaProfile(@Body request: MahasiswaDto): Response<StandardResponse>

    // --- 3. KEMAHASISWAAN PROFILE ---
    // Sesuai KemahasiswaanController.java @RequestMapping("/kemahasiswaan")
    @GET("kemahasiswaan")
    suspend fun getKemahasiswaanProfile(): Response<KemahasiswaanDto>

    @PATCH("kemahasiswaan")
    suspend fun updateKemahasiswaanProfile(@Body request: KemahasiswaanDto): Response<StandardResponse>
}