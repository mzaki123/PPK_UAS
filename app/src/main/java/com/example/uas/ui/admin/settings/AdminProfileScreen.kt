package com.example.uas.ui.admin.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.uas.ui.theme.UASTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(onLogout: () -> Unit) {
    var showAboutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color(0xFFF6F6F8)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ProfileHeader()
            Spacer(modifier = Modifier.height(24.dp))
            SettingsMenu(onAboutClick = { showAboutDialog = true }, onLogoutClick = onLogout)
        }
    }

    if (showAboutDialog) {
        AboutAppDialog(onDismiss = { showAboutDialog = false })
    }
}

@Composable
fun ProfileHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2E8F0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile Picture",
                tint = Color(0xFF64748B),
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Admin Utama",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D121B)
        )
    }
}

@Composable
fun SettingsMenu(onAboutClick: () -> Unit, onLogoutClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Account Security
        SettingsGroup(
            title = "Keamanan Akun",
            items = listOf(
                SettingItemData(
                    icon = Icons.Default.Lock,
                    title = "Ganti Password",
                    subtitle = "Ubah kata sandi akun anda",
                    onClick = { /*TODO*/ }
                )
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Preferences
        SettingsGroup(
            title = "Preferensi",
            items = listOf(
                SettingItemData(
                    icon = Icons.Default.Info,
                    title = "About App",
                    subtitle = "Versi 1.0.2 (Build 2023)",
                    onClick = onAboutClick
                )
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Logout Button
        Button(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFFDC2626)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Logout")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keluar Aplikasi", fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "SIAKTIF © 2023 All Rights Reserved",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
fun SettingsGroup(title: String, items: List<SettingItemData>) {
    Column {
        Text(
            text = title.uppercase(),
            color = Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingItem(item)
                    if (index < items.size - 1) {
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

data class SettingItemData(
    val icon: ImageVector,
    val title: String,
    val subtitle: String? = null,
    val onClick: () -> Unit
)

@Composable
fun SettingItem(data: SettingItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = data.onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = data.icon,
            contentDescription = data.title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(data.title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            data.subtitle?.let {
                Text(it, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Go to ${data.title}",
            tint = Color.Gray
        )
    }
}

@Composable
fun AboutAppDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("SIAKTIF", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Versi 1.0.2 (Build 2023)",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "SIAKTIF adalah sistem manajemen pengajuan Surat Keterangan Mahasiswa (SKM) berbasis Android.",
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Tutup")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminProfileScreenPreview() {
    UASTheme {
        AdminProfileScreen(onLogout = {})
    }
}
