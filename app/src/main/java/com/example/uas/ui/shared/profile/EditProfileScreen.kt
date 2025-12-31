package com.example.uas.ui.shared.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uas.data.SessionManager
import com.example.uas.model.MahasiswaDto
import com.example.uas.model.KemahasiswaanDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel // Pastikan ViewModel sudah di-inject dengan benar
) {
    val userState by viewModel.userState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    val context = LocalContext.current
    val role = remember { SessionManager.getRole()?.uppercase() ?: "" }

    // State form lokal
    var nama by remember { mutableStateOf("") }
    var identifier by remember { mutableStateOf("") } // NIM atau NIP
    var extraInfo by remember { mutableStateOf("") } // Kelas/Jurusan

    // Sinkronkan data saat data profil berhasil dimuat
    LaunchedEffect(userState) {
        when (val user = userState) {
            is MahasiswaDto -> {
                nama = user.nama
                identifier = user.nim
                extraInfo = user.kelas ?: user.jurusan ?: ""
            }
            is KemahasiswaanDto -> {
                nama = user.nama
                identifier = user.nip
            }

            else -> {}
        }
    }

    LaunchedEffect(updateState) {
        if (updateState is EditProfileUiState.Success) {
            Toast.makeText(context, "Profil Berhasil Diperbarui!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ubah Profil Saya", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (userState == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Avatar Placeholder
                Box(Modifier.size(100.dp).clip(CircleShape).background(Color(0xFFE2E8F0)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(60.dp), tint = Color.Gray)
                }

                Text("Update informasi profil Anda di bawah ini.", fontSize = 14.sp, color = Color.Gray)

                // Field Identifier (NIM/NIP) - Read Only
                OutlinedTextField(
                    value = identifier,
                    onValueChange = {},
                    label = { Text(if (role == "MAHASISWA") "NIM" else "NIP") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    leadingIcon = { Icon(Icons.Default.Badge, null) },
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFF1F5F9))
                )

                // Field Nama
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Lengkap") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.AccountCircle, null) }
                )

                // Field Tambahan (Khusus Mahasiswa)
                if (role == "MAHASISWA") {
                    OutlinedTextField(
                        value = extraInfo,
                        onValueChange = { extraInfo = it },
                        label = { Text("Kelas / Jurusan") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.School, null) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (role == "MAHASISWA") {
                            viewModel.saveChanges(MahasiswaDto(nama = nama, nim = identifier, kelas = extraInfo))
                        } else {
                            viewModel.saveChanges(KemahasiswaanDto(nama = nama, nip = identifier))
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = updateState !is EditProfileUiState.Loading
                ) {
                    if (updateState is EditProfileUiState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Simpan Perubahan")
                    }
                }
            }
        }
    }
}