package com.example.uas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uas.data.repository.MahasiswaRepository
import com.example.uas.data.repository.ProfileRepository
import com.example.uas.service.RetrofitInstance
import com.example.uas.ui.components.BottomNavigationBar
import com.example.uas.ui.mahasiswa.MahasiswaViewModel
import com.example.uas.ui.mahasiswa.detail.DetailPengajuanMahasiswaScreen
import com.example.uas.ui.mahasiswa.home.HomeScreen
import com.example.uas.ui.mahasiswa.form.FormPengajuanScreen
import com.example.uas.ui.mahasiswa.pengajuan.HistoryScreen
import com.example.uas.ui.shared.profile.ProfileScreen
import com.example.uas.ui.shared.profile.ChangePasswordScreen
import com.example.uas.ui.shared.profile.EditProfileScreen
import com.example.uas.ui.shared.profile.EditProfileViewModel

@Composable
fun MahasiswaNavigation(onLogout: () -> Unit) {
    val navController = rememberNavController()
    // Ambil Application Context agar aman dari memory leak lur
    val context = LocalContext.current.applicationContext

    // ViewModelFactory untuk MahasiswaViewModel
    val viewModelFactory = remember(context) {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MahasiswaViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    // Kirim context ke Repository jika memang dibutuhkan di sana
                    return MahasiswaViewModel(MahasiswaRepository(RetrofitInstance.api)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    // ViewModel ini bersifat Shared (dipakai semua screen di bawah)
    val mahasiswaViewModel: MahasiswaViewModel = viewModel(factory = viewModelFactory)

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(navController, viewModel = mahasiswaViewModel)
            }

            composable(Routes.FORM_PENGAJUAN) {
                FormPengajuanScreen(
                    navController = navController,
                    viewModel = mahasiswaViewModel
                )
            }

            composable(Routes.HISTORY) {
                HistoryScreen(navController, viewModel = mahasiswaViewModel)
            }

            composable(Routes.PROFILE) {
                ProfileScreen(navController, onLogout = onLogout)
            }

            composable(Routes.CHANGE_PASSWORD) {
                ChangePasswordScreen(navController = navController)
            }

            composable(Routes.EDIT_PROFILE) {
                // Factory lokal untuk EditProfile
                val editViewModel: EditProfileViewModel = viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return EditProfileViewModel(ProfileRepository(RetrofitInstance.api)) as T
                        }
                    }
                )
                EditProfileScreen(navController = navController, viewModel = editViewModel)
            }

            composable(
                route = "${Routes.DETAIL}/{pengajuanId}",
                arguments = listOf(navArgument("pengajuanId") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("pengajuanId") ?: 0L
                DetailPengajuanMahasiswaScreen(
                    navController = navController,
                    viewModel = mahasiswaViewModel,
                    pengajuanId = id
                )
            }
        }
    }
}