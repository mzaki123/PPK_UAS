package com.example.uas.ui.kemahasiswaan

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas.ui.theme.UASTheme

data class Pengajuan(
    val id: Int,
    val nama: String,
    val nim: String,
    val tanggal: String,
    val status: String
)

val dummyPengajuanList = listOf(
    Pengajuan(1, "Budi Santoso", "21000123", "12 Okt 2023 • 09:30 WIB", "Diajukan"),
    Pengajuan(2, "Rina Hartati", "21000882", "13 Okt 2023 • 14:15 WIB", "Diajukan"),
    Pengajuan(3, "Ahmad Fauzi", "21000456", "10 Okt 2023 • 11:20 WIB", "Diterima"),
    Pengajuan(4, "Siti Aminah", "21000567", "09 Okt 2023 • 08:00 WIB", "Ditolak")
)

@Composable
fun DaftarPengajuanScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Pengajuan", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth()) },
                 navigationIcon = {
                    IconButton(onClick = { /* Handle back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchBarSection(searchQuery, onQueryChange = { searchQuery = it })
            FilterSection(selectedFilter, onFilterChange = { selectedFilter = it })
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(dummyPengajuanList.filter {
                    (it.nama.contains(searchQuery, ignoreCase = true) || it.nim.contains(searchQuery, ignoreCase = true)) &&
                    (selectedFilter == "Semua" || it.status == selectedFilter)
                }) { pengajuan ->
                    PengajuanCard(pengajuan = pengajuan)
                }
            }
        }
    }
}

@Composable
fun SearchBarSection(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Cari berdasarkan NIM/Nama") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp)),
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
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
fun PengajuanCard(pengajuan: Pengajuan) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                             Text(pengajuan.nama, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                             Text("NIM: ${pengajuan.nim}", fontSize = 12.sp, color = Color.Gray)
                        }
                       StatusBadge(status = pengajuan.status)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Tanggal", modifier = Modifier.size(14.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(pengajuan.tanggal, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            if(pengajuan.status == "Diajukan"){
                 Button(
                    onClick = { /*TODO: Navigate to detail*/ },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Verifikasi", fontSize = 14.sp)
                }
            } else {
                 OutlinedButton(
                    onClick = { /*TODO: Navigate to detail*/ },
                    modifier = Modifier.align(Alignment.End),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Lihat Detail", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (backgroundColor, textColor, icon) = when (status) {
        "Diterima" -> Triple(Color(0xFFDCFCE7), Color(0xFF16A34A), Icons.Default.CheckCircle)
        "Ditolak" -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), Icons.Default.Cancel)
        else -> Triple(Color(0xFFFFFBEB), Color(0xFFB45309), Icons.Default.HourglassTop)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = status, tint = textColor, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(status, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}


@Preview(showBackground = true)
@Composable
fun DaftarPengajuanScreenPreview() {
    UASTheme {
        DaftarPengajuanScreen()
    }
}
