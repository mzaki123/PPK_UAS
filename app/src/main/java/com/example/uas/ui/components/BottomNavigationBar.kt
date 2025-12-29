package com.example.uas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.uas.ui.navigation.Routes



sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(Routes.HOME, Icons.Filled.Home, "Home")
    object History : BottomNavItem(Routes.HISTORY, Icons.Filled.History, "History")
    object Profile : BottomNavItem(Routes.PROFILE, Icons.Filled.Person, "Profile")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.History,
        BottomNavItem.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val isHistoryTab = item.route == Routes.HISTORY
            val isCurrentlyOnHistoryScreen = currentRoute == Routes.HISTORY

            if (isHistoryTab && isCurrentlyOnHistoryScreen) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Ajukan Surat Baru") },
                    label = { Text("Ajukan") },
                    selected = true,
                    onClick = {
                        navController.navigate(Routes.FORM_PENGAJUAN)
                    }
                )
            } else {
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}


sealed class KemahasiswaanBottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Dashboard : KemahasiswaanBottomNavItem(Routes.KEMAHASISWAAN_DASHBOARD, Icons.Filled.Dashboard, "Dashboard")
    object DaftarPengajuan : KemahasiswaanBottomNavItem(Routes.KEMAHASISWAAN_DAFTAR_PENGAJUAN, Icons.Filled.ListAlt, "Daftar")
    object Profil : KemahasiswaanBottomNavItem(Routes.KEMAHASISWAAN_PROFIL, Icons.Filled.Person, "Profil")
}

// 2. Buat Composable Bottom Bar khusus untuk Kemahasiswaan
@Composable
fun KemahasiswaanBottomNavigationBar(navController: NavController) {
    val items = listOf(
        KemahasiswaanBottomNavItem.Dashboard,
        KemahasiswaanBottomNavItem.DaftarPengajuan,
        KemahasiswaanBottomNavItem.Profil
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Logika navigasi agar tidak menumpuk back stack
                        popUpTo(navController.graph.startDestinationId) {
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

