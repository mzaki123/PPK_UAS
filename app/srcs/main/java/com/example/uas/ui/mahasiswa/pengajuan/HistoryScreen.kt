package com.example.uas.ui.mahasiswa.pengajuan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uas.model.Pengajuan
import com.example.uas.ui.mahasiswa.HistoryUiState
import com.example.uas.ui.mahasiswa.MahasiswaViewModel
import com.example.uas.ui.theme.UASTheme

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: MahasiswaViewModel
) {
    val historyState by viewModel.historyState.collectAsState()
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Diajukan", "Diterima", "Ditolak")

    LaunchedEffect(Unit) {
        viewModel.getMyPengajuan()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Riwayat Pengajuan", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
        LazyRow(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        when (val state = historyState) {
            is HistoryUiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is HistoryUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(state.pengajuan.filter {
                        selectedFilter == "Semua" || it.status.equals(selectedFilter, ignoreCase = true)
                    }) { pengajuan ->
                        PengajuanCard(pengajuan = pengajuan, navController = navController)
                    }
                }
            }
            is HistoryUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            else -> {}
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
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(pengajuan.tanggalPengajuan, fontSize = 12.sp, color = Color.Gray)
                Text(
                    pengajuan.status,
                    color = when (pengajuan.status.uppercase()) {
                        "DITERIMA", "SELESAI" -> Color(0xFF66BB6A)
                        "DIAJUKAN" -> Color(0xFFFFA726)
                        else -> Color(0xFFEF5350)
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(pengajuan.tujuanSurat, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Lihat Detail") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    UASTheme {
        val pengajuan = Pengajuan(1, "Surat Keterangan Aktif", "24 Okt 2023", "Diterima", "Budi", "123")
        PengajuanCard(pengajuan = pengajuan, navController = rememberNavController())
    }
}
