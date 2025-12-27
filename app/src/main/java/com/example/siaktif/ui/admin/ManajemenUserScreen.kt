package com.example.siaktif.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class User(
    val name: String,
    val email: String,
    val role: String,
    val status: String,
    val imageUrl: String
)

val userList = listOf(
    User("Ahmad Santoso", "ahmad@student.univ.ac.id", "Mahasiswa", "Aktif", ""),
    User("Dr. Budi Raharjo", "budi@staff.univ.ac.id", "Kemahasiswaan", "Aktif", ""),
    User("Siti Aminah", "admin.siti@univ.ac.id", "Admin", "Nonaktif", ""),
    User("Doni Pratama", "doni.p@student.univ.ac.id", "Mahasiswa", "Nonaktif", "")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManajemenUserScreen() {
    Scaffold(
        topBar = { ManajemenUserTopAppBar() },
        containerColor = Color(0xFFF6F6F8)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { SearchAndFilterSection() }
            items(userList) { user ->
                UserListItem(user = user, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun ManajemenUserTopAppBar() {
    TopAppBar(
        title = {
            Text(
                "Manajemen User",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = { /* TODO: Add user */ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add User",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF314158)
        )
    )
}

@Composable
fun SearchAndFilterSection() {
    var searchQuery by remember { mutableStateOf("") }
    val filters = listOf("Semua", "Mahasiswa", "Kemahasiswaan", "Admin")
    var selectedFilter by remember { mutableStateOf("Semua") }

    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari nama atau email...", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                )
            }
        }
    }
}

@Composable
fun UserListItem(user: User, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = user.email,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
                IconButton(
                    onClick = { /* TODO: More options */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                UserTag(
                    text = user.role,
                    backgroundColor = Color(0xFFE0E7FF),
                    textColor = Color(0xFF4338CA)
                )
                UserTag(
                    text = user.status,
                    backgroundColor = if (user.status == "Aktif") Color(0xFFD1FAE5) else Color(0xFFF3F4F6),
                    textColor = if (user.status == "Aktif") Color(0xFF065F46) else Color(0xFF4B5563)
                )
            }
        }
    }
}

@Composable
fun UserTag(text: String, backgroundColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ManajemenUserScreenPreview() {
    ManajemenUserScreen()
}
