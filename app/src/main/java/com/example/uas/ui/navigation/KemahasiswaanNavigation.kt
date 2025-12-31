package com.example.uas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uas.data.repository.KemahasiswaanRepository
import com.example.uas.data.repository.ProfileRepository
import com.example.uas.service.RetrofitInstance
import com.example.uas.ui.components.KemahasiswaanBottomNavigationBar
import com.example.uas.ui.kemahasiswaan.KemahasiswaanViewModel
import com.example.uas.ui.kemahasiswaan.daftar.DaftarPengajuanScreen
import com.example.uas.ui.kemahasiswaan.detail.DetailPengajuanScreen
import com.example.uas.ui.kemahasiswaan.home.DashboardKemahasiswaanScreen
import com.example.uas.ui.shared.profile.ChangePasswordScreen
import com.example.uas.ui.shared.profile.EditProfileScreen
import com.example.uas.ui.shared.profile.EditProfileViewModel
import com.example.uas.ui.shared.profile.ProfileScreen
import com.example.uas.ui.shared.profile.ChangePasswordScreen


@Composable
fun KemahasiswaanNavigation(
    appNavController: NavHostController, // Digunakan untuk logout ke Auth Graph
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    // --- SHARED VIEWMODEL FACTORY ---
    val kemahasiswaanViewModel: KemahasiswaanViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return KemahasiswaanViewModel(KemahasiswaanRepository(RetrofitInstance.api)) as T
            }
        }
    )

    Scaffold(
        bottomBar = { KemahasiswaanBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.KEMAHASISWAAN_DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Dashboard
            composable(Routes.KEMAHASISWAAN_DASHBOARD) {
                DashboardKemahasiswaanScreen(
                    navController = navController,
                    viewModel = kemahasiswaanViewModel
                )
            }

            // 2. Daftar Pengajuan
            composable(Routes.KEMAHASISWAAN_DAFTAR_PENGAJUAN) {
                DaftarPengajuanScreen(
                    navController = navController,
                    viewModel = kemahasiswaanViewModel
                )
            }

            // 3. Profil Utama
            composable(Routes.KEMAHASISWAAN_PROFIL) {
                ProfileScreen(
                    navController = navController,
                    onLogout = onLogout
                )
            }

            // --- FITUR PROFIL TAMBAHAN ---

            // 4. Edit Profil (Gunakan Factory agar Repository masuk)
            composable(Routes.EDIT_PROFILE) {
                val editViewModel: EditProfileViewModel = viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return EditProfileViewModel(ProfileRepository(RetrofitInstance.api)) as T
                        }
                    }
                )
                EditProfileScreen(navController = navController, viewModel = editViewModel)
            }

            // 5. Ganti Password
            composable(Routes.CHANGE_PASSWORD) {
                ChangePasswordScreen(navController = navController)
            }

            // 6. Detail Pengajuan
            composable(
                route = "${Routes.KEMAHASISWAAN_DETAIL_PENGAJUAN}/{pengajuanId}",
                arguments = listOf(
                    navArgument("pengajuanId") { type = NavType.LongType }
                )
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