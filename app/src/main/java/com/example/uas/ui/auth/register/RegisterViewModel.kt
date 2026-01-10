package com.example.uas.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.AuthRepository
import com.example.uas.model.request.RegisterRequest
import com.example.uas.service.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// State ditaruh di sini lur agar terbaca oleh Screen
data class RegisterUiState(
    val nama: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val role: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val namaError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)

class RegisterViewModel : ViewModel() {
    // Karena tidak pakai Factory, kita inisialisasi Repository di sini lur
    // Pastikan RetrofitInstance.api sudah ada
    private val authRepository = AuthRepository(RetrofitInstance.api)

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNamaChange(v: String) = _uiState.update { it.copy(nama = v, namaError = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, emailError = null) }
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v, passwordError = null) }
    fun onConfirmPasswordChange(v: String) = _uiState.update { it.copy(confirmPassword = v, confirmPasswordError = null) }
    fun onRoleChange(v: String) = _uiState.update { it.copy(role = v) }

    fun register() {
        if (!validateInput()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val request = RegisterRequest(
                    nama = _uiState.value.nama,
                    email = _uiState.value.email,
                    password = _uiState.value.password,
                    role = _uiState.value.role
                )

                val response = authRepository.register(request)

                if (response.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                } else {
                    val errorMsg = if (response.code() == 500) {
                        "Email sudah terdaftar lur!"
                    } else {
                        "Gagal mendaftar (Error: ${response.code()})"
                    }
                    _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Koneksi gagal lur: ${e.message}") }
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true
        val s = _uiState.value

        if (s.nama.isBlank()) {
            _uiState.update { it.copy(namaError = "Nama wajib diisi") }
            isValid = false
        }
        if (!s.email.contains("@")) {
            _uiState.update { it.copy(emailError = "Email tidak valid") }
            isValid = false
        }
        if (s.password.length < 6) {
            _uiState.update { it.copy(passwordError = "Minimal 6 karakter") }
            isValid = false
        }
        if (s.password != s.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Password tidak cocok") }
            isValid = false
        }
        if (s.role.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Pilih peran Anda") }
            isValid = false
        }

        return isValid
    }
}