package com.example.uas.ui.navigation

// Single source of truth for navigation routes
object Routes {
    // Authentication
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Mahasiswa Flow
    const val HOME = "home"
    const val HISTORY = "history"
    const val PROFILE = "profile"
    const val FORM_PENGAJUAN = "form_pengajuan"
    const val EDIT_PROFILE = "edit_profile"
    const val CHANGE_PASSWORD = "change_password"
    const val DETAIL = "detail"


    const val KEMAHASISWAAN_DASHBOARD = "kemahasiswaan_dashboard"
    const val KEMAHASISWAAN_DAFTAR_PENGAJUAN = "kemahasiswaan_daftar_pengajuan"
    const val KEMAHASISWAAN_PROFIL = "kemahasiswaan_profil"

    const val KEMAHASISWAAN_DETAIL_PENGAJUAN = "kemahasiswaan_detail_pengajuan"

    // Admin Flow
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_MANAJEMEN_USER = "admin_manajemen_user"
    const val ADMIN_RIWAYAT = "admin_riwayat"
    const val ADMIN_SETTINGS = "admin_settings"
}
