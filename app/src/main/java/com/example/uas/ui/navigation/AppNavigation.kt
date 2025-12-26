package com.example.uas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uas.ui.screens.DetailScreen
import com.example.uas.ui.screens.FormScreen
import com.example.uas.ui.screens.HistoryScreen
import com.example.uas.ui.screens.ProfileScreen
import com.example.uas.ui.home.HomeScreen
import com.example.uas.ui.login.LoginScreen
import com.example.uas.ui.register.RegisterScreen

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val HISTORY = "history"
    const val DETAIL = "detail"
    const val PROFILE = "profile"
    const val FORM = "form"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.HISTORY) {
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
        composable(AppRoutes.HISTORY) {
            HistoryScreen(navController = navController)
        }
        composable(AppRoutes.DETAIL) {
            DetailScreen(navController = navController)
        }
        composable(AppRoutes.PROFILE) {
            ProfileScreen(navController = navController)
        }
        composable(AppRoutes.FORM) {
            FormScreen(navController = navController)
        }
    }
}
