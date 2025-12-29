package com.example.uas.ui.mahasiswa.pengajuan

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uas.ui.mahasiswa.CreatePengajuanUiState
import com.example.uas.ui.mahasiswa.MahasiswaViewModel
import com.example.uas.ui.theme.UASTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavController,
    viewModel: MahasiswaViewModel
) {
    var tujuan by remember { mutableStateOf("") }
    val createState by viewModel.createState.collectAsState()

    LaunchedEffect(createState) {
        if (createState is CreatePengajuanUiState.Success) {
            viewModel.resetCreateState() // Reset state to avoid re-triggering
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Form Pengajuan Surat") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Button(
                    onClick = { viewModel.createPengajuan(tujuan) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = tujuan.isNotBlank() && createState !is CreatePengajuanUiState.Loading
                ) {
                    if (createState is CreatePengajuanUiState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text(text = "Ajukan Permohonan")
                    }
                }
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Batal")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, "Info Icon", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Pastikan data diri Anda di profil sudah benar sebelum mengajukan surat.", fontSize = 14.sp)
                }
            }

            OutlinedTextField(
                value = tujuan,
                onValueChange = { tujuan = it },
                label = { Text("Jelaskan keperluan pengajuan surat...") },
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )

            Text("File Pendukung (Opsional)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier.fillMaxWidth().dashedBorder(1.dp, Color.Gray, 8.dp).padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CloudUpload, "Upload Icon", tint = Color.Gray)
                    Text("Tap to upload", color = Color.Gray)
                }
            }
        }
    }
}

fun Modifier.dashedBorder(width: Dp, color: Color, cornerRadius: Dp) = this.drawBehind {
    val stroke = Stroke(
        width = width.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius.toPx())
    )
}

@Preview(showBackground = true)
@Composable
fun FormScreenPreview() {
    UASTheme {
        // FormScreen(rememberNavController(), /* viewModel needed */)
    }
}
