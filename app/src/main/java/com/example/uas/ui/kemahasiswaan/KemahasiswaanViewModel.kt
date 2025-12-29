package com.example.uas.ui.kemahasiswaan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.KemahasiswaanRepository
import com.example.uas.model.Pengajuan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PengajuanListUiState {
    object Idle : PengajuanListUiState()
    object Loading : PengajuanListUiState()
    data class Success(val pengajuan: List<Pengajuan>) : PengajuanListUiState()
    data class Error(val message: String) : PengajuanListUiState()
}

sealed class PengajuanDetailUiState {
    object Idle : PengajuanDetailUiState()
    object Loading : PengajuanDetailUiState()
    data class Success(val pengajuan: Pengajuan) : PengajuanDetailUiState()
    data class Error(val message: String) : PengajuanDetailUiState()
}

sealed class UpdateStatusUiState {
    object Idle : UpdateStatusUiState()
    object Loading : UpdateStatusUiState()
    data class Success(val pengajuan: Pengajuan) : UpdateStatusUiState()
    data class Error(val message: String) : UpdateStatusUiState()
}

class KemahasiswaanViewModel(private val repository: KemahasiswaanRepository) : ViewModel() {

    private val _pengajuanListState = MutableStateFlow<PengajuanListUiState>(PengajuanListUiState.Idle)
    val pengajuanListState: StateFlow<PengajuanListUiState> = _pengajuanListState

    private val _pengajuanDetailState = MutableStateFlow<PengajuanDetailUiState>(PengajuanDetailUiState.Idle)
    val pengajuanDetailState: StateFlow<PengajuanDetailUiState> = _pengajuanDetailState

    private val _updateStatusState = MutableStateFlow<UpdateStatusUiState>(UpdateStatusUiState.Idle)
    val updateStatusState: StateFlow<UpdateStatusUiState> = _updateStatusState

    fun getAllPengajuan() {
        viewModelScope.launch {
            _pengajuanListState.value = PengajuanListUiState.Loading
            try {
                val response = repository.getAllPengajuan()
                if (response.isSuccessful) {
                    _pengajuanListState.value = PengajuanListUiState.Success(response.body() ?: emptyList())
                } else {
                    _pengajuanListState.value = PengajuanListUiState.Error("Failed to fetch data: ${response.message()}")
                }
            } catch (e: Exception) {
                _pengajuanListState.value = PengajuanListUiState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun getPengajuanById(id: Long) {
        viewModelScope.launch {
            _pengajuanDetailState.value = PengajuanDetailUiState.Loading
            try {
                val response = repository.getPengajuanById(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _pengajuanDetailState.value = PengajuanDetailUiState.Success(it)
                    } ?: run {
                        _pengajuanDetailState.value = PengajuanDetailUiState.Error("Pengajuan not found")
                    }
                } else {
                    _pengajuanDetailState.value = PengajuanDetailUiState.Error("Failed to fetch details: ${response.message()}")
                }
            } catch (e: Exception) {
                _pengajuanDetailState.value = PengajuanDetailUiState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun updatePengajuanStatus(id: Long, status: String) {
        viewModelScope.launch {
            _updateStatusState.value = UpdateStatusUiState.Loading
            try {
                val response = repository.updatePengajuanStatus(id, status)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _updateStatusState.value = UpdateStatusUiState.Success(it)
                        // Refresh detail view
                        getPengajuanById(id)
                    } ?: run {
                         _updateStatusState.value = UpdateStatusUiState.Error("Update failed, response body was null")
                    }
                } else {
                    _updateStatusState.value = UpdateStatusUiState.Error("Failed to update status: ${response.message()}")
                }
            } catch (e: Exception) {
                _updateStatusState.value = UpdateStatusUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}
