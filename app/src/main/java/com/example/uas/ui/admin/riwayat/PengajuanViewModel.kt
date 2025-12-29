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
                val response = pengajuanRepository.getAllPengajuan()
                if (response.isSuccessful) {
                    _pengajuanState.value = PengajuanUiState.Success(response.body() ?: emptyList())
                } else {
                    _pengajuanState.value = PengajuanUiState.Error("Failed to fetch pengajuan: ${response.message()}")
                }
            } catch (e: Exception) {
                _pengajuanState.value = PengajuanUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}
