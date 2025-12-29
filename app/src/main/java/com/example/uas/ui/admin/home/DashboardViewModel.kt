package com.example.uas.ui.admin.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.DashboardRepository
import com.example.uas.model.response.DashboardStatsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    object Idle : DashboardUiState()
    object Loading : DashboardUiState()
    data class Success(val stats: DashboardStatsResponse) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel(private val dashboardRepository: DashboardRepository) : ViewModel() {

    private val _dashboardState = MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)
    val dashboardState: StateFlow<DashboardUiState> = _dashboardState

    fun getDashboardStats() {
        viewModelScope.launch {
            _dashboardState.value = DashboardUiState.Loading
            try {
                val response = dashboardRepository.getDashboardStats()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _dashboardState.value = DashboardUiState.Success(it)
                    } ?: run {
                        _dashboardState.value = DashboardUiState.Error("Stats not found")
                    }
                } else {
                    _dashboardState.value = DashboardUiState.Error("Failed to fetch stats: ${response.message()}")
                }
            } catch (e: Exception) {
                _dashboardState.value = DashboardUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}
