package com.example.uas.service

import com.example.uas.model.ChangePasswordRequest
import com.example.uas.model.KemahasiswaanDto
import com.example.uas.model.MahasiswaDto
import com.example.uas.model.Pengajuan
import com.example.uas.model.StandardResponse
import com.example.uas.model.User
import com.example.uas.model.request.*
import com.example.uas.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // --- AUTHENTICATION ---
    // Ditambahkan agar AuthRepository bisa melakukan registrasi
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    // --- USER MANAGEMENT ---
    @GET("users")
    suspend fun getAllUsers(): Response<List<User>>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: Long): Response<Unit>

    // --- PENGAJUAN (ADMIN/KEMAHASISWAAN) ---
    @GET("pengajuan/all")
    suspend fun getAllPengajuan(): Response<List<Pengajuan>>

    @PATCH("pengajuan/{id}")
    suspend fun updateStatusPengajuan(
        @Path("id") id: Long,
        @Body request: PengajuanUpdateStatusRequest
    ): Response<Pengajuan>

    // --- PENGAJUAN (MAHASISWA) ---
    @GET("pengajuan")
    suspend fun getMyPengajuan(): Response<List<Pengajuan>>

    @Multipart
    @POST("pengajuan")
    suspend fun createPengajuan(
        @Part("tujuanSurat") tujuan: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<Unit>

    @POST("changepassword")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<StandardResponse>

    // --- 2. MAHASISWA PROFILE ---
    @GET("mahasiswa")
    suspend fun getMahasiswaProfile(): Response<MahasiswaDto>

    @PATCH("mahasiswa")
    suspend fun updateMahasiswaProfile(@Body request: MahasiswaDto): Response<StandardResponse>

    // --- 3. KEMAHASISWAAN PROFILE ---
    @GET("kemahasiswaan")
    suspend fun getKemahasiswaanProfile(): Response<KemahasiswaanDto>

    @PATCH("kemahasiswaan")
    suspend fun updateKemahasiswaanProfile(@Body request: KemahasiswaanDto): Response<StandardResponse>
}