package com.example.uas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uas.data.repository.KemahasiswaanRepository
import com.example.uas.service.RetrofitInstance
import com.example.uas.ui.components.KemahasiswaanBottomNavigationBar
import com.example.uas.ui.kemahasiswaan.KemahasiswaanViewModel
import com.example.uas.ui.kemahasiswaan.daftar.DaftarPengajuanScreen
import com.example.uas.ui.kemahasiswaan.detail.DetailPengajuanScreen
import com.example.uas.ui.kemahasiswaan.home.DashboardKemahasiswaanScreen
import com.example.uas.ui.shared.profile.ProfileScreen

@Composable
fun KemahasiswaanNavigation(appNavController: NavController, onLogout: () -> Unit) {
    val navController = rememberNavController()

    val viewModelFactory = remember {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(KemahasiswaanViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return KemahasiswaanViewModel(KemahasiswaanRepository(RetrofitInstance.api)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    val kemahasiswaanViewModel: KemahasiswaanViewModel = viewModel(factory = viewModelFactory)

    Scaffold(
        bottomBar = { KemahasiswaanBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.KEMAHASISWAAN_DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.KEMAHASISWAAN_DASHBOARD) { DashboardKemahasiswaanScreen(navController) }
            composable(Routes.KEMAHASISWAAN_DAFTAR_PENGAJUAN) {
                DaftarPengajuanScreen(
                    navController = navController,
                    viewModel = kemahasiswaanViewModel
                )
            }
            composable(Routes.KEMAHASISWAAN_PROFIL) {
                ProfileScreen(
                    navController = navController,
                    onLogout = onLogout
                )
            }
            composable(
                route = "${Routes.KEMAHASISWAAN_DETAIL_PENGAJUAN}/{pengajuanId}",
                arguments = listOf(navArgument("pengajuanId") { type = NavType.LongType })
            ) { backStackEntry ->
                val pengajuanId = backStackEntry.arguments?.getLong("pengajuanId") ?: 0L
                DetailPengajuanScreen(
                    navController = navController,
                    viewModel = kemahasiswaanViewModel,
                    pengajuanId = pengajuanId
                )
            }
        }
    }
}
