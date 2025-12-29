package com.example.uas.ui.mahasiswa.home

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uas.R
import com.example.uas.ui.navigation.Routes
import com.example.uas.ui.theme.UASTheme

@Composable
fun HomeScreen(
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Dashboard", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("SIAKTIF Mobile", fontSize = 12.sp, color = Color.Gray)
            }
            IconButton(onClick = { /* TODO: Notifikasi */ }) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("MAHASISWA AKTIF", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    Text("Budi Santoso", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("NIM: 1234567890", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Statistics Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Statistik Pengajuan", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                "Semester Ini",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .background(Color(0xFFEEEEEE), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Stat Cards
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard("Total", "12", Icons.Filled.Folder, Color(0xFF314158), 0.8f, modifier = Modifier.weight(1f))
            StatCard("Diajukan", "2", Icons.Filled.HourglassTop, Color(0xFFE6A23C), 0.2f, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard("Diterima", "9", Icons.Filled.CheckCircle, Color(0xFF67C23A), 0.7f, modifier = Modifier.weight(1f))
            StatCard("Ditolak", "1", Icons.Filled.Cancel, Color(0xFFF56C6C), 0.1f, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons (SUDAH DIPERBAIKI)
        Button(
            onClick = { navController.navigate(Routes.FORM_PENGAJUAN) }, // <-- PERBAIKAN DI SINI
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ajukan Surat Baru", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = { navController.navigate(Routes.HISTORY) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Filled.History, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Riwayat Pengajuan", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Aktivitas Terkini", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Belum ada aktivitas terbaru.", color = Color.Gray, modifier=Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}

// ... (StatCard dan Preview tidak perlu diubah, biarkan seperti semula) ...

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color, progress: Float, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(title, fontSize = 14.sp, color = Color.Gray)
                Icon(icon, contentDescription = null, tint = color.copy(alpha = 0.3f), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, color = color)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = color,
                trackColor = color.copy(alpha = 0.2f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    UASTheme {
        HomeScreen(navController = rememberNavController())
    }
}
