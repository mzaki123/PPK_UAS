package com.example.uas.ui.mahasiswa.profil

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
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
import com.example.uas.R
import com.example.uas.ui.navigation.Routes
import com.example.uas.ui.theme.UASTheme

@Composable
fun ProfileScreen(navController: NavController, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profil Mahasiswa",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp).align(Alignment.Start)
        )

        // Profile Picture
        Box {
            Image(
                painter = painterResource(id = R.drawable.avatar), // Make sure you have avatar.png in res/drawable
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            IconButton(
                onClick = { /*TODO: Handle image picking*/ },
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
        AssistChip(onClick = { /*No-op*/ }, label = { Text("Mahasiswa Aktif") })
        Text(text = "21041056 | TI-3A", fontSize = 16.sp, color = Color.Gray)
        Text(text = "budi.s@student.university.ac.id", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))

        // Menu
        ProfileMenuItem(icon = Icons.Filled.Edit, text = "Edit Profil", description = "Perbarui data diri anda", onClick = { navController.navigate(Routes.EDIT_PROFILE) })
        ProfileMenuItem(icon = Icons.Filled.Lock, text = "Ganti Password", description = "Amankan akun anda", onClick = { navController.navigate(Routes.CHANGE_PASSWORD) })
        ProfileMenuItem(icon = Icons.AutoMirrored.Filled.Help, text = "Bantuan", description = "Pusat bantuan & FAQ", onClick = { /*TODO*/ })
        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout Icon", tint = Color.Red)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Logout", color = Color.Red)
        }
        Text(text = "Versi Aplikasi 1.0.2", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, text: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(text = description, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = "Go to $text")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    UASTheme {
        ProfileScreen(rememberNavController(), onLogout = {})
    }
}
