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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas.ui.theme.UASTheme
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPengajuanScreen(navController: NavController) {
    var showApproveDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pengajuan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back */ }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            ActionFooter(
                onApproveClick = { showApproveDialog = true },
                onRejectClick = { showRejectDialog = true }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item { StudentProfileSection() }
            item { StatusIndicatorSection() }
            item { ApplicationDetailsCard() }
            item { SupportingFilesSection() }
            item { VerificationNotesInput(notes, onValueChange = { notes = it }) }
        }
    }

    if (showApproveDialog) {
        ConfirmationDialog(
            title = "Terima Pengajuan?",
            message = "Surat akan diproses dan notifikasi akan dikirimkan kepada mahasiswa. Tindakan ini tidak dapat dibatalkan.",
            onConfirm = { /* Handle approve logic */ showApproveDialog = false },
            onDismiss = { showApproveDialog = false },
            confirmButtonText = "Ya, Terima",
            icon = Icons.Default.Verified,
            iconColor = Color(0xFF16A34A)
        )
    }

    if (showRejectDialog) {
        ConfirmationDialog(
            title = "Tolak Pengajuan?",
            message = "Pastikan Anda telah memberikan catatan alasan penolakan agar mahasiswa dapat memperbaiki pengajuannya.",
            onConfirm = { /* Handle reject logic */ showRejectDialog = false },
            onDismiss = { showRejectDialog = false },
            confirmButtonText = "Tolak Pengajuan",
            icon = Icons.Default.Warning,
            iconColor = Color(0xFFDC2626)
        )
    }
}

@Composable
fun StudentProfileSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text("Budi Santoso", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("3202016024", color = Color.Gray, fontSize = 14.sp)
            Text("Kelas 3A - D3 Teknik Informatika", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun StatusIndicatorSection() {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color(0xFFFEF3C7), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.PendingActions, contentDescription = "Status", tint = Color(0xFFD97706))
            Spacer(Modifier.width(8.dp))
            Text("Status Saat Ini", fontWeight = FontWeight.SemiBold, color = Color(0xFFB45309))
        }
        Text(
            "Diajukan",
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB45309),
            modifier = Modifier
                .background(Color(0xFFFDE68A), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp
        )
    }
}

@Composable
fun ApplicationDetailsCard() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("INFORMASI SURAT", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tujuan Surat", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Text(
                    "Permohonan pembuatan Surat Keterangan Mahasiswa Aktif untuk keperluan pendaftaran magang mandiri di PT Telkom Indonesia Tbk.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text("Tanggal Pengajuan", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Tanggal", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("24 Oktober 2023", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

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
            Divider()
            FileItem("Bukti_Pembayaran.jpg", "2.4 MB • Image", Icons.Default.Image, Color.Blue)
        }
    }
}

@Composable
fun FileItem(name: String, details: String, icon: ImageVector, iconColor: Color) {
     Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(icon, contentDescription = name, tint = iconColor, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(details, fontSize = 12.sp, color = Color.Gray)
            }
        }
        IconButton(onClick = { /* Handle view file */ }) {
            Icon(Icons.Default.Visibility, contentDescription = "Lihat Dokumen", tint = MaterialTheme.colorScheme.primary)
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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onRejectClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                border = BorderStroke(2.dp, Color.Red)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Tolak")
                Spacer(Modifier.width(4.dp))
                Text("Tolak", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onApproveClick,
                modifier = Modifier.weight(2f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
            ) {
                Icon(Icons.Default.Check, contentDescription = "Terima")
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
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(32.dp))
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
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Batal")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun DetailPengajuanScreenPreview() {
    UASTheme {
    }
}
