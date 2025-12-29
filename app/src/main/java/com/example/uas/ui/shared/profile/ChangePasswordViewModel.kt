package com.example.uas.ui.shared.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// import com.example.uas.data.repository.UserRepository // Nanti akan pakai ini
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// State untuk merepresentasikan hasil dari proses ganti password
sealed class ChangePasswordUiState {
    object Idle : ChangePasswordUiState()
    object Loading : ChangePasswordUiState()
    object Success : ChangePasswordUiState()
    data class Error(val message: String) : ChangePasswordUiState()
}

class ChangePasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ChangePasswordUiState>(ChangePasswordUiState.Idle)
    val uiState: StateFlow<ChangePasswordUiState> = _uiState

    fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        // 1. Validasi Frontend
        if (newPassword.length < 6) {
            _uiState.value = ChangePasswordUiState.Error("Password baru minimal harus 6 karakter.")
            return
        }
        if (newPassword != confirmPassword) {
            _uiState.value = ChangePasswordUiState.Error("Konfirmasi password tidak cocok.")
            return
        }
        // ... validasi lain jika perlu

        _uiState.value = ChangePasswordUiState.Loading

        viewModelScope.launch {
            // TODO: Nanti di sini Anda akan memanggil UserRepository untuk mengirim data ke API backend
            // val result = userRepository.changePassword(oldPassword, newPassword)
            // result.onSuccess { ... }
            // result.onFailure { ... }

            // Untuk sekarang, kita simulasikan proses berhasil setelah 2 detik
            kotlinx.coroutines.delay(2000)
            _uiState.value = ChangePasswordUiState.Success
        }
    }
}
