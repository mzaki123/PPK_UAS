package com.example.uas.ui.shared.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.SessionManager
import com.example.uas.model.User // Pastikan file User.kt sudah ada di data/model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Sealed class untuk merepresentasikan state UI, letakkan di luar class ViewModel
sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: User) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

// Deklarasi class ViewModel yang benar
class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        // Panggil fungsi untuk memuat data pengguna saat ViewModel dibuat
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            // TODO: Nanti di sini kita akan memanggil UserRepository untuk mengambil data user dari API.
            // Untuk sekarang, kita pakai data dummy berdasarkan peran dari SessionManager.

            val role = SessionManager.getRole()
            val user = when (role?.uppercase()) {
                "KEMAHASISWAAN" -> User(
                    name = "Dr. Budi Raharjo, M.Kom",
                    email = "budi.raharjo@staff.univ.ac.id",
                    role = "Kemahasiswaan",
                    nip = "198503152008011001"
                )
                "ADMIN" -> User(
                    name = "Siti Aminah, S.Kom",
                    email = "admin.siti@univ.ac.id",
                    role = "Admin"
                )
                else -> User( // Default ke Mahasiswa
                    name = "Ahmad Dahlan",
                    email = "ahmad.d@student.univ.ac.id",
                    role = "Mahasiswa",
                    nim = "21041001",
                )
            }
            _uiState.value = ProfileUiState.Success(user)
        }
    }
}
