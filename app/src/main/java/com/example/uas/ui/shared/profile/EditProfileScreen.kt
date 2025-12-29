package com.example.uas.ui.shared.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uas.R
import com.example.uas.model.User
import com.example.uas.ui.theme.UASTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    editProfileViewModel: EditProfileViewModel = viewModel()
) {
    val initialUser by editProfileViewModel.userState.collectAsState()
    val updateState by editProfileViewModel.updateState.collectAsState()

    var name by remember { mutableStateOf("") }
    var kelas by remember { mutableStateOf("") } // Hanya untuk mahasiswa
    // ... state lain yang bisa diubah

    // LaunchedEffect untuk mengisi form saat data awal berhasil dimuat
    LaunchedEffect(initialUser) {
        initialUser?.let { user ->
            name = user.name
        }
    }

    val context = LocalContext.current
    LaunchedEffect(updateState) {
        if (updateState is EditProfileUiState.Success) {
            Toast.makeText(context, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        } else if (updateState is EditProfileUiState.Error) {
            Toast.makeText(context, (updateState as EditProfileUiState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profil", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = { Spacer(modifier = Modifier.width(48.dp)) }
            )
        }
    ) { paddingValues ->
        if (initialUser == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val user = initialUser!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ... (UI untuk foto profil) ...

                Spacer(modifier = Modifier.height(24.dp))

                // --- FIELD DINAMIS BERDASARKAN PERAN ---

                // NIM (hanya tampil jika user punya NIM)
                user.nim?.let {
                    OutlinedTextField(
                        value = it,
                        onValueChange = {},
                        label = { Text("NIM (TIDAK DAPAT DIUBAH)") },
                        readOnly = true,
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // NIP (hanya tampil jika user punya NIP)
                user.nip?.let {
                    OutlinedTextField(
                        value = it,
                        onValueChange = {},
                        label = { Text("NIP (TIDAK DAPAT DIUBAH)") },
                        readOnly = true,
                        leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Email (read-only)
                OutlinedTextField(
                    value = user.email,
                    onValueChange = {},
                    label = { Text("EMAIL (TIDAK DAPAT DIUBAH)") },
                    readOnly = true,
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Nama Lengkap (bisa diubah)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Lengkap") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Spacer(modifier = Modifier.weight(1f))

                // Tombol Simpan
                Button(
                    onClick = {
                        val updatedUser = user.copy(name = name)
                        editProfileViewModel.saveChanges(updatedUser)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = updateState !is EditProfileUiState.Loading
                ) {
                    if (updateState is EditProfileUiState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text("Simpan Perubahan")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    UASTheme {
        EditProfileScreen(rememberNavController())
    }
}
