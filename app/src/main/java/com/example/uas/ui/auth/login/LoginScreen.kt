package com.example.uas.ui.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.data.SessionManager
import com.example.uas.data.repository.AuthRepository
import com.example.uas.model.request.LoginRequest
import com.example.uas.service.RetrofitInstance

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    navigateToRegister: () -> Unit
) {
    val viewModelFactory = remember {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(AuthRepository(RetrofitInstance.api)) as T
            }
        }
    }

    val loginViewModel: LoginViewModel = viewModel(factory = viewModelFactory)
    val loginState by loginViewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginUiState.Success -> {
                val token = state.data.body()?.accessToken
                // Gunakan role hasil bedahan token di ViewModel tadi lur
                val role = state.decodedRole

                if (token != null) {
                    // Simpan sesi dengan role hasil bedahan
                    SessionManager.login(token, role)
                    onLoginSuccess(role)
                    errorMessage = null
                } else {
                    errorMessage = "Login sukses, tapi token tidak ditemukan."
                }
            }
            is LoginUiState.Error -> {
                errorMessage = state.message
            }
            else -> {}
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Masuk Ke SIAKTIF",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Silakan login untuk mengelola pengajuan",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage != null
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = errorMessage != null
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp, start = 4.dp).align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { loginViewModel.login(LoginRequest(email, password)) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = loginState !is LoginUiState.Loading && email.isNotEmpty() && password.isNotEmpty()
            ) {
                if (loginState is LoginUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Login Sekarang", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = navigateToRegister) {
                Text("Belum punya akun? Daftar ")
            }
        }
    }
}