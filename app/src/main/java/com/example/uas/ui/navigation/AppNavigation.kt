package com.example.uas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.uas.data.SessionManager
import com.example.uas.ui.auth.login.LoginScreen
import com.example.uas.ui.auth.register.RegisterScreen

// Grafik Navigasi Utama
object Graph {
    const val AUTHENTICATION = "auth_graph"
    const val MAHASISWA = "mahasiswa_graph"
    const val KEMAHASISWAAN = "kemahasiswaan_graph"
    const val ADMIN = "admin_graph"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Cek status login saat aplikasi dibuka
    val startGraph = if (SessionManager.getToken() != null) {
        when (SessionManager.getRole()?.uppercase()) {
            "MAHASISWA" -> Graph.MAHASISWA
            "KEMAHASISWAAN" -> Graph.KEMAHASISWAAN
            "ADMIN" -> Graph.ADMIN
            else -> Graph.AUTHENTICATION
        }
    } else {
        Graph.AUTHENTICATION
    }

    NavHost(navController = navController, startDestination = startGraph) {

        // --- GRAPH AUTH ---
        navigation(startDestination = Routes.LOGIN, route = Graph.AUTHENTICATION) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = { userRole ->
                        val destination = when (userRole.uppercase()) {
                            "MAHASISWA" -> Graph.MAHASISWA
                            "KEMAHASISWAAN" -> Graph.KEMAHASISWAAN
                            "ADMIN" -> Graph.ADMIN
                            else -> Graph.AUTHENTICATION
                        }
                        navController.navigate(destination) {
                            popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                        }
                    },
                    navigateToRegister = { navController.navigate(Routes.REGISTER) }
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    onBackClick = { navController.popBackStack() },
                    onLoginClick = { navController.popBackStack() }
                )
            }
        }

        // --- GRAPH MAHASISWA ---
        composable(Graph.MAHASISWA) {
            MahasiswaNavigation(onLogout = {
                SessionManager.logout() // Samakan jadi logout()
                navController.navigate(Graph.AUTHENTICATION) {
                    popUpTo(Graph.MAHASISWA) { inclusive = true }
                }
            })
        }

        // --- GRAPH KEMAHASISWAAN ---
        composable(Graph.KEMAHASISWAAN) {
            KemahasiswaanNavigation(
                appNavController = navController, // Pastikan di file KemahasiswaanNavigation menerima parameter ini
                onLogout = {
                    SessionManager.logout()
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.KEMAHASISWAAN) { inclusive = true }
                    }
                }
            )
        }

        // --- GRAPH ADMIN ---
        composable(Graph.ADMIN) {
            AdminNavigation(onLogout = {
                SessionManager.logout()
                navController.navigate(Graph.AUTHENTICATION) {
                    popUpTo(Graph.ADMIN) { inclusive = true }
                }
            })
        }
    }
}