package com.example.uas.ui.mahasiswa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.MahasiswaRepository
import com.example.uas.model.Pengajuan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HistoryUiState {
    object Idle : HistoryUiState()
    object Loading : HistoryUiState()
    data class Success(val pengajuan: List<Pengajuan>) : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}

sealed class CreatePengajuanUiState {
    object Idle : CreatePengajuanUiState()
    object Loading : CreatePengajuanUiState()
    object Success : CreatePengajuanUiState()
    data class Error(val message: String) : CreatePengajuanUiState()
}

class MahasiswaViewModel(private val repository: MahasiswaRepository) : ViewModel() {

    private val _historyState = MutableStateFlow<HistoryUiState>(HistoryUiState.Idle)
    val historyState: StateFlow<HistoryUiState> = _historyState

    private val _createState = MutableStateFlow<CreatePengajuanUiState>(CreatePengajuanUiState.Idle)
    val createState: StateFlow<CreatePengajuanUiState> = _createState

    fun getMyPengajuan() {
        viewModelScope.launch {
            _historyState.value = HistoryUiState.Loading
            try {
                val response = repository.getMyPengajuan()
                if (response.isSuccessful) {
                    _historyState.value = HistoryUiState.Success(response.body() ?: emptyList())
                } else {
                    _historyState.value = HistoryUiState.Error("Failed to fetch history: ${response.message()}")
                }
            } catch (e: Exception) {
                _historyState.value = HistoryUiState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun createPengajuan(tujuan: String) {
        viewModelScope.launch {
            _createState.value = CreatePengajuanUiState.Loading
            try {
                val response = repository.createPengajuan(tujuan)
                if (response.isSuccessful) {
                    _createState.value = CreatePengajuanUiState.Success
                } else {
                    _createState.value = CreatePengajuanUiState.Error("Failed to create pengajuan: ${response.message()}")
                }
            } catch (e: Exception) {
                _createState.value = CreatePengajuanUiState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun resetCreateState() {
        _createState.value = CreatePengajuanUiState.Idle
    }
}
