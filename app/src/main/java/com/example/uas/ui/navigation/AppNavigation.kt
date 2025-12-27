package com.example.uas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.uas.ui.components.BottomNavigationBar
import com.example.uas.ui.home.HomeScreen
import com.example.uas.ui.login.LoginScreen
import com.example.uas.ui.register.RegisterScreen
import com.example.uas.ui.screens.ChangePasswordScreen
import com.example.uas.ui.screens.DetailScreen
import com.example.uas.ui.screens.EditProfileScreen
import com.example.uas.ui.screens.FormScreen
import com.example.uas.ui.screens.HistoryScreen // Pastikan Anda sudah membuat file ini
import com.example.uas.ui.screens.ProfileScreen  // Pastikan Anda sudah membuat file ini

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val HISTORY = "history"
    const val PROFILE = "profile"
    const val DETAIL = "detail"
    const val EDIT_PROFILE = "edit_profile"
    const val CHANGE_PASSWORD = "change_password"

    const val FORM_PENGAJUAN = "form_pengajuan" // <-- TAMBAHKAN BARIS INI
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Dapatkan rute saat ini untuk menentukan apakah BottomNav perlu ditampilkan
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Tentukan rute mana saja yang akan memiliki BottomNavigationBar
    val routesWithBottomNav = listOf(AppRoutes.HOME, AppRoutes.HISTORY, AppRoutes.PROFILE)

    // Gunakan Scaffold sebagai layout utama aplikasi
    Scaffold(
        bottomBar = {
            // HANYA tampilkan BottomNavigationBar jika rute saat ini ada dalam daftar
            if (currentRoute in routesWithBottomNav) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        // NavHost sekarang menjadi "konten" dari Scaffold
        NavHost(
            navController = navController,
            startDestination = AppRoutes.LOGIN, // Aplikasi tetap mulai dari Login
            modifier = Modifier.padding(innerPadding) // Berikan padding dari Scaffold
        ) {
            // --- Grafik Navigasi untuk Flow Autentikasi (TANPA BottomNav) ---
            composable(AppRoutes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        // Setelah login, pergi ke HOME dan hapus semua history login/register
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        }
                    },
                    onRegisterClick = { navController.navigate(AppRoutes.REGISTER) }
                )
            }
            composable(AppRoutes.REGISTER) {
                RegisterScreen(
                    onBackClick = { navController.popBackStack() },
                    onLoginClick = { navController.popBackStack() }
                )
            }

            // --- Grafik Navigasi untuk Flow Utama (DENGAN BottomNav) ---
            // Daftarkan SEMUA layar yang bisa diakses setelah login
            composable(AppRoutes.HOME) {
                // Panggil HomeScreen yang sudah disederhanakan (tanpa Scaffold)
                HomeScreen(navController = navController)
            }
            composable(AppRoutes.HISTORY) {
                // Panggil HistoryScreen yang sudah disederhanakan (tanpa Scaffold)
                HistoryScreen(navController = navController)
            }
            composable(AppRoutes.PROFILE) {
                // Panggil ProfileScreen yang sudah disederhanakan (tanpa Scaffold)
                ProfileScreen(navController = navController)
            }

            composable(AppRoutes.DETAIL) {
                 DetailScreen(navController = navController)
            }
            composable(AppRoutes.FORM_PENGAJUAN) {
                FormScreen(navController = navController)
            }
            composable(AppRoutes.EDIT_PROFILE) {
                EditProfileScreen(navController = navController)
            }
            composable(AppRoutes.CHANGE_PASSWORD) {
                ChangePasswordScreen(navController = navController)
            }


        }
    }
}
