package com.example.uas.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager private constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: SessionManager? = null

        fun init(context: Context): SessionManager {
            return instance ?: synchronized(this) {
                instance ?: SessionManager(context).also { instance = it }
            }
        }

        fun getToken(): String? = instance?.prefs?.getString("access_token", null)
        fun getRole(): String? = instance?.prefs?.getString("user_role", null)

        /**
         * FIX: Mengubah parameter 'role' menjadi nullable (String?)
         * dan memberikan nilai default "MAHASISWA" jika null.
         */
        fun login(token: String, role: String? = null) {
            val finalRole = role ?: "MAHASISWA" // Proteksi jika role dari API null
            instance?.let {
                it.prefs.edit()
                    .putString("access_token", token)
                    .putString("user_role", finalRole)
                    .apply()
            }
        }

        // FIX: Kita pakai satu nama saja: logout()
        fun logout() {
            instance?.let {
                it.prefs.edit().clear().apply()
            }
        }

        // Alias agar tidak error jika terlanjur panggil clearSession
        fun clearSession() = logout()

        fun getInstance(): SessionManager {
            return instance ?: throw IllegalStateException("SessionManager belum di-init lur!")
        }
    }
}