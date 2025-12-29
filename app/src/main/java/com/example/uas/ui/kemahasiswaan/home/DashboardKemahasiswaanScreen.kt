package com.example.uas.ui.kemahasiswaan.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uas.ui.navigation.Routes

// --- FUNGSI UTAMA: Merakit semua komponen ---
@Composable
fun DashboardKemahasiswaanScreen(navController: NavController) {
    // LazyColumn agar seluruh layar bisa di-scroll dengan lancar
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F8)) // background-light
    ) {
        item { HeaderSection() }
        item { StatisticsSection() }
        item { RecentActivitySection(navController = navController) }
    }
}


// --- 1. Header Section ---
@Composable
fun HeaderSection() {
    val brandDark = Color(0xFF314158)
    val whiteTransparent = Color.White.copy(alpha = 0.1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(brandDark)
            .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 32.dp)
    ) {
        // --- Sapaan dan Tombol Notifikasi ---
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Selamat Datang,", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text("Staff Kemahasiswaan", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp)
                }
                BadgedBox(
                    badge = {
                        Badge(
                            modifier = Modifier.border(1.dp, brandDark, CircleShape),
                            containerColor = Color.Red
                        ) { /* Badge content can be empty */ }
                    }
                ) {
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.background(whiteTransparent, CircleShape)
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = Color.White)
                    }
                }
            }

            // --- Info Profil Staff ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(whiteTransparent, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Budi Santoso, M.Pd.", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("NIP. 19850101 201012 1 002", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                }
            }
        }
    }
}


// --- 2. Statistics Section ---
@Composable
fun StatisticsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(Icons.Default.BarChart, contentDescription = "Statistik", tint = Color(0xFF135BEC)) // primary color
            Spacer(modifier = Modifier.width(8.dp))
            Text("Statistik Pengajuan", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        // Grid 2x2 untuk kartu statistik
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    title = "Total Masuk",
                    value = "152",
                    subtitle = "+5%",
                    icon = Icons.Default.Inbox,
                    iconBgColor = Color(0xFFEFF6FF), // blue-50
                    iconColor = Color(0xFF135BEC),   // primary
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Menunggu",
                    value = "12",
                    subtitle = "Perlu tindakan",
                    icon = Icons.Default.PendingActions,
                    iconBgColor = Color(0xFFFFF7ED), // orange-50/100
                    iconColor = Color(0xFFC2410C),   // orange-600
                    cardBgColor = Color(0xFFFFFBEB), // orange-50
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    title = "Disetujui",
                    value = "8",
                    subtitle = "Hari ini",
                    icon = Icons.Default.CheckCircle,
                    iconBgColor = Color(0xFFF0FDF4), // emerald-50
                    iconColor = Color(0xFF059669),   // emerald-600
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Ditolak",
                    value = "2",
                    subtitle = "Total",
                    icon = Icons.Default.Cancel,
                    iconBgColor = Color(0xFFFEF2F2), // red-50
                    iconColor = Color(0xFFDC2626),   // red-600
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// Komponen pembantu untuk kartu statistik
@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    cardBgColor: Color = MaterialTheme.colorScheme.surface
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.background(iconBgColor, RoundedCornerShape(8.dp)).padding(6.dp)
                ) {
                    Icon(icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(20.dp))
                }
                Text(title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}


// --- 3. Recent Activity Section ---
@Composable
fun RecentActivitySection(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Pengajuan Terbaru", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            TextButton(onClick = { navController.navigate(Routes.KEMAHASISWAAN_DAFTAR_PENGAJUAN) }) {
                Text("Lihat Semua")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Daftar aktivitas
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ActivityItem(
                name = "Ahmad Dahlan",
                details = "SKM - Beasiswa • 10m lalu",
                status = "Menunggu",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD86nSDgmwjl5y26o4GjEtOHTPbOKHQwgWWPV3YSAXGo2u8cX1FgCPqDolLRvrIZP6MNz59LFzXE3CnrWnYN2X-YdWkw93yg-axy-ABS6Ag-JITzm30V0DrNo1M80_oDMolzvKp4vbEBTimobDhImPwnKpa3O9S3ePM8n3M3aJVnz3jSjt6UwxpThyLH7JHCt6fWjjf2xOGO_H_S4NvjWkMV8vamMW0STtVZ1XY4VHz9UHLgzGwEL3mw-Wx3S7UTElzRZV_i4UjFnA"
            )
            ActivityItem(
                name = "Sarah Wibowo",
                details = "SKM - Lomba • 1j lalu",
                status = "Disetujui",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB7DTtuDSgmrvqtX-PdmS6R-t_G2YFz_HNZTX9zwuxzQDLTXG9AIcSbQnU-oICCl5PV0-oPoyKB0DS7gbMoaZWErl3s1nq56XroBwJrtsSRbbWWh7WYblyDl0yGP1awq7GVgaGXW4PDjR505f7Jwz3SRovdMHFOPOi3X48-G49KEb1QBQGtJOqmda0CncTpY6wlafqtDWPgnfaqgbW0XwBMglbeHdZLfGLbiKomYsN2TbsOt1Yy_rpQ0AC9OXh-zgifqrn-I9bZqw0"
            )
            ActivityItem(
                name = "Rizky Pratama",
                details = "SKM - UKT • 2j lalu",
                status = "Menunggu",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC0g7FJHbTBf9oxR2A-P2mpXoIpGRNYPaVicLZTHv90jJZVh27Tn6DC6sM7k3hWLpyJnyDKT-vt4beOvcXKUYhaz5kvTQ6R8PH3ET39AvRDM-fg-_MlmZTZ8ckc39wRnqCjhpEm5Jk9txbafdchEoTkjyKCwI5Be0s9KyftG09_FgHSoXGEvsVM6vUo4qv6d7P3iXtircqDjXq49w_TpQTsJ0gUmkuE8KcM7WXIaeUtCw1I1DcuQ1YqiC7VuLW0LBJOGCfmG90ZarU"
            )
        }

        // Tombol Lihat Semua Pengajuan
        OutlinedButton(
            onClick = { navController.navigate(Routes.KEMAHASISWAAN_DAFTAR_PENGAJUAN) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Lihat Semua Pengajuan")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}

// Komponen pembantu untuk item aktivitas
@Composable
fun ActivityItem(name: String, details: String, status: String, imageUrl: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(details, fontSize = 12.sp, color = Color.Gray)
                }
            }
            StatusBadge(status = status)
        }
    }
}

// Komponen pembantu untuk status badge
@Composable
fun StatusBadge(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "Disetujui" -> Color(0xFFD1FAE5) to Color(0xFF065F46)
        "Ditolak" -> Color(0xFFFEE2E2) to Color(0xFF991B1B)
        else -> Color(0xFFFFFBEB) to Color(0xFF92400E) // Menunggu
    }
    Text(
        text = status,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

// --- Preview untuk melihat hasil di Android Studio ---
@Preview(showBackground = true)
@Composable
fun DashboardKemahasiswaanScreenPreview() {
    // Gunakan NavController bohongan untuk preview
    DashboardKemahasiswaanScreen(navController = rememberNavController())
}

