package com.example.uas.ui.shared.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uas.data.SessionManager
import com.example.uas.data.repository.ProfileRepository
import com.example.uas.model.KemahasiswaanDto
import com.example.uas.model.MahasiswaDto
import com.example.uas.service.RetrofitInstance
import com.example.uas.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: EditProfileViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return EditProfileViewModel(ProfileRepository(RetrofitInstance.api)) as T
            }
        }
    )
) {

    val userState by viewModel.userState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profil Akun", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
        ) {
            // --- Bagian Header Profil (Adaptif berdasarkan Role) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                when (val state = userState) {
                    is FetchProfileUiState.Loading -> {
                        CircularProgressIndicator() // Tampilkan loading
                    }
                    is FetchProfileUiState.Success -> {
                        // Jika sukses, tampilkan data asli
                        val userData = state.data
                        val (nama, role) = when (userData) {
                            is MahasiswaDto -> userData.nama to "MAHASISWA"
                            is KemahasiswaanDto -> userData.nama to "KEMAHASISWAAN"
                            else -> "Pengguna" to "USER"
                        }
                        ProfileHeaderContent(name = nama, role = role)
                    }
                    is FetchProfileUiState.Error -> {
                        // Jika error, tampilkan pesan error atau default
                        ProfileHeaderContent(name = "Gagal Memuat", role = "Error")
                    }
                }
            }

            // --- Menu Kelompok 1: Akun ---
            ProfileSectionHeader("PENGATURAN AKUN")
            ProfileMenuCard {
                ProfileMenuItem(
                    icon = Icons.Default.Badge,
                    title = "Detail Informasi",
                    subtitle = "Lihat NIK, NIM, atau NIP Anda",
                    onClick = { navController.navigate(Routes.EDIT_PROFILE)}
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color(0xFFF1F5F9))
                ProfileMenuItem(
                    icon = Icons.Default.LockReset,
                    title = "Keamanan",
                    subtitle = "Ganti password akun Anda",
                    onClick = { navController.navigate(Routes.CHANGE_PASSWORD) }
                )
            }

            // --- Menu Kelompok 2: Aplikasi ---
            ProfileSectionHeader("APLIKASI")
            ProfileMenuCard {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color(0xFFF1F5F9))
                ProfileMenuItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Keluar Aplikasi",
                    subtitle = "Akhiri sesi login Anda",
                    titleColor = Color.Red,
                    onClick = { showLogoutDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "SIAKTIF v1.0.0\nPoliteknik Statistika STIS",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.LightGray,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // --- Dialog Konfirmasi Logout ---
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color.Red) },
            title = { Text("Konfirmasi Keluar", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin keluar? Anda harus login kembali untuk mengakses data.") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Ya, Keluar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun ProfileHeaderContent(name: String, role: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = Color(0xFF94A3B8)
            )
        }
        Spacer(Modifier.height(16.dp))

        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF314158)
        )

        Surface(
            color = Color(0xFF026AA1).copy(alpha = 0.1f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = role,
                color = Color(0xFF026AA1),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun ProfileSectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 8.dp),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF64748B),
        letterSpacing = 1.sp
    )
}

@Composable
fun ProfileMenuCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.5.dp),
        content = content
    )
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    titleColor: Color = Color(0xFF1E293B),
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (titleColor == Color.Red) Color(0xFFFEF2F2) else Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (titleColor == Color.Red) Color.Red else Color(0xFF026AA1),
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = titleColor)
            Text(text = subtitle, fontSize = 11.sp, color = Color.Gray)
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFFCBD5E1),
            modifier = Modifier.size(20.dp)
        )
    }
}