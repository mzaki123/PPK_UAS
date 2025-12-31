package com.example.uas.ui.auth.login

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.AuthRepository
import com.example.uas.model.request.LoginRequest
import com.example.uas.model.response.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.nio.charset.Charset

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val data: Response<LoginResponse>, val decodedRole: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            try {
                val response = authRepository.login(loginRequest)
                if (response.isSuccessful) {
                    val body = response.body()
                    // BEDAH TOKEN DI SINI LUR!
                    val roleFromToken = body?.accessToken?.let { getRoleFromToken(it) } ?: "MAHASISWA"

                    _loginState.value = LoginUiState.Success(response, roleFromToken)
                } else {
                    _loginState.value = LoginUiState.Error("Login gagal: Periksa email/password kamu .")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error("Koneksi bermasalah: ${e.message}")
            }
        }
    }

    /**
     * Fungsi sakti untuk bongkar JWT Token tanpa library tambahan lur
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
                jsonObject.has("role") -> jsonObject.getString("role").replace("ROLE_", "").uppercase()
                else -> "MAHASISWA"
            }
        } catch (e: Exception) {
            android.util.Log.e("JWT_ERROR", "Gagal bongkar token: ${e.message}")
            "MAHASISWA"
        }
    }
}