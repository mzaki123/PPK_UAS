package com.example.uas.ui.kemahasiswaan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas.ui.theme.UASTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilKemahasiswaanScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Kemahasiswaan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back */ }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                 colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ProfileHeader()
            AccountSection()
            Spacer(modifier = Modifier.weight(1f))
            LogoutButton()
            AppVersionText()
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(112.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
        ) {
            // Image here
            Icon(
                Icons.Default.PhotoCamera,
                contentDescription = "Edit Foto",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(8.dp)
                    .size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Budi Santoso, M.Pd", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text("Staf Kemahasiswaan", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("19800101 200501 1 001", fontSize = 14.sp, color = Color.Gray)
        Text("kemahasiswaan@univ.ac.id", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun AccountSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("AKUN", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            ProfileMenuItem(
                title = "Edit Profil",
                icon = Icons.Default.Edit
            )
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            ProfileMenuItem(
                title = "Ganti Password",
                icon = Icons.Default.Lock
            )
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /*TODO*/ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ArrowForwardIos, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
    }
}

@Composable
fun LogoutButton() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(Icons.Default.Logout, contentDescription = "Keluar")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Keluar Aplikasi", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AppVersionText() {
     Text(
        "Versi SIAKTIF 1.2.0",
        fontSize = 12.sp,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ProfilKemahasiswaanScreenPreview() {
    UASTheme {
        ProfilKemahasiswaanScreen()
    }
}
