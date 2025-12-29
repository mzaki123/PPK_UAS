package com.example.uas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uas.data.repository.MahasiswaRepository
import com.example.uas.service.RetrofitInstance
import com.example.uas.ui.components.BottomNavigationBar
import com.example.uas.ui.mahasiswa.MahasiswaViewModel
import com.example.uas.ui.mahasiswa.home.HomeScreen
import com.example.uas.ui.mahasiswa.pengajuan.FormScreen
import com.example.uas.ui.mahasiswa.pengajuan.HistoryScreen
import com.example.uas.ui.shared.profile.ProfileScreen

@Composable
fun MahasiswaNavigation(onLogout: () -> Unit) {
    val navController = rememberNavController()

    val viewModelFactory = remember {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MahasiswaViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return MahasiswaViewModel(MahasiswaRepository(RetrofitInstance.api)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    val mahasiswaViewModel: MahasiswaViewModel = viewModel(factory = viewModelFactory)

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) { HomeScreen(navController) }
            composable(Routes.HISTORY) {
                HistoryScreen(
                    navController = navController,
                    viewModel = mahasiswaViewModel
                )
            }
            composable(Routes.PROFILE) { ProfileScreen(navController, onLogout = onLogout) }
            composable(Routes.FORM_PENGAJUAN) {
                FormScreen(
                    navController = navController,
                    viewModel = mahasiswaViewModel
                )
            }
        }
    }
}
