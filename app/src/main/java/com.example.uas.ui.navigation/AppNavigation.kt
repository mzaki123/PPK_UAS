package com.example.uas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.uas.ui.auth.login.LoginScreen
import com.example.uas.ui.auth.register.RegisterScreen
import com.example.uas.ui.kemahasiswaan.DashboardKemahasiswaanScreen
import com.example.uas.ui.mahasiswa.home.HomeScreen

// Rute utama aplikasi
object Graph {
    const val AUTHENTICATION = "auth_graph"
    const val MAHASISWA = "mahasiswa_graph"
    const val KEMAHASISWAAN = "kemahasiswaan_graph"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Graph.AUTHENTICATION
    ) {
        // --- GRAFIK AUTENTIKASI ---
        navigation(
            startDestination = "login",
            route = Graph.AUTHENTICATION
        ) {
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { role ->
                        val destination = when (role) {
                            "MAHASISWA" -> Graph.MAHASISWA
                            "KEMAHASISWAAN" -> Graph.KEMAHASISWAAN
                            else -> Graph.AUTHENTICATION // Default fallback
                        }
                        navController.navigate(destination) {
                            popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                        }
                    },
                    navigateToRegister = { navController.navigate("register") }
                )
            }
            composable("register") {
                RegisterScreen(
                    onBackClick = { navController.popBackStack() },
                    onLoginClick = { navController.popBackStack() }
                )
            }
        }

        // --- GRAFIK MAHASISWA ---
        navigation(
            startDestination = "mahasiswa_dashboard",
            route = Graph.MAHASISWA
        ) {
            composable("mahasiswa_dashboard") {
                // TODO: Ganti dengan NavHost Mahasiswa yang sebenarnya jika sudah ada
                HomeScreen(navController = navController)
            }
            // Tambahkan rute lain untuk mahasiswa di sini
        }

        // --- GRAFIK KEMAHASISWAAN ---
        navigation(
            startDestination = "kemahasiswaan_dashboard",
            route = Graph.KEMAHASISWAAN
        ) {
            composable("kemahasiswaan_dashboard") {
                // TODO: Ganti dengan NavHost Kemahasiswaan yang sebenarnya
                 DashboardKemahasiswaanScreen()
            }
            // Tambahkan rute lain untuk kemahasiswaan di sini
        }
    }
}
