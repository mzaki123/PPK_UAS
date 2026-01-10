package com.example.uas.ui.admin.home

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.data.repository.DashboardRepository
import com.example.uas.model.Pengajuan
import com.example.uas.model.User
import com.example.uas.model.response.DashboardStatsResponse
import com.example.uas.service.RetrofitInstance
import com.example.uas.ui.theme.UASTheme
import kotlin.collections.take
@Composable
fun AdminDashboardScreen() {
    // Factory untuk menyuntikkan repository ke ViewModel
    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return DashboardViewModel(DashboardRepository(RetrofitInstance.api)) as T
            }
        }
    )

    val usersState by dashboardViewModel.usersState.collectAsState()
    val pengajuanState by dashboardViewModel.pengajuanState.collectAsState()
    val stats = dashboardViewModel.calculatedStats // Ambil hasil hitungan manual


    LaunchedEffect(Unit) {
        dashboardViewModel.fetchData()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF6F6F8)),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // --- Statistik (Data dari Hasil Hitung Manual) ---
        item {
            DashboardHeader(stats)
        }

        item {
            Text(
                "Aktivitas Terbaru",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Daftar User
        when (val uState = usersState) {
            is DataListUiState.Success -> {
                items(uState.data.take(2)) { user ->
                    UserActivityItem(user, Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
                }
            }
            is DataListUiState.Loading -> {
                item { LinearProgressIndicator(Modifier.fillMaxWidth().padding(16.dp)) }
            }
            else -> {}
        }

        // Daftar Pengajuan
        when (val pState = pengajuanState) {
            is DataListUiState.Success -> {
                items(pState.data.take(3)) { pengajuan ->
                    PengajuanActivityItem(pengajuan, Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
                }
            }
            else -> {}
        }
    }
}


@Composable
fun DashboardHeader(stats: LocalDashboardStats) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF314158), RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Selamat Datang Admin Dashboard", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("SIAKTIF Control Panel", color = Color.White.copy(0.7f), fontSize = 12.sp)

        Spacer(modifier = Modifier.height(24.dp))

        // Baris 1 Statistik
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Total User", stats.totalUsers.toString(), Icons.Default.Group, Modifier.weight(1f))
            StatCard("Mahasiswa", stats.mahasiswa.toString(), Icons.Default.School, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Baris 2 Statistik
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Kemahasiswaan", stats.kemahasiswaan.toString(), Icons.Default.Person, Modifier.weight(1f))
            StatCard("Total Pengajuan", stats.totalPengajuan.toString(), Icons.Default.Description, Modifier.weight(1f))
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = Color(0xFF026AA1))
            Text(title, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun UserActivityItem(user: User, modifier: Modifier = Modifier) {
    val displayName = if (user.role.uppercase() == "ADMIN") {
        "Administrator Utama" // Nama untuk admin
    } else {
        user.name ?: "User Baru"
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(Color(0xFFE3F2FD), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Color(0xFF026AA1), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(displayName, color = Color(0xFF314158), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("${user.email} - ${user.role}", color = Color(0xFF64748B), fontSize = 12.sp)
            }
            Text("Baru", color = Color(0xFF94A3B8), fontSize = 12.sp)
        }
    }
}

@Composable
fun PengajuanActivityItem(pengajuan: Pengajuan, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(Color(0xFFFFF3E0), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Description, null, tint = Color(0xFFFB8C00), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(pengajuan.tujuanSurat ?: "Pengajuan SKM", color = Color(0xFF314158), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(pengajuan.mahasiswaNama ?: "Mahasiswa", color = Color(0xFF64748B), fontSize = 12.sp)
            }
            BadgeStatusSimple(status = pengajuan.status ?: "Proses")
        }
    }
}

@Composable
fun BadgeStatusSimple(status: String) {
    Text(
        text = status,
        color = if (status == "SELESAI") Color(0xFF4CAF50) else Color(0xFFFB8C00),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.background(Color(0xFFF1F5F9), RoundedCornerShape(4.dp)).padding(4.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun AdminDashboardScreenPreview() {
    UASTheme {
        AdminDashboardScreen()
    }
}
