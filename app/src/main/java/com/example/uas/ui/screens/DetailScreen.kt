package com.example.uas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uas.ui.theme.UASTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pengajuan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Download Surat")
                }
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Kembali ke Beranda")
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
            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF66BB6A).copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Status Icon",
                        tint = Color(0xFF66BB6A),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Status Saat Ini", fontSize = 12.sp, color = Color.Gray)
                        Text(text = "DITERIMA", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF66BB6A))
                    }
                }
            }

            // Informasi Surat Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "Info Icon")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Informasi Surat", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Divider()
                    InfoRow(label = "ID Pengajuan", value = "#REQ-2023-8821")
                    InfoRow(label = "Tanggal Pengajuan", value = "24 Oktober 2023")
                    InfoRow(label = "Tujuan Surat", value = "Permohonan pembuatan Surat Keterangan Mahasiswa Aktif...")
                }
            }

            // Dokumen Pendukung
            Text(text = "Dokumen Pendukung", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            DocumentItem(fileName = "KTP_Mahasiswa_Scan.pdf", fileSize = "2.4 MB")

            // Surat Diterbitkan
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF66BB6A).copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Pengajuan Disetujui", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF66BB6A))
                    Text(text = "Surat Keterangan telah diterbitkan dan siap diunduh.", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoRow(label = "Nomor Surat", value = "001/SKM/X/2023")
                    Divider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF Icon", tint = Color.Red)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "SKM_001_Final.pdf", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Signed Digital", fontSize = 10.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.Default.DownloadDone, contentDescription = "Downloaded", tint = Color(0xFF66BB6A))
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun DocumentItem(fileName: String, fileSize: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Attachment, contentDescription = "Attachment Icon")
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = fileName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(text = fileSize, fontSize = 12.sp, color = Color.Gray)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Visibility, contentDescription = "View Document")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    UASTheme {
        DetailScreen(rememberNavController())
    }
}
