package com.example.uas.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.network.RetrofitInstance
import com.example.uas.network.dto.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Updated to include role in the Success state
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String, val role: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun loginUser(email: String, password: String) {
        _loginState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = RetrofitInstance.api.login(request)
                // Set state to Success with both token and role
                _loginState.value = LoginUiState.Success(response.accessToken, response.role)
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
