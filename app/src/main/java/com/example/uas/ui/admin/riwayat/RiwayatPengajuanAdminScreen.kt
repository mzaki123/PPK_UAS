package com.example.uas.ui.admin.riwayat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.data.repository.PengajuanRepository
import com.example.uas.model.Pengajuan
import com.example.uas.service.RetrofitInstance
import com.example.uas.ui.theme.UASTheme

val filters = listOf("Semua", "Diajukan", "Diterima", "Ditolak")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatPengajuanAdminScreen() {
    val viewModelFactory = remember {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PengajuanViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return PengajuanViewModel(PengajuanRepository(RetrofitInstance.api)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    val pengajuanViewModel: PengajuanViewModel = viewModel(factory = viewModelFactory)
    val pengajuanState by pengajuanViewModel.pengajuanState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    LaunchedEffect(Unit) {
        pengajuanViewModel.getAllPengajuan()
    }

    Scaffold(
        containerColor = Color(0xFFF6F6F8)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Riwayat Pengajuan", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    IconButton(onClick = { /* TODO: Export */ }) {
                        Icon(Icons.Default.Download, "Export Laporan", tint = Color(0xFF135BEC))
                    }
                }
            }
            item {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari NIM atau Nama Mahasiswa...", color = Color(0xFF9AA2B1)) },
                    leadingIcon = { Icon(Icons.Default.Search, "Search", tint = Color(0xFF4C669A)) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF135BEC),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            when (val state = pengajuanState) {
                is PengajuanUiState.Loading -> {
                    item { Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() } }
                }
                is PengajuanUiState.Success -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Menampilkan ${state.pengajuan.size} pengajuan", fontSize = 12.sp, color = Color.Gray)
                            TextButton(onClick = { /* TODO: Sort */ }) {
                                Icon(Icons.Default.Sort, "Sort", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Terbaru")
                            }
                        }
                    }
                    items(state.pengajuan) { pengajuan ->
                        PengajuanItem(pengajuan = pengajuan, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
                    }
                }
                is PengajuanUiState.Error -> {
                    item { Text(state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp)) }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun PengajuanItem(pengajuan: Pengajuan, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFF1F5F9)).border(1.dp, Color(0xFFE2E8F0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, "User Avatar", tint = Color(0xFF94A3B8), modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(pengajuan.mahasiswaNama, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    StatusBadge(status = pengajuan.status)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("NIM: ${pengajuan.mahasiswaNim}", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, color = Color.Gray) {
                    Icon(Icons.Default.CalendarToday, "Date", modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(pengajuan.tanggalPengajuan, fontSize = 12.sp)
                    Text(" • ", fontSize = 12.sp)
                    Text(pengajuan.tujuanSurat, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "SELESAI" -> Color(0xFFD1FAE5) to Color(0xFF065F46) // Diterima
        "DITOLAK" -> Color(0xFFFEE2E2) to Color(0xFF991B1B) // Ditolak
        else -> Color(0xFFFEF3C7) to Color(0xFF92400E) // Diajukan / Proses
    }
    Box(
        modifier = Modifier.background(backgroundColor, RoundedCornerShape(6.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(status, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true)
@Composable
fun RiwayatPengajuanAdminScreenPreview() {
    UASTheme {
        RiwayatPengajuanAdminScreen()
    }
}
