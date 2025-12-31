package com.example.uas.ui.kemahasiswaan.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uas.model.Pengajuan
import com.example.uas.ui.kemahasiswaan.KemahasiswaanViewModel
import com.example.uas.ui.kemahasiswaan.PengajuanDetailUiState
import com.example.uas.ui.kemahasiswaan.UpdateStatusUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPengajuanScreen(
    navController: NavController,
    viewModel: KemahasiswaanViewModel,
    pengajuanId: Long
) {
    val detailState by viewModel.pengajuanDetailState.collectAsState()
    val updateState by viewModel.updateStatusState.collectAsState()
    val notes by viewModel.notes.collectAsState()

    var showApproveDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }

    // Memicu pencarian data di memori lokal (Frontend Lookup)
    LaunchedEffect(pengajuanId) {
        viewModel.getPengajuanById(pengajuanId)
    }

    // Jika update status berhasil, otomatis kembali ke daftar
    LaunchedEffect(updateState) {
        if (updateState is UpdateStatusUiState.Success) {
            navController.popBackStack()
            viewModel.resetUpdateState() // Reset agar dialog tidak muncul lagi saat kembali
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pengajuan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        when (val state = detailState) {
            is PengajuanDetailUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF026AA1))
                }
            }
            is PengajuanDetailUiState.Success -> {
                val pengajuan = state.pengajuan
                Scaffold(
                    modifier = Modifier.padding(paddingValues),
                    bottomBar = {
                        // Tombol aksi hanya muncul jika status masih DIAJUKAN
                        if (pengajuan.status.equals("DIAJUKAN", ignoreCase = true)) {
                            ActionFooter(
                                onApproveClick = { showApproveDialog = true },
                                onRejectClick = { showRejectDialog = true }
                            )
                        }
                    }
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(Color(0xFFF6F6F8)),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        item { StudentProfileSection(pengajuan) }
                        item { StatusIndicatorSection(pengajuan.status) }
                        item { ApplicationDetailsCard(pengajuan) }
                        item { SupportingFilesSection() }

                        // Input catatan hanya muncul jika status masih DIAJUKAN
                        if (pengajuan.status.equals("DIAJUKAN", ignoreCase = true)) {
                            item {
                                VerificationNotesInput(
                                    value = notes,
                                    onValueChange = { viewModel.onNotesChange(it) }
                                )
                            }
                        }
                    }
                }
            }
            is PengajuanDetailUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
            else -> {}
        }
    }

    // Dialog Konfirmasi Setuju
    if (showApproveDialog) {
        ConfirmationDialog(
            title = "Setujui Pengajuan?",
            message = "Surat akan ditandai SELESAI dan mahasiswa akan menerima notifikasi.",
            onConfirm = {
                // Sesuai Enum di Backend: SELESAI
                viewModel.updatePengajuanStatus(pengajuanId, "SELESAI")
                showApproveDialog = false
            },
            onDismiss = { showApproveDialog = false },
            confirmButtonText = "Ya, Selesaikan",
            icon = Icons.Default.CheckCircle, // Gunakan icon standard
            iconColor = Color(0xFF16A34A)
        )
    }

    // Dialog Konfirmasi Tolak
    if (showRejectDialog) {
        ConfirmationDialog(
            title = "Tolak Pengajuan?",
            message = "Apakah Anda yakin menolak pengajuan ini?",
            onConfirm = {
                // Sesuai Enum di Backend: DITOLAK
                viewModel.updatePengajuanStatus(pengajuanId, "DITOLAK")
                showRejectDialog = false
            },
            onDismiss = { showRejectDialog = false },
            confirmButtonText = "Tolak Pengajuan",
            icon = Icons.Default.Warning,
            iconColor = Color(0xFFDC2626)
        )
    }
}

@Composable
fun StudentProfileSection(pengajuan: Pengajuan) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(60.dp).clip(CircleShape).background(Color(0xFFE2E8F0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(pengajuan.mahasiswaNama, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF314158))
            Text("NIM: ${pengajuan.mahasiswaNim}", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun StatusIndicatorSection(status: String) {
    val (backgroundColor, textColor, label) = when (status.uppercase()) {
        "SELESAI" -> Triple(Color(0xFFDCFCE7), Color(0xFF16A34A), "Selesai")
        "DITOLAK" -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), "Ditolak")
        else -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), "Diajukan")
    }

    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth().background(backgroundColor, RoundedCornerShape(12.dp)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, null, tint = textColor, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Status Verifikasi", fontWeight = FontWeight.SemiBold, color = textColor, fontSize = 14.sp)
        }
        Surface(color = Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp)) {
            Text(label, fontWeight = FontWeight.Bold, color = textColor, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 12.sp)
        }
    }
}

@Composable
fun ApplicationDetailsCard(pengajuan: Pengajuan) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Rincian Pengajuan", fontWeight = FontWeight.Bold, color = Color(0xFF64748B), fontSize = 12.sp)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DetailItem(label = "Tujuan Surat", value = pengajuan.tujuanSurat)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color(0xFFF1F5F9))
                DetailItem(label = "Tanggal Masuk", value = pengajuan.tanggalPengajuan, icon = Icons.Default.CalendarToday)
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, icon: ImageVector? = null) {
    Column {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(icon, null, modifier = Modifier.size(16.dp), tint = Color(0xFF026AA1))
                Spacer(Modifier.width(8.dp))
            }
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFF314158))
        }
    }
}

@Composable
fun SupportingFilesSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Dokumen Pendukung", fontWeight = FontWeight.Bold, color = Color(0xFF64748B), fontSize = 12.sp)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PictureAsPdf, null, tint = Color(0xFFEF4444), modifier = Modifier.size(32.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("KTM_Mahasiswa.pdf", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                        Text("1.2 MB", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                IconButton(onClick = { /* View File */ }) {
                    Icon(Icons.Default.Visibility, null, tint = Color(0xFF026AA1))
                }
            }
        }
    }
}

@Composable
fun VerificationNotesInput(value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Catatan Tambahan (Opsional)", fontWeight = FontWeight.Bold, color = Color(0xFF314158), fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Tulis alasan penolakan atau instruksi...", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF026AA1),
                unfocusedBorderColor = Color(0xFFE2E8F0)
            )
        )
    }
}

@Composable
fun ActionFooter(onApproveClick: () -> Unit, onRejectClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 16.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onRejectClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFEF4444)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
            ) {
                Text("Tolak", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onApproveClick,
                modifier = Modifier.weight(2f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
            ) {
                Text("Setujui Pengajuan", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmButtonText: String,
    icon: ImageVector,
    iconColor: Color
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(icon, null, tint = iconColor, modifier = Modifier.size(40.dp)) },
        title = { Text(title, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
        text = { Text(message, textAlign = TextAlign.Center) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if(iconColor == Color.Red) Color(0xFFEF4444) else Color(0xFF16A34A))
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Batal", color = Color.Gray) }
        }
    )
}