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
import com.example.uas.data.SessionManager

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    navigateToRegister: () -> Unit,
    viewModel: LoginViewModel // Gunakan viewModel yang dikirim dari AppNavigation
) {
    // Collect state dari viewModel parameter
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Jalankan efek saat state berubah
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginUiState.Success -> {
                // Di ViewModel baru, state.data adalah LoginResponse langsung (bukan Response)
                val token = state.data.accessToken
                val role = state.decodedRole

                if (token.isNotEmpty()) {
                    // Simpan sesi
                    SessionManager.login(token, role)
                    // Navigasi berdasarkan role
                    onLoginSuccess(role)
                    errorMessage = null
                } else {
                    errorMessage = "Login sukses, tapi token kosong."
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
                onValueChange = {
                    email = it
                    if (errorMessage != null) viewModel.resetState() // Reset error saat ngetik
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage != null,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (errorMessage != null) viewModel.resetState()
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = errorMessage != null,
                singleLine = true
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 4.dp)
                        .align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.login(email, password) }, // Gunakan parameter email & password
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
                Text("Belum punya akun? Daftar")
            }
        }
    }
}