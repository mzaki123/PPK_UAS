package com.example.uas.ui.kemahasiswaan.daftar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uas.model.Pengajuan
import com.example.uas.ui.kemahasiswaan.KemahasiswaanViewModel
import com.example.uas.ui.kemahasiswaan.PengajuanListUiState
import com.example.uas.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarPengajuanScreen(
    navController: NavController,
    viewModel: KemahasiswaanViewModel
) {
    // FIX: Ambil state gabungan (Search + Filter) dari ViewModel
    val pengajuanState by viewModel.filteredListState.collectAsState()

    // State lokal untuk tampilan TextField agar smooth saat mengetik
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    LaunchedEffect(Unit) {
        viewModel.getAllPengajuan()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F8))
    ) {
        // --- Header & Pencarian ---
        SearchBarSection(
            query = searchQuery,
            onQueryChange = {
                searchQuery = it
                viewModel.onSearchChange(it) // Kirim ke ViewModel
            }
        )

        // --- Filter Chips ---
        FilterSection(
            selectedFilter = selectedFilter,
            onFilterChange = {
                selectedFilter = it
                viewModel.onFilterChange(it) // Kirim ke ViewModel
            }
        )

        // --- List Content ---
        when (val state = pengajuanState) {
            is PengajuanListUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF026AA1))
                }
            }
            is PengajuanListUiState.Success -> {
                if (state.pengajuan.isEmpty()) {
                    EmptyState()
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.pengajuan) { pengajuan ->
                            PengajuanCard(
                                pengajuan = pengajuan,
                                navController = navController
                            )
                        }
                    }
                }
            }
            is PengajuanListUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
            }
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarSection(query: String, onQueryChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF314158))
            .padding(16.dp)
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Cari NIM atau Nama...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFF4C669A)) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Composable
fun FilterSection(selectedFilter: String, onFilterChange: (String) -> Unit) {
    val filters = listOf("Semua", "Diajukan", "Selesai", "Ditolak")
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            FilterChip(
                selected = selectedFilter.equals(filter, ignoreCase = true),
                onClick = { onFilterChange(filter) },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF026AA1),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
fun PengajuanCard(pengajuan: Pengajuan, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("${Routes.KEMAHASISWAAN_DETAIL_PENGAJUAN}/${pengajuan.id}")
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Description, null, tint = Color(0xFF94A3B8))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(pengajuan.tujuanSurat, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF314158))
                Text("${pengajuan.mahasiswaNama} (${pengajuan.mahasiswaNim})", fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(pengajuan.tanggalPengajuan, fontSize = 12.sp, color = Color.Gray)
                }
            }
            StatusBadgeSmall(status = pengajuan.status)
        }
    }
}

@Composable
fun StatusBadgeSmall(status: String) {
    val (bgColor, txtColor) = when (status.uppercase()) {
        "SELESAI" -> Color(0xFFDCFCE7) to Color(0xFF16A34A)
        "DITOLAK" -> Color(0xFFFEE2E2) to Color(0xFFDC2626)
        else -> Color(0xFFFFFBEB) to Color(0xFFB45309)
    }
    Surface(color = bgColor, shape = RoundedCornerShape(8.dp)) {
        Text(
            text = status,
            color = txtColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun EmptyState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Inbox, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
            Spacer(Modifier.height(8.dp))
            Text("Tidak ada data ditemukan lur.", color = Color.Gray)
        }
    }
}