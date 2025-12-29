package com.example.uas.ui.kemahasiswaan.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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

    var showApproveDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }

    LaunchedEffect(pengajuanId) {
        viewModel.getPengajuanById(pengajuanId)
    }

    LaunchedEffect(updateState) {
        if (updateState is UpdateStatusUiState.Success) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pengajuan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        when (val state = detailState) {
            is PengajuanDetailUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is PengajuanDetailUiState.Success -> {
                val pengajuan = state.pengajuan
                Scaffold(
                    bottomBar = {
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
                            .padding(innerPadding) // Use innerPadding from the inner Scaffold
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        item { StudentProfileSection(pengajuan) }
                        item { StatusIndicatorSection(pengajuan.status) }
                        item { ApplicationDetailsCard(pengajuan) }
                        item { SupportingFilesSection() }
                        item { VerificationNotesInput(notes, onValueChange = { notes = it }) }
                    }
                }
            }
            is PengajuanDetailUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {}
        }
    }

    if (showApproveDialog) {
        ConfirmationDialog(
            title = "Terima Pengajuan?",
            message = "Surat akan diproses dan notifikasi akan dikirimkan. Tindakan ini tidak dapat dibatalkan.",
            onConfirm = {
                viewModel.updatePengajuanStatus(pengajuanId, "DITERIMA")
                showApproveDialog = false
            },
            onDismiss = { showApproveDialog = false },
            confirmButtonText = "Ya, Terima",
            icon = Icons.Default.Verified,
            iconColor = Color(0xFF16A34A)
        )
    }

    if (showRejectDialog) {
        ConfirmationDialog(
            title = "Tolak Pengajuan?",
            message = "Pastikan Anda telah memberikan catatan alasan penolakan.",
            onConfirm = {
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
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(Color.Gray))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(pengajuan.mahasiswaNama, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(pengajuan.mahasiswaNim, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun StatusIndicatorSection(status: String) {
    val (backgroundColor, textColor, text) = when (status) {
        "SELESAI" -> Triple(Color(0xFFDCFCE7), Color(0xFF16A34A), "Diterima")
        "DITOLAK" -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), "Ditolak")
        else -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), "Diajukan")
    }

    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth().background(backgroundColor, RoundedCornerShape(12.dp)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.PendingActions, "Status", tint = textColor)
            Spacer(Modifier.width(8.dp))
            Text("Status Saat Ini", fontWeight = FontWeight.SemiBold, color = textColor)
        }
        Text(
            text, fontWeight = FontWeight.Bold, color = textColor,
            modifier = Modifier.background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp)).padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp
        )
    }
}

@Composable
fun ApplicationDetailsCard(pengajuan: Pengajuan) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("INFORMASI SURAT", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tujuan Surat", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Text(pengajuan.tujuanSurat, style = MaterialTheme.typography.bodyLarge)
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text("Tanggal Pengajuan", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, "Tanggal", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(pengajuan.tanggalPengajuan, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// Other composables (SupportingFilesSection, ActionFooter, etc.) remain largely the same for UI structure
@Composable
fun SupportingFilesSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("FILE PENDUKUNG", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            FileItem("Scan_KTM_Budi.pdf", "1.2 MB • PDF Document", Icons.Default.PictureAsPdf, Color.Red)
        }
    }
}

@Composable
fun FileItem(name: String, details: String, icon: ImageVector, iconColor: Color) {
     Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(icon, name, tint = iconColor, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(details, fontSize = 12.sp, color = Color.Gray)
            }
        }
        IconButton(onClick = { /* Handle view file */ }) {
            Icon(Icons.Default.Visibility, "Lihat Dokumen", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun VerificationNotesInput(value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Catatan Verifikasi", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Tulis catatan penolakan atau pesan tambahan (Opsional)...") },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun ActionFooter(onApproveClick: () -> Unit, onRejectClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 8.dp) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onRejectClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                border = BorderStroke(2.dp, Color.Red)
            ) {
                Icon(Icons.Default.Close, "Tolak")
                Spacer(Modifier.width(4.dp))
                Text("Tolak", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onApproveClick,
                modifier = Modifier.weight(2f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
            ) {
                Icon(Icons.Default.Check, "Terima")
                Spacer(Modifier.width(4.dp))
                Text("Terima Pengajuan", fontWeight = FontWeight.Bold)
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
        title = { Text(title, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
        text = { Text(message, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
        icon = {
             Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(iconColor.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(32.dp))
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                 colors = ButtonDefaults.buttonColors(containerColor = if(iconColor == Color.Red) Color.Red else Color(0xFF16A34A))
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Batal") }
        }
    )
}
