package com.example.uas.ui.shared.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.SessionManager
import com.example.uas.model.User // <-- Pastikan import ini benar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Sealed class untuk UI state management
sealed class EditProfileUiState {
    object Idle : EditProfileUiState()
    object Loading : EditProfileUiState()
    object Success : EditProfileUiState()
    data class Error(val message: String) : EditProfileUiState()
}

class EditProfileViewModel : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState

    private val _updateState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Idle)
    val updateState: StateFlow<EditProfileUiState> = _updateState

    init {
        // Panggil fungsi untuk memuat data pengguna saat ViewModel dibuat
        loadInitialUserData()
    }

    private fun loadInitialUserData() {
        // Fungsi ini akan mengambil data user saat ini untuk mengisi form
        viewModelScope.launch {
            // TODO: Nanti, ambil data user yang sedang login dari API.
            // Untuk sekarang, kita pakai data dummy berdasarkan peran dari SessionManager.
            val role = SessionManager.getRole()
            _userState.value = when (role?.uppercase()) {
                "KEMAHASISWAAN" -> User(
                    name = "Dr. Budi Raharjo, M.Kom",
                    email = "budi.raharjo@staff.univ.ac.id",
                    role = "Kemahasiswaan", // <-- role diisi
                    nip = "198503152008011001"
                )
                "ADMIN" -> User(
                    name = "Siti Aminah, S.Kom",
                    email = "admin.siti@univ.ac.id",
                    role = "Admin" // <-- role diisi
                )
                else -> User( // Default ke Mahasiswa
                    name = "Ahmad Dahlan",
                    email = "ahmad.d@student.univ.ac.id",
                    role = "Mahasiswa", // <-- role diisi
                    nim = "21041001",
                )
            }
        }
    }


    // Function to save changes (will be implemented later)
    fun saveChanges(updatedUser: User) {
        _updateState.value = EditProfileUiState.Loading
        // Simulate network call
        viewModelScope.launch {
            kotlinx.coroutines.delay(1500) // Simulasi panggilan API
            // In a real app, you would call your repository/API here
            _updateState.value = EditProfileUiState.Success
        }
    }
}
