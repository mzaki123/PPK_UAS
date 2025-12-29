package com.example.uas.ui.auth.login

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.SessionManager
import com.example.uas.service.RetrofitInstance
import com.example.uas.service.dto.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.Charset

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
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginUiState.Error("Email dan Password tidak boleh kosong.")
            return
        }
        _loginState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = RetrofitInstance.api.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    val token = authResponse.accessToken

                    val userRole = getRoleFromToken(token)

                    // SIMPAN TOKEN DAN ROLE KE SESSION MANAGER (INI BAGIAN PENTING YANG HILANG)
                    SessionManager.login(token, userRole)

                    _loginState.value = LoginUiState.Success(token, userRole)

                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Login Gagal"
                    try {
                        val errorJson = JSONObject(errorMsg)
                        _loginState.value = LoginUiState.Error(errorJson.getString("message"))
                    } catch (e: Exception) {
                        _loginState.value = LoginUiState.Error("Login Gagal. Kredensial Salah.")
                    }
                }

            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error("Koneksi Gagal. Periksa jaringan/IP.")
            }
        }
    }

    private fun getRoleFromToken(token: String): String {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return "MAHASISWA" // Default

            val payload = parts[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes, Charset.forName("UTF-8"))
            val jsonObject = JSONObject(decodedString)

            android.util.Log.d("JWT_DECODE", "Payload: $decodedString")

            when {
                // Cek field 'roles' (Array)
                jsonObject.has("roles") -> {
                    val rolesArray = jsonObject.getJSONArray("roles")
                    if (rolesArray.length() > 0) {
                        rolesArray.getString(0).removePrefix("ROLE_").uppercase()
                    } else "MAHASISWA"
                }
                // Cek field 'authorities' (Array) -> SPRING DEFAULT
                jsonObject.has("authorities") -> {
                    val authArray = jsonObject.getJSONArray("authorities")
                    if (authArray.length() > 0) {
                        authArray.getString(0).removePrefix("ROLE_").uppercase()
                    } else "MAHASISWA"
                }
                // Cek field 'role' (String)
                jsonObject.has("role") -> {
                    jsonObject.getString("role").removePrefix("ROLE_").uppercase()
                }

                else -> "MAHASISWA"
            }
        } catch (e: Exception) {
            android.util.Log.e("JWT_ERROR", "Gagal bongkar token: ${e.message}")
            "MAHASISWA"
        }
    }
}