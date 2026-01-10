package com.example.uas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uas.data.repository.UserRepository
import com.example.uas.service.RetrofitInstance
import com.example.uas.ui.admin.home.AdminDashboardScreen
import com.example.uas.ui.admin.riwayat.RiwayatPengajuanAdminScreen
import com.example.uas.ui.admin.settings.AdminProfileScreen
import com.example.uas.ui.admin.user.DetailUserScreen
import com.example.uas.ui.admin.user.ManajemenUserScreen
import com.example.uas.ui.admin.user.UserViewModel
import com.example.uas.ui.shared.profile.ChangePasswordScreen

// --- FIX 1: Samakan rute Profile dengan Routes.kt ---
sealed class AdminScreen(val route: String, val title: String, val icon: ImageVector) {
    object Home : AdminScreen(Routes.ADMIN_DASHBOARD, "Home", Icons.Default.Home)
    object Users : AdminScreen(Routes.ADMIN_MANAJEMEN_USER, "Users", Icons.Default.SupervisedUserCircle)
    object History : AdminScreen(Routes.ADMIN_RIWAYAT, "Riwayat", Icons.Default.History)
    object Profile : AdminScreen(Routes.ADMIN_SETTINGS, "Profil", Icons.Default.Person)
}

val adminItems = listOf(
    AdminScreen.Home,
    AdminScreen.Users,
    AdminScreen.History,
    AdminScreen.Profile,
)

@Composable
fun AdminNavigation(onLogout: () -> Unit) {
    val navController = rememberNavController()

    val userViewModelFactory = remember {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return UserViewModel(UserRepository(RetrofitInstance.api)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    val userViewModel: UserViewModel = viewModel(factory = userViewModelFactory)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                adminItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        // FIX 2: Supaya tab tetep nyala walau lagi di sub-halaman
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AdminScreen.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(AdminScreen.Home.route) { AdminDashboardScreen() }

            composable(AdminScreen.Users.route) {
                ManajemenUserScreen(
                    userViewModel = userViewModel,
                    onUserClick = { userId ->
                        navController.navigate("admin_user_detail/$userId")
                    }
                )
            }

            composable(AdminScreen.History.route) { RiwayatPengajuanAdminScreen() }

            composable(AdminScreen.Profile.route) {
                AdminProfileScreen(
                    navController = navController,
                    onLogout = onLogout
                )
            }

            // FIX 3: Pastikan rute pendukung terdaftar agar tidak crash
            composable(Routes.CHANGE_PASSWORD) {
                ChangePasswordScreen(navController = navController)
            }

            composable(
                route = "admin_user_detail/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.LongType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
                DetailUserScreen(
                    userId = userId,
                    userViewModel = userViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
