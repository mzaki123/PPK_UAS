package com.example.uas.ui.mahasiswa.form

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uas.ui.mahasiswa.CreatePengajuanUiState
import com.example.uas.ui.mahasiswa.MahasiswaViewModel
import com.example.uas.ui.mahasiswa.ProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPengajuanScreen(
    navController: NavController,
    viewModel: MahasiswaViewModel
) {
    var tujuan by remember { mutableStateOf("") }
    val createState by viewModel.createState.collectAsState()
    val profileState by viewModel.profileState.collectAsState()
    val selectedFileUri by viewModel.selectedFileUri.collectAsState()

    val context = LocalContext.current

    // Cek apakah profil sudah siap lur
    val isProfileReady = viewModel.isProfileReady()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onFileSelected(uri)
    }

    LaunchedEffect(createState) {
        if (createState is CreatePengajuanUiState.Success) {
            viewModel.resetCreateState()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Form Pengajuan Surat", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = { viewModel.createPengajuan(context, tujuan) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF026AA1)),
                        // VALIDASI: Tombol hanya aktif jika profil LENGKAP, tujuan TERISI, dan file DIPILIH
                        enabled = isProfileReady && tujuan.isNotBlank() && selectedFileUri != null && createState !is CreatePengajuanUiState.Loading
                    ) {
                        if (createState is CreatePengajuanUiState.Loading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text(text = "Ajukan Permohonan", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Batal", color = Color.Gray)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Peringatan Profil Belum Lengkap lur
            if (!isProfileReady && profileState !is ProfileUiState.Loading) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Peringatan", tint = Color.Red)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Profil Belum Lengkap!", fontWeight = FontWeight.Bold, color = Color.Red, fontSize = 14.sp)
                            Text("Harap lengkapi Nama dan Kelas di menu Profil Anda sebelum mengajukan surat.", color = Color.Red, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }

            // Card Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = Color(0xFF026AA1), modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Lengkapi detail tujuan dan lampirkan dokumen pendukung ", fontSize = 13.sp, color = Color(0xFF026AA1))
                }
            }

            // Input Tujuan
            Column {
                Text("Tujuan Pengajuan", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF314158))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = tujuan,
                    onValueChange = { tujuan = it },
                    placeholder = { Text("Jelaskan keperluan Anda...") },
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = isProfileReady, // Disable input jika profil belum siap
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color(0xFFF1F5F9)
                    )
                )
            }

            // Upload Area
            Column {
                Text("File Pendukung (Wajib PDF)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF314158))
                Spacer(modifier = Modifier.height(8.dp))
                DashedUploadBox(
                    selectedFileUri = selectedFileUri,
                    onClick = {
                        if (isProfileReady) filePickerLauncher.launch("application/pdf")
                    },
                    onClear = { viewModel.onFileSelected(null) }
                )
            }

            if (createState is CreatePengajuanUiState.Error) {
                Text(
                    text = (createState as CreatePengajuanUiState.Error).message,
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun DashedUploadBox(
    selectedFileUri: Uri?,
    onClick: () -> Unit,
    onClear: () -> Unit
) {
    val context = LocalContext.current
    var fileName by remember { mutableStateOf<String?>(null) }
    var fileSize by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedFileUri) {
        selectedFileUri?.let { uri ->
            try {
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (cursor.moveToFirst()) {
                        fileName = cursor.getString(nameIndex)
                        val size = cursor.getLong(sizeIndex)
                        fileSize = android.text.format.Formatter.formatShortFileSize(context, size)
                    }
                }
            } catch (e: Exception) {
                fileName = "Unknown File"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .dashedBorder(1.dp, Color(0xFF94A3B8), 12.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (selectedFileUri == null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CloudUpload, "Upload", tint = Color(0xFF94A3B8), modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tap untuk pilih dokumen PDF", color = Color(0xFF94A3B8), fontSize = 12.sp)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.PictureAsPdf, null, tint = Color.Red, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(fileName ?: "File.pdf", fontWeight = FontWeight.Bold, maxLines = 1, fontSize = 14.sp)
                        Text(fileSize ?: "", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, "Hapus file", tint = Color.Gray)
                }
            }
        }
    }
}

fun Modifier.dashedBorder(width: Dp, color: Color, cornerRadius: Dp) = this.drawBehind {
    val stroke = Stroke(
        width = width.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
    )
    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius.toPx())
    )
}