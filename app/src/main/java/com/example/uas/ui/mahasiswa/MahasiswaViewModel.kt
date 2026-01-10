package com.example.uas.ui.mahasiswa

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.MahasiswaRepository
import com.example.uas.model.MahasiswaDto
import com.example.uas.model.Pengajuan
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

sealed class HistoryUiState {
    object Idle : HistoryUiState()
    object Loading : HistoryUiState()
    data class Success(val pengajuan: List<Pengajuan>) : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}
sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val profile: MahasiswaDto) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
sealed class CreatePengajuanUiState {
    object Idle : CreatePengajuanUiState()
    object Loading : CreatePengajuanUiState()
    object Success : CreatePengajuanUiState()
    data class Error(val message: String) : CreatePengajuanUiState()
}
sealed class PengajuanDetailUiState {
    object Idle : PengajuanDetailUiState()
    object Loading : PengajuanDetailUiState()
    data class Success(val pengajuan: Pengajuan) : PengajuanDetailUiState()
    data class Error(val message: String) : PengajuanDetailUiState()
}

class MahasiswaViewModel(private val repository: MahasiswaRepository) : ViewModel() {

    private var _originalHistory = listOf<Pengajuan>()
    private val _historyState = MutableStateFlow<HistoryUiState>(HistoryUiState.Idle)
    val historyState = _historyState.asStateFlow()

    private val _profileState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileState = _profileState.asStateFlow()

    private val _createState = MutableStateFlow<CreatePengajuanUiState>(CreatePengajuanUiState.Idle)
    val createState = _createState.asStateFlow()

    private val _selectedFileUri = MutableStateFlow<Uri?>(null)
    val selectedFileUri = _selectedFileUri.asStateFlow()

    private val _detailState = MutableStateFlow<PengajuanDetailUiState>(PengajuanDetailUiState.Idle)
    val detailState = _detailState.asStateFlow()

    init { fetchData() }

    fun fetchData() {
        getMyPengajuan()
        getMyProfile()
    }

    fun onFileSelected(uri: Uri?) { _selectedFileUri.value = uri }

    fun getMyPengajuan() {
        viewModelScope.launch {
            _historyState.value = HistoryUiState.Loading
            try {
                val response = repository.getMyPengajuan()
                if (response.isSuccessful) {
                    _originalHistory = response.body() ?: emptyList()
                    _historyState.value = HistoryUiState.Success(_originalHistory)
                } else {
                    _historyState.value = HistoryUiState.Error("Gagal memuat riwayat.")
                }
            } catch (e: Exception) {
                _historyState.value = HistoryUiState.Error(e.message ?: "Error Jaringan")
            }
        }
    }

    /**
     * Validasi: Cek apakah Nama dan Kelas sudah terisi di profil.
     */
    fun isProfileReady(): Boolean {
        val state = _profileState.value
        return if (state is ProfileUiState.Success) {
            // Pastikan nama dan kelas tidak null dan tidak kosong
            !state.profile.nama.isNullOrBlank() && !state.profile.kelas.isNullOrBlank()
        } else {
            false
        }
    }

    fun createPengajuan(context: Context, tujuan: String) {
        // Validasi profil sebelum proses upload lur
        if (!isProfileReady()) {
            _createState.value = CreatePengajuanUiState.Error("Lengkapi profil (Nama & Kelas) dulu lur!")
            return
        }

        val uri = _selectedFileUri.value
        if (uri == null) {
            _createState.value = CreatePengajuanUiState.Error("Pilih file PDF lampiran dulu lur!")
            return
        }

        viewModelScope.launch {
            _createState.value = CreatePengajuanUiState.Loading
            try {
                val tujuanBody = tujuan.toRequestBody("text/plain".toMediaTypeOrNull())
                val file = uriToFile(context, uri)
                val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val response = repository.createPengajuanWithFile(tujuanBody, filePart)

                if (response.isSuccessful) {
                    _createState.value = CreatePengajuanUiState.Success
                    getMyPengajuan()
                } else {
                    _createState.value = CreatePengajuanUiState.Error("Gagal kirim: ${response.message()}")
                }
            } catch (e: Exception) {
                _createState.value = CreatePengajuanUiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_upload_${System.currentTimeMillis()}.pdf")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    fun getPengajuanById(id: Long) {
        val item = _originalHistory.find { it.id == id }
        _detailState.value = if (item != null) PengajuanDetailUiState.Success(item)
        else PengajuanDetailUiState.Error("Data tidak ditemukan")
    }

    fun resetCreateState() { _createState.value = CreatePengajuanUiState.Idle }

    private fun getMyProfile() {
        viewModelScope.launch {
            try {
                val response = repository.getMyProfile()
                if (response.isSuccessful) {
                    _profileState.value = ProfileUiState.Success(response.body()!!)
                }
            } catch (e: Exception) {
                _profileState.value = ProfileUiState.Error(e.message ?: "Gagal muat profil")
            }
        }
    }
}