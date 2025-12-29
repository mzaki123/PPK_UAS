package com.example.uas.data

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREFS_NAME = "app_session_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_ROLE = "user_role"

    private var sharedPreferences: SharedPreferences? = null

    // Fungsi ini HARUS dipanggil sekali di awal aplikasi (misalnya di MainActivity)
    // untuk memberikan "kunci brankas" (Context).
    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    // Fungsi untuk menyimpan data saat login (sekarang disimpan ke file)
    fun login(token: String, role: String) {
        val editor = sharedPreferences?.edit()
        editor?.putString(KEY_TOKEN, "Bearer $token")
        editor?.putString(KEY_ROLE, role)
        editor?.apply()
    }

    // Fungsi untuk menghapus data saat logout (menghapus dari file)
    fun logout() {
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
    }

    // Fungsi untuk mendapatkan token dari file penyimpanan
    fun getToken(): String? {
        return sharedPreferences?.getString(KEY_TOKEN, null)
    }

    // Fungsi untuk mendapatkan peran (role) dari file penyimpanan
    fun getRole(): String? {
        return sharedPreferences?.getString(KEY_ROLE, null)
    }
}
