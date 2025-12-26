package com.example.uas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uas.ui.home.HomeScreen
import com.example.uas.ui.login.LoginScreen
import com.example.uas.ui.register.RegisterScreen

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.HOME) {
                        // Hapus semua halaman sebelumnya dari tumpukan navigasi
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
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
        composable(AppRoutes.HOME) {
            HomeScreen()
        }
    }
}
