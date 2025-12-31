package com.example.uas.ui.kemahasiswaan.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uas.model.Pengajuan
import com.example.uas.ui.kemahasiswaan.KemahasiswaanViewModel
import com.example.uas.ui.kemahasiswaan.PengajuanListUiState
import com.example.uas.ui.navigation.Routes

@Composable
fun DashboardKemahasiswaanScreen(
    navController: NavController,
    viewModel: KemahasiswaanViewModel // Menggunakan Unified ViewModel lur
) {
    val pengajuanState by viewModel.filteredListState.collectAsState()

    // Trigger ambil data saat dashboard dibuka
    LaunchedEffect(Unit) {
        viewModel.getAllPengajuan()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F8))
    ) {
        item { HeaderSection() }

        when (val state = pengajuanState) {
            is PengajuanListUiState.Loading -> {
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF135BEC))
                    }
                }
            }
            is PengajuanListUiState.Success -> {
                val list = state.pengajuan

                item {
                    StatisticsSection(
                        total = list.size,
                        pending = list.count { it.status.equals("DIAJUKAN", ignoreCase = true) },
                        approved = list.count { it.status.equals("SELESAI", ignoreCase = true) },
                        rejected = list.count { it.status.equals("DITOLAK", ignoreCase = true) }
                    )
                }

                item {
                    RecentActivitySection(
                        navController = navController,
                        recentList = list.take(5) // Ambil 5 pengajuan terbaru
                    )
                }
            }
            is PengajuanListUiState.Error -> {
                item { Text("Gagal memuat statistik: ${state.message}", color = Color.Red, modifier = Modifier.padding(16.dp)) }
            }
            else -> {}
        }
    }
}

@Composable
fun HeaderSection() {
    val brandDark = Color(0xFF314158)
    val whiteTransparent = Color.White.copy(alpha = 0.1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(brandDark)
            .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 32.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Selamat Datang,", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text("Staff Kemahasiswaan", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(
                    onClick = { /* Notif */ },
                    modifier = Modifier.background(whiteTransparent, CircleShape)
                ) {
                    Icon(Icons.Filled.Notifications, null, tint = Color.White)
                }
            }

            // Info Petugas (Bisa ambil dari SessionManager jika perlu)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(whiteTransparent, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.AccountCircle, null, tint = Color.White, modifier = Modifier.size(40.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Petugas Verifikasi", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Bagian Pelayanan Mahasiswa", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun StatisticsSection(total: Int, pending: Int, approved: Int, rejected: Int) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            Icon(Icons.Filled.BarChart, null, tint = Color(0xFF135BEC))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Statistik Pengajuan", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Total Masuk", total.toString(), "Semua data", Icons.Filled.Inbox, Color(0xFFEFF6FF), Color(0xFF135BEC), modifier = Modifier.weight(1f))
                StatCard("Menunggu", pending.toString(), "Perlu dicek", Icons.Filled.PendingActions, Color(0xFFFFF7ED), Color(0xFFC2410C), cardBgColor = Color(0xFFFFFBEB), modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Selesai", approved.toString(), "Telah di-ACC", Icons.Filled.CheckCircle, Color(0xFFF0FDF4), Color(0xFF059669), modifier = Modifier.weight(1f))
                StatCard("Ditolak", rejected.toString(), "Tidak valid", Icons.Filled.Cancel, Color(0xFFFEF2F2), Color(0xFFDC2626), modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatCard(
    title: String, value: String, subtitle: String, icon: ImageVector, iconBgColor: Color, iconColor: Color, modifier: Modifier = Modifier, cardBgColor: Color = Color.White
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.background(iconBgColor, RoundedCornerShape(8.dp)).padding(6.dp)) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF314158))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
            Text(subtitle, fontSize = 10.sp, color = Color.Gray.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun RecentActivitySection(navController: NavController, recentList: List<Pengajuan>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Pengajuan Terbaru", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = { navController.navigate(Routes.KEMAHASISWAAN_DAFTAR_PENGAJUAN) }) {
                Text("Lihat Semua", color = Color(0xFF135BEC))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (recentList.isEmpty()) {
            Text("Belum ada pengajuan masuk.", modifier = Modifier.padding(vertical = 20.dp), color = Color.Gray)
        } else {
            recentList.forEach { pengajuan ->
                ActivityItem(
                    name = pengajuan.mahasiswaNama,
                    details = "${pengajuan.tujuanSurat} • ${pengajuan.tanggalPengajuan}",
                    status = pengajuan.status,
                    onClick = { navController.navigate("${Routes.KEMAHASISWAAN_DETAIL_PENGAJUAN}/${pengajuan.id}") }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ActivityItem(name: String, details: String, status: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(details, fontSize = 12.sp, color = Color.Gray)
            }
            // Badge Status Sederhana
            val badgeColor = when(status.uppercase()) {
                "SELESAI" -> Color(0xFF059669)
                "DITOLAK" -> Color(0xFFDC2626)
                else -> Color(0xFFC2410C)
            }
            Box(Modifier.background(badgeColor.copy(alpha = 0.1f), RoundedCornerShape(6.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                Text(status, color = badgeColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}