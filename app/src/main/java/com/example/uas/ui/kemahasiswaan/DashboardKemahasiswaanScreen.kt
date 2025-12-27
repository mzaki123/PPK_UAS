package com.example.uas.ui.kemahasiswaan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas.ui.theme.UASTheme

@Composable
fun DashboardKemahasiswaanScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item { HeaderSection() }
            item { StatisticsSection() }
            item { RecentActivitySection() }
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(Color(0xFF314158))
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Selamat Datang,", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text("Staff Kemahasiswaan", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Budi Santoso, M.Pd.", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("NIP. 19850101 201012 1 002", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun StatisticsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(Icons.Default.BarChart, contentDescription = "Statistik", tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Statistik Pengajuan", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
             Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Total Masuk", "152", "+5%", Modifier.weight(1f))
                StatCard("Menunggu", "12", "Perlu tindakan", Modifier.weight(1f), highlight = true)
             }
             Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Disetujui", "8", "Hari ini", Modifier.weight(1f))
                StatCard("Ditolak", "2", "Total", Modifier.weight(1f))
             }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, subtitle: String, modifier: Modifier = Modifier, highlight: Boolean = false) {
    val backgroundColor = if (highlight) Color(0xFFFFFBEB) else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (highlight) Color(0xFFB45309) else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 12.sp, color = textColor.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, fontSize = 12.sp, color = textColor.copy(alpha = 0.7f))
        }
    }
}


@Composable
fun RecentActivitySection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Pengajuan Terbaru", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            TextButton(onClick = { /*TODO*/ }) {
                Text("Lihat Semua")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ActivityItem("Ahmad Dahlan", "SKM - Beasiswa • 10m lalu", "Menunggu")
            ActivityItem("Sarah Wibowo", "SKM - Lomba • 1j lalu", "Disetujui")
            ActivityItem("Rizky Pratama", "SKM - UKT • 2j lalu", "Menunggu")
        }
         Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Lihat Semua Pengajuan")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
fun ActivityItem(name: String, details: String, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(name, fontWeight = FontWeight.SemiBold)
                    Text(details, fontSize = 12.sp, color = Color.Gray)
                }
            }
            Text(
                text = status,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = when (status) {
                    "Disetujui" -> Color(0xFF16A34A)
                    "Ditolak" -> Color(0xFFDC2626)
                    else -> Color(0xFFD97706)
                },
                modifier = Modifier
                    .background(
                        color = when (status) {
                            "Disetujui" -> Color(0xFFDCFCE7)
                            "Ditolak" -> Color(0xFFFEE2E2)
                            else -> Color(0xFFFEF3C7)
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DashboardKemahasiswaanScreenPreview() {
    UASTheme {
        DashboardKemahasiswaanScreen()
    }
}
