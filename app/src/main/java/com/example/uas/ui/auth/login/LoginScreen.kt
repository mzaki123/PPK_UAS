package com.example.uas.ui.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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
    // Basic ViewModelFactory
    val viewModelFactory = remember {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return LoginViewModel(AuthRepository(RetrofitInstance.api)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
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
                val responseBody = state.data.body()
                val token = responseBody?.accessToken
                val role = responseBody?.role

                if (token != null && role != null) {
                    // SessionManager is already initialized in MyApplication
                    SessionManager.login(token, role)

                    onLoginSuccess(role)
                    errorMessage = null
                } else {
                    errorMessage = "Login successful, but token or role was null."
                }
            }
            is LoginUiState.Error -> {
                errorMessage = state.message
            }
            else -> {
                // Idle or Loading, do nothing
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

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
            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = {
                    loginViewModel.login(LoginRequest(email, password))
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = loginState !is LoginUiState.Loading
            ) {
                if (loginState is LoginUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Login")
                }
            }

            TextButton(onClick = navigateToRegister) {
                Text("Don't have an account? Register")
            }
        }
    }
}
