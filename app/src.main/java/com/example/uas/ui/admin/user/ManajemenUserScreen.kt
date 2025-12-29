package com.example.uas.ui.admin.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uas.data.repository.UserRepository
import com.example.uas.model.User
import com.example.uas.service.RetrofitInstance
import com.example.uas.ui.theme.UASTheme

val filters = listOf("Semua", "Mahasiswa", "Kemahasiswaan", "Admin")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManajemenUserScreen(onUserClick: (Long) -> Unit = {}) {
    val viewModelFactory = remember {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return UserViewModel(UserRepository(RetrofitInstance.api)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    val userViewModel: UserViewModel = viewModel(factory = viewModelFactory)
    val userState by userViewModel.userState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    LaunchedEffect(Unit) {
        userViewModel.getAllUsers()
    }

    Scaffold(
        topBar = { ManajemenUserTopAppBar() },
        containerColor = Color(0xFFF6F6F8)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            item {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari nama atau email...", color = Color(0xFF9CA3AF)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF4C669A)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF026AA1),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            when (val state = userState) {
                is UserUiState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is UserUiState.Success -> {
                    items(state.users) { user ->
                        UserListItem(
                            user = user,
                            onClick = { onUserClick(user.id) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
                is UserUiState.Error -> {
                    item {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

// TopAppBar, UserListItem, and RoleBadge composables remain the same
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManajemenUserTopAppBar() {
    TopAppBar(
        title = { Text("Manajemen User", color = Color.White, fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = { /* TODO: Add user */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add User", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF314158)
        )
    )
}

@Composable
fun UserListItem(user: User, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Avatar",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color(0xFF0D121B)
                        )
                        Text(
                            text = user.email,
                            fontSize = 14.sp,
                            color = Color(0xFF4C669A)
                        )
                    }
                }
                IconButton(onClick = { /* TODO: More options */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = Color(0xFF9CA3AF)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            RoleBadge(role = user.role)
        }
    }
}

@Composable
fun RoleBadge(role: String) {
    val (backgroundColor, textColor) = when (role) {
        "Mahasiswa" -> Color(0xFFEFF6FF) to Color(0xFF2563EB)
        "Kemahasiswaan" -> Color(0xFFF5F3FF) to Color(0xFF7C3AED)
        "Admin" -> Color(0xFFFFF7ED) to Color(0xFFEA580C)
        else -> Color(0xFFF1F5F9) to Color(0xFF475569)
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = role,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ManajemenUserScreenPreview() {
    UASTheme {
       // Mock data for preview
        val users = listOf(User(1, "Ahmad Santoso", "ahmad@student.univ.ac.id", "Mahasiswa"))
        // You can create a mock ViewModel or pass the state directly for preview
    }
}
