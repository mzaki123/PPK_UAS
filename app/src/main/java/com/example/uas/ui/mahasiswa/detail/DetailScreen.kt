package com.example.uas.ui.mahasiswa.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uas.model.Pengajuan
import com.example.uas.ui.mahasiswa.MahasiswaViewModel
import com.example.uas.ui.mahasiswa.PengajuanDetailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPengajuanMahasiswaScreen(
    navController: NavController,
    viewModel: MahasiswaViewModel,
    pengajuanId: Long
) {
    val detailState by viewModel.detailState.collectAsState()

    LaunchedEffect(pengajuanId) {
        viewModel.getPengajuanById(pengajuanId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pengajuan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = detailState) {
            is PengajuanDetailUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is PengajuanDetailUiState.Success -> {
                val pengajuan = state.pengajuan
                DetailContent(
                    pengajuan = pengajuan,
                    modifier = Modifier.padding(paddingValues),
                    onBack = { navController.popBackStack() }
                )
            }
            is PengajuanDetailUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message, color = Color.Red)
                }
            }
            else -> {}
        }
    }
}

@Composable
fun DetailContent(
    pengajuan: Pengajuan,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val isSelesai = pengajuan.status.equals("DITERIMA", ignoreCase = true)
    val isDitolak = pengajuan.status.equals("DITOLAK", ignoreCase = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Status Card ---
        val statusColor = when {
            isSelesai -> Color(0xFF16A34A)
            isDitolak -> Color(0xFFDC2626)
            else -> Color(0xFFD97706)
        }

        val statusIcon = when {
            isSelesai -> Icons.Default.CheckCircle
            isDitolak -> Icons.Default.Cancel
            else -> Icons.Default.PendingActions
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(statusIcon, null, tint = statusColor, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Status Pengajuan", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = pengajuan.status.uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }
        }

        // --- Informasi Utama ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = Color(0xFF026AA1), modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Informasi Surat", fontWeight = FontWeight.Bold)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

                InfoRow(label = "ID Pengajuan", value = "#REQ-${pengajuan.id}")
                InfoRow(label = "Tanggal", value = pengajuan.tanggalPengajuan)
                InfoRow(label = "Tujuan Surat", value = pengajuan.tujuanSurat)
            }
        }

        // --- Bagian Dokumen (Jika Sudah Selesai) ---
        if (isSelesai) {
            Text("Surat Diterbitkan", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PictureAsPdf, null, tint = Color(0xFFEF4444))
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("SKM_${pengajuan.id}_Digital.pdf", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Sudah ditandatangani secara digital", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* Handle Download */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Download, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Download Surat Sekarang")
                    }
                }
            }
        }

        if (isDitolak) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, null, tint = Color.Red)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Pengajuan ditolak. Silakan cek kembali data atau hubungi bagian kemahasiswaan.",
                        fontSize = 12.sp,
                        color = Color(0xFF991B1B)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kembali ke Beranda", color = Color.Gray)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(4.dp))
    }
}