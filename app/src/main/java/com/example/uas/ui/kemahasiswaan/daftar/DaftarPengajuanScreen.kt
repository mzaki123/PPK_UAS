package com.example.uas.ui.kemahasiswaan.daftar

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
    val pengajuanState by viewModel.pengajuanListState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    LaunchedEffect(Unit) {
        viewModel.getAllPengajuan()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SearchBarSection(searchQuery, onQueryChange = { searchQuery = it })
        FilterSection(selectedFilter, onFilterChange = { selectedFilter = it })

        when (val state = pengajuanState) {
            is PengajuanListUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is PengajuanListUiState.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.pengajuan.filter {
                        (it.mahasiswaNama.contains(searchQuery, ignoreCase = true) ||
                         it.mahasiswaNim.contains(searchQuery, ignoreCase = true)) &&
                        (selectedFilter == "Semua" || it.status.equals(selectedFilter, ignoreCase = true))
                    }) { pengajuan ->
                        PengajuanCard(pengajuan = pengajuan, navController = navController)
                    }
                }
            }
            is PengajuanListUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {}
        }
    }
}

@Composable
fun SearchBarSection(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Cari berdasarkan NIM/Nama") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = { /*TODO: Sort action*/ },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
        ) {
            Icon(Icons.Default.Sort, contentDescription = "Sort")
        }
    }
}

@Composable
fun FilterSection(selectedFilter: String, onFilterChange: (String) -> Unit) {
    val filters = listOf("Semua", "Diajukan", "Diterima", "Ditolak")
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = selectedFilter.equals(filter, ignoreCase = true),
                onClick = { onFilterChange(filter) },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
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
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                navController.navigate("${Routes.KEMAHASISWAAN_DETAIL_PENGAJUAN}/${pengajuan.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.LightGray))
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(pengajuan.tujuanSurat, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Text("${pengajuan.mahasiswaNama} (${pengajuan.mahasiswaNim})", fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                        }
                       StatusBadge(status = pengajuan.status)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, "Tanggal", modifier = Modifier.size(14.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(pengajuan.tanggalPengajuan, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (backgroundColor, textColor, icon) = when (status) {
        "SELESAI" -> Triple(Color(0xFFDCFCE7), Color(0xFF16A34A), Icons.Default.CheckCircle)
        "DITOLAK" -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), Icons.Default.Cancel)
        else -> Triple(Color(0xFFFFFBEB), Color(0xFFB45309), Icons.Default.HourglassTop)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(backgroundColor, RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(icon, status, tint = textColor, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(status, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
