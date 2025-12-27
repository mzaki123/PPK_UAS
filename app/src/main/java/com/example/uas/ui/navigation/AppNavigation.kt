package com.example.uas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.siaktif.ui.admin.AdminDashboardScreen
import com.example.siaktif.ui.admin.ManajemenUserScreen
import com.example.siaktif.ui.admin.RiwayatPengajuanAdminScreen
import com.example.siaktif.ui.admin.SettingsAdminScreen
import com.example.uas.ui.components.BottomNavigationBar
import com.example.uas.ui.home.HomeScreen
import com.example.uas.ui.login.LoginScreen
import com.example.uas.ui.register.RegisterScreen
import com.example.uas.ui.screens.ChangePasswordScreen
import com.example.uas.ui.screens.DetailScreen
import com.example.uas.ui.screens.EditProfileScreen
import com.example.uas.ui.screens.FormScreen
import com.example.uas.ui.screens.HistoryScreen
import com.example.uas.ui.screens.ProfileScreen

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val HISTORY = "history"
    const val PROFILE = "profile"
    const val DETAIL = "detail"
    const val EDIT_PROFILE = "edit_profile"
    const val CHANGE_PASSWORD = "change_password"
    const val FORM_PENGAJUAN = "form_pengajuan"

    // Admin Routes
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_MANAJEMEN_USER = "admin_manajemen_user"
    const val ADMIN_RIWAYAT = "admin_riwayat"
    const val ADMIN_SETTINGS = "admin_settings"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val routesWithUserBottomNav = listOf(AppRoutes.HOME, AppRoutes.HISTORY, AppRoutes.PROFILE)
    val routesWithAdminBottomNav = listOf(
        AppRoutes.ADMIN_DASHBOARD,
        AppRoutes.ADMIN_MANAJEMEN_USER,
        AppRoutes.ADMIN_RIWAYAT,
        AppRoutes.ADMIN_SETTINGS
    )

    Scaffold(
        bottomBar = {
            when {
                currentRoute in routesWithUserBottomNav -> BottomNavigationBar(navController = navController)
                currentRoute in routesWithAdminBottomNav -> AdminBottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoutes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = { role ->
                        // Navigate based on role, assuming role is returned from LoginScreen
                        val destination = if (role == "admin") AppRoutes.ADMIN_DASHBOARD else AppRoutes.HOME
                        navController.navigate(destination) {
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

            // --- User Flow ---
            composable(AppRoutes.HOME) { HomeScreen(navController = navController) }
            composable(AppRoutes.HISTORY) { HistoryScreen(navController = navController) }
            composable(AppRoutes.PROFILE) { ProfileScreen(navController = navController) }
            composable(AppRoutes.DETAIL) { DetailScreen(navController = navController) }
            composable(AppRoutes.FORM_PENGAJUAN) { FormScreen(navController = navController) }
            composable(AppRoutes.EDIT_PROFILE) { EditProfileScreen(navController = navController) }
            composable(AppRoutes.CHANGE_PASSWORD) { ChangePasswordScreen(navController = navController) }

            // --- Admin Flow ---
            composable(AppRoutes.ADMIN_DASHBOARD) { AdminDashboardScreen() }
            composable(AppRoutes.ADMIN_MANAJEMEN_USER) { ManajemenUserScreen() }
            composable(AppRoutes.ADMIN_RIWAYAT) { RiwayatPengajuanAdminScreen() }
            composable(AppRoutes.ADMIN_SETTINGS) { SettingsAdminScreen() }
        }
    }
}


@Composable
fun AdminBottomNavigationBar(navController: NavController) {
    val items = listOf(
        AdminBottomNavItem("Home", Icons.Default.Home, AppRoutes.ADMIN_DASHBOARD),
        AdminBottomNavItem("Users", Icons.Default.Group, AppRoutes.ADMIN_MANAJEMEN_USER),
        AdminBottomNavItem("Riwayat", Icons.Default.History, AppRoutes.ADMIN_RIWAYAT),
        AdminBottomNavItem("Settings", Icons.Default.Settings, AppRoutes.ADMIN_SETTINGS)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Avoid multiple copies of the same destination when re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                        // Pop up to the start destination of the graph to avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
            )
        }
    }
}

data class AdminBottomNavItem(val title: String, val icon: ImageVector, val route: String)
