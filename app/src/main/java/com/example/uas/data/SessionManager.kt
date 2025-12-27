package com.example.uas.data

object SessionManager {
    var authToken: String? = null
    var userRole: String? = null

    fun login(token: String, role: String) {
        authToken = "Bearer $token" // Tambahkan prefix "Bearer "
        userRole = role
    }

    fun logout() {
        authToken = null
        userRole = null
    }
}
