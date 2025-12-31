package com.example.uas.ui.shared.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uas.data.repository.ProfileRepository
import com.example.uas.service.RetrofitInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavController
) {
    // --- FIX 1: Gunakan Factory untuk Inject Repository ke ViewModel ---
    val viewModel: ChangePasswordViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ChangePasswordViewModel(ProfileRepository(RetrofitInstance.api)) as T
            }
        }
    )

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // State Input
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // State Visibility
    var isOldVisible by remember { mutableStateOf(false) }
    var isNewVisible by remember { mutableStateOf(false) }
    var isConfirmVisible by remember { mutableStateOf(false) }

    // Listener hasil API
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ChangePasswordUiState.Success -> {
                Toast.makeText(context, "Password berhasil diubah lur!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            is ChangePasswordUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ganti Password", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Gunakan password yang kuat untuk menjaga keamanan akun SIAKTIF kamu lur.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // --- Password Lama ---
            PasswordField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = "Password Lama",
                isVisible = isOldVisible,
                onToggleVisibility = { isOldVisible = !isOldVisible }
            )

            // --- Password Baru ---
            PasswordField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "Password Baru",
                isVisible = isNewVisible,
                onToggleVisibility = { isNewVisible = !isNewVisible }
            )

            // --- Validasi Sederhana ---
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                ValidationRow("Minimal 6 karakter", newPassword.length >= 6)
                ValidationRow("Bukan password lama", newPassword.isNotEmpty() && newPassword != oldPassword)
            }

            // --- Konfirmasi Password Baru ---
            PasswordField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Konfirmasi Password Baru",
                isVisible = isConfirmVisible,
                onToggleVisibility = { isConfirmVisible = !isConfirmVisible },
                isError = confirmPassword.isNotEmpty() && confirmPassword != newPassword
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Tombol Simpan ---
            Button(
                onClick = { viewModel.changePassword(oldPassword, newPassword, confirmPassword) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState !is ChangePasswordUiState.Loading && newPassword.length >= 6 && newPassword == confirmPassword
            ) {
                if (uiState is ChangePasswordUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = isError,
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(if (isVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null)
            }
        }
    )
}

@Composable
fun ValidationRow(text: String, isValid: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isValid) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isValid) Color(0xFF16A34A) else Color.Gray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 12.sp, color = if (isValid) Color(0xFF16A34A) else Color.Gray)
    }
}