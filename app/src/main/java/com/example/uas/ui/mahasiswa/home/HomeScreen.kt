package com.example.uas.ui.mahasiswa.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uas.R
import com.example.uas.data.SessionManager
import com.example.uas.ui.mahasiswa.HistoryUiState
import com.example.uas.ui.mahasiswa.MahasiswaViewModel
import com.example.uas.ui.navigation.Routes

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MahasiswaViewModel
) {
    val historyState by viewModel.historyState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMyPengajuan()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // --- Greeting Section ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Dashboard", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("SIAKTIF Mobile", fontSize = 12.sp, color = Color.Gray)
            }
            IconButton(onClick = { /* Notifikasi */ }) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Profile Card (Data Statis/Session) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(60.dp).clip(CircleShape).background(Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = Color.Gray, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("MAHASISWA AKTIF", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text(SessionManager.getRole() ?: "Mahasiswa", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Selamat beraktivitas !", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Statistics Section (Data Dinamis) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Statistik Pengajuan", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                "Real-time",
                fontSize = 11.sp,
                color = Color.DarkGray,
                modifier = Modifier
                    .background(Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = historyState) {
            is HistoryUiState.Loading -> {
                Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(strokeWidth = 2.dp)
                }
            }
            is HistoryUiState.Success -> {
                val list = state.pengajuan
                val total = list.size
                val diajukan = list.count { it.status.equals("DIAJUKAN", ignoreCase = true) }
                val selesai = list.count { it.status.equals("DITERIMA", ignoreCase = true) }
                val ditolak = list.count { it.status.equals("DITOLAK", ignoreCase = true) }

                Column {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatCard("Total", total.toString(), Icons.Filled.Folder, Color(0xFF314158), modifier = Modifier.weight(1f))
                        StatCard("Proses", diajukan.toString(), Icons.Filled.HourglassTop, Color(0xFFE6A23C), modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatCard("Selesai", selesai.toString(), Icons.Filled.CheckCircle, Color(0xFF67C23A), modifier = Modifier.weight(1f))
                        StatCard("Ditolak", ditolak.toString(), Icons.Filled.Cancel, Color(0xFFF56C6C), modifier = Modifier.weight(1f))
                    }
                }
            }
            is HistoryUiState.Error -> {
                Text("Gagal memuat statistik", color = Color.Red, fontSize = 12.sp)
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Action Buttons ---
        Button(
            onClick = { navController.navigate(Routes.FORM_PENGAJUAN) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF026AA1))
        ) {
            Icon(Icons.Filled.Add, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ajukan Surat Baru", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { navController.navigate(Routes.HISTORY) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFF026AA1))
        ) {
            Icon(Icons.Filled.History, null, tint = Color(0xFF026AA1))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Lihat Riwayat Saya", color = Color(0xFF026AA1), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Aktivitas Terkini", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))

        // Menampilkan satu item terbaru jika ada
        if (historyState is HistoryUiState.Success) {
            val list = (historyState as HistoryUiState.Success).pengajuan
            if (list.isNotEmpty()) {
                val latest = list.first()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, null, tint = Color.Gray)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(latest.tujuanSurat, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Status: ${latest.status}", fontSize = 12.sp, color = Color(0xFF026AA1))
                        }
                    }
                }
            } else {
                Text("Belum ada pengajuan lur.", color = Color.Gray, modifier=Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 12.sp, color = Color.Gray)
                Icon(icon, null, tint = color.copy(alpha = 0.5f), modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = color)
        }
    }
}