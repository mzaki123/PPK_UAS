package com.example.uas.ui.shared.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// --- FIX 1: Definisikan State di sini agar bisa dibaca oleh Screen ---
sealed class ChangePasswordUiState {
    object Idle : ChangePasswordUiState()
    object Loading : ChangePasswordUiState()
    object Success : ChangePasswordUiState()
    data class Error(val message: String) : ChangePasswordUiState()
}

class ChangePasswordViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ChangePasswordUiState>(ChangePasswordUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun changePassword(old: String, new: String, confirm: String) {
        // 1. Validasi Input di Frontend
        if (new.length < 6) {
            _uiState.value = ChangePasswordUiState.Error("Password baru minimal harus 6 karakter lur!")
            return
        }
        if (new != confirm) {
            _uiState.value = ChangePasswordUiState.Error("Konfirmasi password tidak cocok!")
            return
        }
        if (old == new) {
            _uiState.value = ChangePasswordUiState.Error("Password baru tidak boleh sama dengan password lama.")
            return
        }

        _uiState.value = ChangePasswordUiState.Loading

        viewModelScope.launch {
            try {
                // 2. Memanggil Repository (Pastikan ProfileRepository sudah ada)
                val response = repository.changePassword(old, new)

                if (response.isSuccessful) {
                    _uiState.value = ChangePasswordUiState.Success
                } else {
                    // 3. Menangani error 400 (Password lama salah) atau 500
                    val errorMsg = when(response.code()) {
                        400 -> "Password lama yang kamu masukkan salah lur."
                        500 -> "Password baru tidak boleh sama dengan yang lama."
                        else -> "Gagal mengubah password. Silakan coba lagi."
                    }
                    _uiState.value = ChangePasswordUiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _uiState.value = ChangePasswordUiState.Error("Terjadi kesalahan koneksi: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = ChangePasswordUiState.Idle
    }
}