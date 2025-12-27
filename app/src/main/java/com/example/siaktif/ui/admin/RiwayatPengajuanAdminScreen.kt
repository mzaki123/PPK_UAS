package com.example.siaktif.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
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

data class PengajuanAdmin(
    val name: String,
    val nim: String,
    val date: String,
    val type: String,
    val status: String,
    val imageUrl: String
)

val pengajuanList = listOf(
    PengajuanAdmin("Budi Santoso", "2101530012", "12 Okt 2023", "Surat Keterangan Aktif", "Diajukan", ""),
    PengajuanAdmin("Siti Aminah", "2101530045", "10 Okt 2023", "Surat Keterangan Lulus", "Diterima", ""),
    PengajuanAdmin("Rizky Pratama", "2101530112", "08 Okt 2023", "Surat Keterangan Aktif", "Ditolak", ""),
    PengajuanAdmin("Dewi Lestari", "2101530225", "05 Okt 2023", "Surat Pengantar Magang", "Diterima", ""),
    PengajuanAdmin("Andi Wijaya", "2101530311", "01 Okt 2023", "Surat Keterangan Aktif", "Diajukan", "")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatPengajuanAdminScreen() {
    Scaffold(
        topBar = { RiwayatPengajuanTopSection() },
        containerColor = Color(0xFFF6F6F8)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pengajuanList) { pengajuan ->
                PengajuanListItem(
                    pengajuan = pengajuan,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun RiwayatPengajuanTopSection() {
    var searchQuery by remember { mutableStateOf("") }
    val filters = listOf("Semua", "Diajukan", "Diterima", "Ditolak")
    var selectedFilter by remember { mutableStateOf("Semua") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF6F6F8))
            .padding(bottom = 8.dp)
    ) {
        // Title and Export Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Riwayat Pengajuan",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* TODO: Export */ }) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Export Laporan"
                )
            }
        }

        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari NIM atau Nama Mahasiswa...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(Modifier.height(16.dp))

        // Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
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

        // Count and Sort
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Menampilkan ${pengajuanList.size} pengajuan",
                fontSize = 12.sp,
                color = Color.Gray
            )
            TextButton(onClick = { /* TODO: Sort */ }) {
                Text("Terbaru")
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Sort")
            }
        }
    }
}

@Composable
fun PengajuanListItem(pengajuan: PengajuanAdmin, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = pengajuan.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    StatusBadge(status = pengajuan.status)
                }
                Text(
                    text = "NIM: ${pengajuan.nim}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    color = Color.Gray
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = pengajuan.date, fontSize = 12.sp, color = Color.Gray)
                    Text(text = " • ", fontSize = 12.sp, color = Color.Gray)
                    Text(text = pengajuan.type, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "Diterima" -> Color(0xFFD1FAE5) to Color(0xFF065F46)
        "Ditolak" -> Color(0xFFFEE2E2) to Color(0xFF991B1B)
        else -> Color(0xFFFEF3C7) to Color(0xFF92400E) // Diajukan
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RiwayatPengajuanAdminScreenPreview() {
    RiwayatPengajuanAdminScreen()
}
