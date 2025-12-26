package com.example.uas.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uas.ui.components.BottomNavigationBar
import com.example.uas.ui.theme.UASTheme
import com.example.uas.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Mahasiswa") }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with actual image
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
                    Icon(Icons.Default.PhotoCamera, contentDescription = "Change Profile Picture", tint = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // User Info
            Text(text = "Budi Santoso", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Chip(onClick = { /*TODO*/ }, label = { Text("Mahasiswa Aktif") })
            Text(text = "21041056 | TI-3A", fontSize = 16.sp, color = Color.Gray)
            Text(text = "budi.s@student.university.ac.id", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))

            // Menu
            ProfileMenuItem(icon = Icons.Default.Edit, text = "Edit Profil", description = "Perbarui data diri anda")
            ProfileMenuItem(icon = Icons.Default.Lock, text = "Ganti Password", description = "Amankan akun anda")
            ProfileMenuItem(icon = Icons.Default.Help, text = "Bantuan", description = "Pusat bantuan & FAQ")
            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Logout Icon", tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Logout", color = Color.Red)
            }
            Text(text = "Versi Aplikasi 1.0.2", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

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
            Icon(Icons.Default.ChevronRight, contentDescription = null)
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
