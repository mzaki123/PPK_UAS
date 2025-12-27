package com.example.uas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add // <-- Tambahkan import ini
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
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
import com.example.uas.ui.navigation.AppRoutes

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(AppRoutes.HOME, Icons.Default.Home, "Home")
    object History : BottomNavItem(AppRoutes.HISTORY, Icons.Default.History, "History")
    object Profile : BottomNavItem(AppRoutes.PROFILE, Icons.Default.Person, "Profile")
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
            // --- INI BAGIAN UTAMA PERUBAHANNYA ---

            val isHistoryTab = item.route == AppRoutes.HISTORY
            val isCurrentlyOnHistoryScreen = currentRoute == AppRoutes.HISTORY

            // Jika ini adalah tab History DAN kita sedang berada di layar History,
            // maka ubah ikon dan fungsinya.
            if (isHistoryTab && isCurrentlyOnHistoryScreen) {
                // Tampilkan Tombol '+'
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Ajukan Surat Baru") },
                    label = { Text("Ajukan") },
                    selected = true, // Selalu aktif saat di layar History
                    onClick = {
                        // Arahkan ke Form Pengajuan
                        navController.navigate(AppRoutes.FORM_PENGAJUAN)
                    }
                )
            } else {
                // Tampilkan item navigasi biasa untuk Home, Profile,
                // atau History (saat tidak di layar History)
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
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
