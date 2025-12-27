package com.example.uas.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uas.R
// Hapus import Scaffold, TopAppBar, ExperimentalMaterial3Api, BottomNavigationBar
import com.example.uas.ui.theme.UASTheme

// Hapus @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    // HAPUS SEMUA KODE SCAFFOLD

    // LANGSUNG MULAI DARI COLUMN KONTEN
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Tambahkan scroll jika konten panjang
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Karena TopAppBar dihapus, buat judul manual jika perlu
        Text(
            text = "Profil Mahasiswa",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Profile Picture
        Box {
            Image(
                painter = painterResource(id = R.drawable.avatar), // Ganti dengan gambar avatar yang benar
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(36.dp),
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Filled.PhotoCamera, contentDescription = "Change Profile Picture", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // User Info
        Text(text = "Budi Santoso", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        AssistChip(onClick = { /*TODO*/ }, label = { Text("Mahasiswa Aktif") })
        Text(text = "21041056 | TI-3A", fontSize = 16.sp, color = Color.Gray)
        Text(text = "budi.s@student.university.ac.id", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))

        // Menu
        ProfileMenuItem(icon = Icons.Filled.Edit, text = "Edit Profil", description = "Perbarui data diri anda")
        ProfileMenuItem(icon = Icons.Filled.Lock, text = "Ganti Password", description = "Amankan akun anda")
        ProfileMenuItem(icon = Icons.Filled.Help, text = "Bantuan", description = "Pusat bantuan & FAQ")
        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        OutlinedButton(
            onClick = { /*TODO: Handle logout logic, e.g., navigate to Login and clear back stack */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout Icon", tint = Color.Red)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Logout", color = Color.Red)
        }
        Text(text = "Versi Aplikasi 1.0.2", fontSize = 12.sp, color = Color.Gray)
    }
}


// Composable ProfileMenuItem dan Preview tidak perlu diubah.
// Biarkan seperti semula.

@Composable
fun ProfileMenuItem(icon: ImageVector, text: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(text = description, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    UASTheme {
        ProfileScreen(rememberNavController())
    }
}
