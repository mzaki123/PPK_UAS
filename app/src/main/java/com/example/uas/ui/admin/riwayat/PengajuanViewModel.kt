package com.example.uas.ui.admin.riwayat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.PengajuanRepository
import com.example.uas.model.Pengajuan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PengajuanUiState {
    object Idle : PengajuanUiState()
    object Loading : PengajuanUiState()
    data class Success(val pengajuan: List<Pengajuan>) : PengajuanUiState()
    data class Error(val message: String) : PengajuanUiState()
}

class PengajuanViewModel(private val pengajuanRepository: PengajuanRepository) : ViewModel() {

    private val _pengajuanState = MutableStateFlow<PengajuanUiState>(PengajuanUiState.Idle)
    val pengajuanState: StateFlow<PengajuanUiState> = _pengajuanState

    fun getAllPengajuan() {
        viewModelScope.launch {
            _pengajuanState.value = PengajuanUiState.Loading
            try {
                // Panggilan ini sudah sinkron dengan repository di Canvas (tanpa token manual)
                val response = pengajuanRepository.getAllPengajuan()
                if (response.isSuccessful) {
                    _pengajuanState.value = PengajuanUiState.Success(response.body() ?: emptyList())
                } else {
                    _pengajuanState.value = PengajuanUiState.Error("Gagal mengambil data: ${response.code()}")
                }
            } catch (e: Exception) {
                _pengajuanState.value = PengajuanUiState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    // Fungsi tambahan untuk update status (Setuju/Tolak) jika nanti dibutuhkan di UI
    fun updateStatus(id: Long, status: String) {
        viewModelScope.launch {
            try {
                val response = pengajuanRepository.updateStatus(id, status)
                if (response.isSuccessful) {
                    // Refresh data setelah berhasil update
                    getAllPengajuan()
                }
            } catch (e: Exception) {
                // Handle error update
            }
        }
    }
}