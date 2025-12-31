package com.example.uas.data.repository

import com.example.uas.data.SessionManager
import com.example.uas.model.*
import com.example.uas.service.ApiService
import retrofit2.Response

class ProfileRepository(private val apiService: ApiService) {

    // Ganti Password (Berlaku untuk semua)
    suspend fun changePassword(old: String, new: String) =
        apiService.changePassword(ChangePasswordRequest(old, new))

    // Ambil profil secara dinamis
    suspend fun getProfileByRole(): Any? {
        val role = SessionManager.getRole()?.uppercase()
        return when (role) {
            "MAHASISWA" -> apiService.getMahasiswaProfile().body()
            "KEMAHASISWAAN" -> apiService.getKemahasiswaanProfile().body()
            else -> null // Admin biasanya tidak punya profil spesifik di controller kamu
        }
    }

    // Update profil secara dinamis
    suspend fun updateProfileByRole(data: Any): Response<StandardResponse> {
        val role = SessionManager.getRole()?.uppercase()
        return when (role) {
            "MAHASISWA" -> apiService.updateMahasiswaProfile(data as MahasiswaDto)
            "KEMAHASISWAAN" -> apiService.updateKemahasiswaanProfile(data as KemahasiswaanDto)
            else -> throw Exception("Role tidak didukung untuk edit profil")
        }
    }
}