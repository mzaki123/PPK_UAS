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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
    val fetchState by viewModel.userState.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    val context = LocalContext.current
    val role = remember { SessionManager.getRole()?.uppercase() ?: "" }

    // State form lokal
    var nama by remember { mutableStateOf("") }
    var identifier by remember { mutableStateOf("") } // NIM atau NIP
    var extraInfo by remember { mutableStateOf("") } // Kelas/Jurusan
    var isIdentifierEditable by remember { mutableStateOf(false) }

    // Sinkronkan data saat data profil berhasil dimuat
    LaunchedEffect(fetchState) {
        if (fetchState is FetchProfileUiState.Success) {
            val user = (fetchState as FetchProfileUiState.Success).data
            // Tentukan apakah identifier bisa diedit berdasarkan data awal
            isIdentifierEditable = viewModel.isIdentifierEditable(user)

            when (user) {
                is MahasiswaDto -> {
                    nama = user.nama
                    identifier = user.nim ?: "" // Isi dengan NIM yang ada atau string kosong
                    extraInfo = user.kelas ?: user.jurusan ?: ""
                }
                is KemahasiswaanDto -> {
                    nama = user.nama
                    identifier = user.nip ?: "" // Isi dengan NIP yang ada atau string kosong
                }
            }
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
        if (fetchState == null) {
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
                    onValueChange = { if (isIdentifierEditable) identifier = it }, // Hanya bisa diubah jika diizinkan
                    label = {
                        // Label dinamis
                        val labelText = if (role == "MAHASISWA") "NIM" else "NIP"
                        Text(if (isIdentifierEditable) labelText else "$labelText (TIDAK DAPAT DIUBAH)")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = !isIdentifierEditable, // Kunci field jika tidak bisa diedit
                    leadingIcon = { Icon(Icons.Default.Badge, null) },
                    trailingIcon = {
                        // Tampilkan ikon gembok jika sudah tidak bisa diedit
                        if (!isIdentifierEditable) {
                            Icon(Icons.Default.Lock, null)
                        }
                    },
                    colors = if (!isIdentifierEditable) {
                        OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFF1F5F9))
                    } else {
                        OutlinedTextFieldDefaults.colors()
                    }
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