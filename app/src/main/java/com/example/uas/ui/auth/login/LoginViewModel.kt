package com.example.uas.ui.auth.login

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.AuthRepository
import com.example.uas.service.RetrofitInstance
import com.example.uas.model.request.LoginRequest
import com.example.uas.model.response.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.Charset

// State untuk UI Login
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val data: LoginResponse, val decodedRole: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel : ViewModel() {

    // Inisialisasi repository langsung di sini agar tidak butuh Factory di NavGraph
    private val authRepository = AuthRepository(RetrofitInstance.api)

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    /**
     * Fungsi Login dengan email dan password murni
     */
    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            try {
                val response = authRepository.login(LoginRequest(email, pass))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        // Bongkar Token untuk ambil role secara manual
                        val roleFromToken = getRoleFromToken(body.accessToken)
                        _loginState.value = LoginUiState.Success(body, roleFromToken)
                    } else {
                        _loginState.value = LoginUiState.Error("Data respon kosong dari server.")
                    }
                } else {
                    val errorMsg = when(response.code()) {
                        401 -> "Email atau password salah lur!"
                        400 -> "Format login tidak valid."
                        else -> "Gagal masuk (Error: ${response.code()})"
                    }
                    _loginState.value = LoginUiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error("Gagal terhubung: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Fungsi bongkar JWT Token tanpa library tambahan
     */
    private fun getRoleFromToken(token: String): String {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return "MAHASISWA"

            val payload = parts[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes, Charset.forName("UTF-8"))
            val jsonObject = JSONObject(decodedString)

            when {
                jsonObject.has("roles") -> {
                    val rolesArray = jsonObject.optJSONArray("roles")
                    if (rolesArray != null && rolesArray.length() > 0) {
                        rolesArray.getString(0).replace("ROLE_", "").uppercase()
                    } else {
                        jsonObject.optString("roles").replace("ROLE_", "").uppercase()
                    }
                }
                jsonObject.has("role") -> {
                    jsonObject.getString("role").replace("ROLE_", "").uppercase()
                }
                else -> "MAHASISWA"
            }
        } catch (e: Exception) {
            Log.e("JWT_ERROR", "Gagal bongkar token: ${e.message}")
            "MAHASISWA"
        }
    }

    fun resetState() {
        _loginState.value = LoginUiState.Idle
    }
}