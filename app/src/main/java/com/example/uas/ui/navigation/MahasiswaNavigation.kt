package com.example.uas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uas.ui.components.BottomNavigationBar
import com.example.uas.ui.mahasiswa.home.HomeScreen
import com.example.uas.ui.screens.HistoryScreen
import com.example.uas.ui.shared.profile.ProfileScreen // Corrected import

@Composable
fun MahasiswaNavigation(onLogout: () -> Unit) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) { HomeScreen(navController) }
            composable(Routes.HISTORY) { HistoryScreen(navController) }
            composable(Routes.PROFILE) { ProfileScreen(navController, onLogout = onLogout) } // Pass onLogout
        }
    }
}
