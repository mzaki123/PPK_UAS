package com.example.uas.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.network.RetrofitInstance
import com.example.uas.network.dto.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// This will represent the states of our Login Screen
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun loginUser(email: String, password: String) {
        // Set state to Loading
        _loginState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = RetrofitInstance.api.login(request)
                // Set state to Success with the token
                _loginState.value = LoginUiState.Success(response.accessToken)
            } catch (e: Exception) {
                // Set state to Error with the exception message
                _loginState.value = LoginUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
