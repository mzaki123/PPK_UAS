package com.example.uas.ui.admin.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.uas.model.User
import com.example.uas.ui.theme.UASTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailUserScreen(
    userId: Long,
    onBackClick: () -> Unit,
    userViewModel: UserViewModel
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val deleteState by userViewModel.deleteUserState.collectAsState()
    val userDetailState by userViewModel.userDetailState.collectAsState()

    LaunchedEffect(userId) {
        if (userId != 0L) {
            userViewModel.getUserById(userId)
        }
    }

    LaunchedEffect(deleteState) {
        if (deleteState is DeleteUserUiState.Success) {
            onBackClick() // Navigate back after successful deletion
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Pengguna",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.8f)
                )
            )
        },
        containerColor = Color(0xFFF6F6F8)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
            when(val state = userDetailState) {
                is UserDetailUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UserDetailUiState.Success -> {
                    val user = state.user

                    // Logic for display name in dialog and header
                    val safeName = when {
                        user.role.equals("ADMIN", ignoreCase = true) -> "Sistem Administrator"
                        user.name.isNullOrBlank() -> "User Belum Mengisi Profil"
                        else -> user.name
                    }

                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            UserProfileHeader(
                                name = safeName,
                                role = user.role,
                                isActive = true // Placeholder
                            )
                        }
                        item {
                            UserInfoSection(
                                nim = user.id.toString(),
                                email = user.email,
                                role = user.role
                            )
                        }
                        item {
                            UserActionButtons(onDeleteClick = { showDeleteDialog = true })
                        }
                    }

                    if (showDeleteDialog) {
                        DeleteUserDialog(
                            userName = safeName,
                            onConfirm = {
                                userViewModel.deleteUser(user.id)
                                showDeleteDialog = false
                            },
                            onDismiss = { showDeleteDialog = false }
                        )
                    }
                }
                is UserDetailUiState.Error -> {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                }
                else -> {
                    // Idle state
                }
            }
        }
    }
}

@Composable
fun UserProfileHeader(name: String, role: String, isActive: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F5F9))
                    .border(4.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Avatar",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(64.dp)
                )
            }
            if (isActive) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22C55E))
                        .border(4.dp, Color.White, CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D121B),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        RoleBadge(role = role)
    }
}

@Composable
fun UserInfoSection(nim: String, email: String, role: String) {
    // Dynamic Label based on role
    val idLabel = if (role.equals("MAHASISWA", true)) "NIM" else "ID User"
    val kelasPlaceholder = if (role.equals("MAHASISWA", true)) "TI-3A" else "-"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Informasi Akun",
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                InfoRow(icon = Icons.Default.Badge, label = idLabel, value = nim)
                HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 1.dp, color = Color(0xFFE2E8F0))
                InfoRow(icon = Icons.Default.Email, label = "Email", value = email)

                if (role.equals("MAHASISWA", true)) {
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 1.dp, color = Color(0xFFE2E8F0))
                    InfoRow(icon = Icons.Default.School, label = "Kelas", value = kelasPlaceholder)
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = label, tint = Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = Color.Gray, fontSize = 14.sp)
        }
        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color(0xFF0D121B))
    }
}

@Composable
fun UserActionButtons(onDeleteClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { /*TODO: Reset Password*/ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF026AA1)
            ),
            border = BorderStroke(1.dp, Color(0xFF026AA1).copy(alpha = 0.5f))
        ) {
            Icon(Icons.Default.LockReset, contentDescription = "Reset Password")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reset Password", fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onDeleteClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFEF2F2),
                contentColor = Color(0xFFDC2626)
            ),
            border = BorderStroke(1.dp, Color(0xFFFCA5A5))
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Hapus User")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Hapus User", fontWeight = FontWeight.Bold)
        }
        Text(
            text = "Tindakan ini tidak dapat dibatalkan. Menghapus user akan menghilangkan semua data terkait.",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun DeleteUserDialog(
    userName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEE2E2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Hapus User?", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Apakah Anda yakin ingin menghapus $userName? Tindakan ini tidak dapat dibatalkan.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text("Hapus Sekarang")
                    }
                    TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                        Text("Batal", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailUserScreenPreview() {
    UASTheme {}
}