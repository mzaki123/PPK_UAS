package com.example.uas.ui.admin.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen() {
    Scaffold(
        topBar = { AdminDashboardTopAppBar() },
        containerColor = Color(0xFFF6F6F8)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { StatsSection() }
            item { QuickActionsSection() }
            item { RecentActivitySection() }
        }
    }
}

@Composable
fun AdminDashboardTopAppBar() {
    Surface(
        color = Color(0xFF314158),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Selamat Datang,", color = Color(0xFFADC8E6), fontSize = 14.sp)
                Text("Admin Panel", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(Icons.Default.Notifications, "Notifications", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray))
            }
        }
    }
}

@Composable
fun StatsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Total Users", "1,240", Icons.Default.Group, Modifier.weight(1f))
            StatCard("Mahasiswa", "1,200", Icons.Default.School, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Kemahasiswaan", "35", Icons.Default.LocalPolice, Modifier.weight(1f))
            StatCard("Pengajuan", "450", Icons.Default.Description, Modifier.weight(1f))
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(32.dp).background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = title, tint = Color(0xFF026AA1))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, color = Color(0xFF64748B), fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = Color(0xFF314158), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun QuickActionsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Quick Actions",
            color = Color(0xFF314158),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionCard("Kelola\nUser", Icons.Default.ManageAccounts, Modifier.weight(1f))
            QuickActionCard("Lihat\nRiwayat", Icons.Default.History, Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun QuickActionCard(title: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            Modifier.fillMaxSize().background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF314158), Color(0xFF1E293B))
                )
            ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier.size(48.dp).background(Color.White.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun RecentActivitySection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text("Recent Activity", color = Color(0xFF314158), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = { /*TODO*/ }) { Text("See All", color = Color(0xFF026AA1)) }
        }
        Spacer(modifier = Modifier.height(8.dp))
        RecentActivityItem("New User Registration", "Budi Santoso - Mahasiswa", "2m ago", Icons.Default.PersonAdd, Color(0xFFE3F2FD), Color(0xFF026AA1))
        Spacer(modifier = Modifier.height(12.dp))
        RecentActivityItem("SKM Submission", "Siti Aminah - Pending Review", "15m ago", Icons.Default.FileCopy, Color(0xFFFFF3E0), Color(0xFFFB8C00))
        Spacer(modifier = Modifier.height(12.dp))
        RecentActivityItem("SKM Approved", "Ahmad Rizky - Approved by Kemahasiswaan", "1h ago", Icons.Default.Verified, Color(0xFFE8F5E9), Color(0xFF4CAF50))
        Spacer(modifier = Modifier.height(12.dp))
        RecentActivityItem("Admin Login", "System access logged", "3h ago", Icons.Default.Login, Color(0xFFE3F2FD), Color(0xFF026AA1))
    }
}

@Composable
fun RecentActivityItem(title: String, subtitle: String, time: String, icon: ImageVector, iconBgColor: Color, iconColor: Color) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(40.dp).background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = iconColor)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color(0xFF314158), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Color(0xFF64748B), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(time, color = Color(0xFF94A3B8), fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardScreenPreview() {
    AdminDashboardScreen()
}
