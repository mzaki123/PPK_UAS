package com.example.uas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uas.data.model.Pengajuan
import com.example.uas.ui.components.BottomNavigationBar
import com.example.uas.ui.theme.UASTheme
import com.example.uas.ui.navigation.AppRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    val pengajuanList = listOf(
        Pengajuan("1", "Surat Keterangan Aktif Kuliah", "24 Okt 2023", "Diterima", "Keperluan beasiswa Djarum Plus..."),
        Pengajuan("2", "Surat Izin Cuti Akademik", "20 Sep 2023", "Diajukan", "Alasan kesehatan yang mendesak..."),
        Pengajuan("3", "Surat Rekomendasi Beasiswa", "15 Sep 2023", "Ditolak", "Pengajuan untuk program pertukaran..."),
        Pengajuan("4", "Surat Pengantar Magang", "10 Sep 2023", "Diajukan", "Permohonan surat pengantar untuk magang...")
    )
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Diajukan", "Diterima", "Ditolak")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Pengajuan") }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) }
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(pengajuanList.filter { it.status == selectedFilter || selectedFilter == "Semua" }) { pengajuan ->
                    PengajuanCard(pengajuan = pengajuan, navController = navController)
                }
            }
        }
    }
}

@Composable
fun PengajuanCard(pengajuan: Pengajuan, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = pengajuan.date, fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = pengajuan.status,
                    color = when (pengajuan.status) {
                        "Diterima" -> Color(0xFF66BB6A)
                        "Diajukan" -> Color(0xFFFFA726)
                        else -> Color(0xFFEF5350)
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = pengajuan.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = pengajuan.description, fontSize = 14.sp, color = Color.Gray, maxLines = 2)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { navController.navigate(AppRoutes.DETAIL) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Lihat Detail")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    UASTheme {
        HistoryScreen(rememberNavController())
    }
}
