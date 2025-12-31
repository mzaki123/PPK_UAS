package com.example.uas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uas.data.repository.MahasiswaRepository
import com.example.uas.data.repository.ProfileRepository
import com.example.uas.service.RetrofitInstance
import com.example.uas.ui.components.BottomNavigationBar
import com.example.uas.ui.mahasiswa.MahasiswaViewModel
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

    // --- SHARED VIEWMODEL MAHASISWA ---
    val viewModelFactory = remember {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return MahasiswaViewModel(MahasiswaRepository(RetrofitInstance.api)) as T
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
            // 1. Dashboard Mahasiswa
            composable(Routes.HOME) {
                HomeScreen(navController, viewModel = mahasiswaViewModel)
            }

            // 2. Form Pengajuan Baru
            composable(Routes.FORM_PENGAJUAN) {
                FormPengajuanScreen(
                    navController = navController,
                    viewModel = mahasiswaViewModel
                )
            }

            // 3. Riwayat Pengajuan Saya
            composable(Routes.HISTORY) {
                HistoryScreen(navController, viewModel = mahasiswaViewModel)
            }

            // 4. Profil
            composable(Routes.PROFILE) {
                ProfileScreen(navController, onLogout = onLogout)
            }

            composable(Routes.CHANGE_PASSWORD) {
                ChangePasswordScreen(navController = navController)
            }

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


        }
    }
}

