package com.example.uas.ui.admin.home

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.data.repository.DashboardRepository
import com.example.uas.model.response.DashboardStatsResponse
import com.example.uas.service.RetrofitInstance
import com.example.uas.ui.theme.UASTheme

@Composable
fun AdminDashboardScreen() {
    val viewModelFactory = remember {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return DashboardViewModel(DashboardRepository(RetrofitInstance.api)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    val dashboardViewModel: DashboardViewModel = viewModel(factory = viewModelFactory)
    val dashboardState by dashboardViewModel.dashboardState.collectAsState()

    LaunchedEffect(Unit) {
        dashboardViewModel.getDashboardStats()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF6F6F8)),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                when (val state = dashboardState) {
                    is DashboardUiState.Loading -> Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                    is DashboardUiState.Success -> DashboardHeader(state.stats)
                    is DashboardUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                    else -> {} // Idle
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Activity", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF314158))
                    TextButton(onClick = { /*TODO*/ }) {
                        Text("See All", color = Color(0xFF026AA1), fontWeight = FontWeight.Medium)
                    }
                }
            }
            items(recentActivities) { activity ->
                RecentActivityItem(activity = activity, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
            }
        }
    }
}

@Composable
fun DashboardHeader(stats: DashboardStatsResponse) {
    Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF6F6F8))) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp).background(
                    color = Color(0xFF314158),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
            )
            Spacer(modifier = Modifier.height(80.dp))
        }

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Selamat Datang,", color = Color(0xFFADC8E6), fontSize = 14.sp)
                    Text("Admin Panel", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, "Profile", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text("Statistik", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Total Users", stats.totalUsers.toString(), Icons.Default.Group, Modifier.weight(1f))
                    StatCard("Mahasiswa", stats.mahasiswa.toString(), Icons.Default.School, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Kemahasiswaan", stats.kemahasiswaan.toString(), Icons.Default.LocalPolice, Modifier.weight(1f))
                    StatCard("Pengajuan", stats.pengajuan.toString(), Icons.Default.Description, Modifier.weight(1f))
                }
            }
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
                    Icon(icon, title, tint = Color(0xFF026AA1), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, color = Color(0xFF64748B), fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = Color(0xFF314158), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

data class Activity(
    val title: String,
    val subtitle: String,
    val time: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconColor: Color
)

val recentActivities = listOf(
    Activity("New User Registration", "Budi Santoso - Mahasiswa", "2m ago", Icons.Default.Person, Color(0xFFE3F2FD), Color(0xFF026AA1)),
    Activity("SKM Submission", "Siti Aminah - Pending Review", "15m ago", Icons.Default.Description, Color(0xFFFFF3E0), Color(0xFFFB8C00)),
    Activity("SKM Approved", "Ahmad Rizky - Approved by Kemahasiswaan", "1h ago", Icons.Default.Verified, Color(0xFFE8F5E9), Color(0xFF4CAF50)),
    Activity("Admin Login", "System access logged", "3h ago", Icons.Default.Login, Color(0xFFE3F2FD), Color(0xFF026AA1))
)

@Composable
fun RecentActivityItem(activity: Activity, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(40.dp).background(activity.iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(activity.icon, activity.title, tint = activity.iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(activity.title, color = Color(0xFF314158), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(activity.subtitle, color = Color(0xFF64748B), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(activity.time, color = Color(0xFF94A3B8), fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardScreenPreview() {
    UASTheme {
        AdminDashboardScreen()
    }
}
