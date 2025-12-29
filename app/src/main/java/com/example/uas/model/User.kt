package com.example.uas.model

// Cetak biru untuk semua jenis pengguna
data class User(
    val name: String,
    val email: String,
    val role: String,
    // Properti opsional (bisa null) yang mungkin hanya dimiliki oleh peran tertentu
    val nim: String? = null,    // Hanya untuk mahasiswa
    val nip: String? = null,    // Hanya untuk kemahasiswaan/admin
)
