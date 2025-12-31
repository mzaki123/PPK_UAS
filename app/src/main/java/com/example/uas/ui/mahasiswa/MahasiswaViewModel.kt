package com.example.uas.ui.mahasiswa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.MahasiswaRepository
import com.example.uas.model.Pengajuan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HistoryUiState {
    object Idle : HistoryUiState()
    object Loading : HistoryUiState()
    data class Success(val pengajuan: List<Pengajuan>) : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}

sealed class PengajuanDetailUiState {
    object Idle : PengajuanDetailUiState()
    object Loading : PengajuanDetailUiState()
    data class Success(val pengajuan: Pengajuan) : PengajuanDetailUiState()
    data class Error(val message: String) : PengajuanDetailUiState()
}

sealed class CreatePengajuanUiState {
    object Idle : CreatePengajuanUiState()
    object Loading : CreatePengajuanUiState()
    object Success : CreatePengajuanUiState()
    data class Error(val message: String) : CreatePengajuanUiState()
}

class MahasiswaViewModel(private val repository: MahasiswaRepository) : ViewModel() {

    private var _originalHistory = listOf<Pengajuan>()

    private val _historyState = MutableStateFlow<HistoryUiState>(HistoryUiState.Idle)
    val historyState: StateFlow<HistoryUiState> = _historyState

    private val _detailState = MutableStateFlow<PengajuanDetailUiState>(PengajuanDetailUiState.Idle)
    val detailState = _detailState.asStateFlow()

    private val _createState = MutableStateFlow<CreatePengajuanUiState>(CreatePengajuanUiState.Idle)
    val createState: StateFlow<CreatePengajuanUiState> = _createState

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
                _historyState.value = HistoryUiState.Error(e.message ?: "Error")
            }
        }
    }

    /**
     * MENCARI DETAIL DI MEMORI (Frontend Lookup)
     */
    fun getPengajuanById(id: Long) {
        _detailState.value = PengajuanDetailUiState.Loading
        val item = _originalHistory.find { it.id == id }
        if (item != null) {
            _detailState.value = PengajuanDetailUiState.Success(item)
        } else {
            _detailState.value = PengajuanDetailUiState.Error("Data pengajuan tidak ditemukan di lokal.")
        }
    }

    fun createPengajuan(tujuan: String) {
        viewModelScope.launch {
            _createState.value = CreatePengajuanUiState.Loading
            try {
                val response = repository.createPengajuan(tujuan)
                if (response.isSuccessful) {
                    _createState.value = CreatePengajuanUiState.Success
                    getMyPengajuan() // Refresh history
                } else {
                    _createState.value = CreatePengajuanUiState.Error("Gagal membuat pengajuan.")
                }
            } catch (e: Exception) {
                _createState.value = CreatePengajuanUiState.Error(e.message ?: "Error")
            }
        }
    }

    fun resetCreateState() {
        _createState.value = CreatePengajuanUiState.Idle
    }
}