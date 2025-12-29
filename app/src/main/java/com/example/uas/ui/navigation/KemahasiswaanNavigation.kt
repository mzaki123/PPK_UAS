package com.example.uas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// --- IMPORT BOTTOM BAR YANG BARU ---
import com.example.uas.ui.components.KemahasiswaanBottomNavigationBar
import com.example.uas.ui.kemahasiswaan.daftar.DaftarPengajuanScreen
import com.example.uas.ui.kemahasiswaan.detail.DetailPengajuanScreen
import com.example.uas.ui.kemahasiswaan.home.DashboardKemahasiswaanScreen
import com.example.uas.ui.shared.profile.ProfileScreen

@Composable
fun KemahasiswaanNavigation(appNavController: NavController,onLogout: () -> Unit) { // Terima NavController utama untuk logout
    val navController = rememberNavController() // Buat NavController LOKAL untuk alur ini
    Scaffold(
        // --- GUNAKAN BOTTOM BAR YANG BARU ---
        bottomBar = { KemahasiswaanBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController, // Gunakan NavController LOKAL
            startDestination = Routes.KEMAHASISWAAN_DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Daftarkan semua layar yang bisa diakses dari bottom bar ini
            composable(Routes.KEMAHASISWAAN_DASHBOARD) { DashboardKemahasiswaanScreen(navController) }
            composable(Routes.KEMAHASISWAAN_DAFTAR_PENGAJUAN) { DaftarPengajuanScreen(navController) }

            // Profil sekarang menggunakan layar shared, dan kita berikan onLogout
            composable(Routes.KEMAHASISWAAN_PROFIL) {
                ProfileScreen(
                    navController = navController,
                    onLogout = {
                        // Gunakan NavController UTAMA untuk proses logout
                        appNavController.navigate(Graph.AUTHENTICATION) {
                            popUpTo(Graph.KEMAHASISWAAN) { inclusive = true }
                        }
                    }
                )
            }

            // Daftarkan juga layar detail
            composable("${Routes.KEMAHASISWAAN_DETAIL_PENGAJUAN}/{pengajuanId}") { backStackEntry ->
                val pengajuanId = backStackEntry.arguments?.getString("pengajuanId")
                DetailPengajuanScreen(navController = navController)
                // TODO: Berikan pengajuanId ke ViewModel
            }
        }
    }
}
