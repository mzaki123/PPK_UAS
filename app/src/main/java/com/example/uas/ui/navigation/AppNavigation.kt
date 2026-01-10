package com.example.uas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.uas.data.SessionManager
import com.example.uas.ui.auth.login.LoginScreen
import com.example.uas.ui.auth.login.LoginViewModel
import com.example.uas.ui.auth.register.RegisterScreen
import com.example.uas.ui.auth.register.RegisterViewModel

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

    // Ambil token & role dari SessionManager untuk menentukan layar awal
    val token = SessionManager.getToken()
    val role = SessionManager.getRole()?.uppercase()

    val startGraph = if (token != null) {
        when (role) {
            "MAHASISWA" -> Graph.MAHASISWA
            "KEMAHASISWAAN" -> Graph.KEMAHASISWAAN
            "ADMIN" -> Graph.ADMIN
            else -> Graph.AUTHENTICATION
        }
    } else {
        Graph.AUTHENTICATION
    }

    NavHost(navController = navController, startDestination = startGraph) {

        // --- GRAPH AUTHENTICATION ---
        navigation(startDestination = Routes.LOGIN, route = Graph.AUTHENTICATION) {
            composable(Routes.LOGIN) {
                // Inisialisasi ViewModel di sini lur
                val loginViewModel: LoginViewModel = viewModel()

                LoginScreen(
                    viewModel = loginViewModel,
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
                // Inisialisasi ViewModel di sini lur
                val registerViewModel: RegisterViewModel = viewModel()

                RegisterScreen(
                    viewModel = registerViewModel,
                    onBackClick = { navController.popBackStack() },
                    onRegisterSuccess = {
                        // Setelah sukses daftar, arahkan ke login
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    }
                )
            }
        }

        // --- GRAPH MAHASISWA ---
        composable(Graph.MAHASISWA) {
            MahasiswaNavigation(onLogout = {
                SessionManager.logout()
                navController.navigate(Graph.AUTHENTICATION) {
                    popUpTo(Graph.MAHASISWA) { inclusive = true }
                }
            })
        }

        // --- GRAPH KEMAHASISWAAN ---
        composable(Graph.KEMAHASISWAAN) {
            KemahasiswaanNavigation(
                appNavController = navController,
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