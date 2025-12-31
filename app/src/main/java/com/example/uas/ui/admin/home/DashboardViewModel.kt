package com.example.uas.ui.admin.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.DashboardRepository
import com.example.uas.model.Pengajuan
import com.example.uas.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Kita buat model lokal saja untuk menampung angka statistik
data class LocalDashboardStats(
    val totalUsers: Int = 0,
    val mahasiswa: Int = 0,
    val kemahasiswaan: Int = 0,
    val totalPengajuan: Int = 0
)

sealed class DataListUiState<out T> {
    object Loading : DataListUiState<Nothing>()
    data class Success<T>(val data: List<T>) : DataListUiState<T>()
    data class Error(val message: String) : DataListUiState<Nothing>()
}

class DashboardViewModel(private val dashboardRepository: DashboardRepository) : ViewModel() {

    private val _usersState = MutableStateFlow<DataListUiState<User>>(DataListUiState.Loading)
    val usersState: StateFlow<DataListUiState<User>> = _usersState.asStateFlow()

    private val _pengajuanState = MutableStateFlow<DataListUiState<Pengajuan>>(DataListUiState.Loading)
    val pengajuanState: StateFlow<DataListUiState<Pengajuan>> = _pengajuanState.asStateFlow()

    // State untuk statistik yang dihitung manual
    var calculatedStats by mutableStateOf(LocalDashboardStats())

    fun fetchData() {
        getRecentUsers()
        getRecentPengajuan()
    }

    private fun getRecentUsers() {
        viewModelScope.launch {
            _usersState.value = DataListUiState.Loading
            try {
                val response = dashboardRepository.getAllUsers()
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    _usersState.value = DataListUiState.Success(users)

                    // HITUNG STATISTIK USER DI SINI LUR
                    updateUserStats(users)
                }
            } catch (e: Exception) {
                _usersState.value = DataListUiState.Error(e.message ?: "Error")
            }
        }
    }

    private fun getRecentPengajuan() {
        viewModelScope.launch {
            _pengajuanState.value = DataListUiState.Loading
            try {
                val response = dashboardRepository.getAllPengajuan()
                if (response.isSuccessful) {
                    val pengajuans = response.body() ?: emptyList()
                    _pengajuanState.value = DataListUiState.Success(pengajuans)

                    // HITUNG STATISTIK PENGAJUAN DI SINI LUR
                    calculatedStats = calculatedStats.copy(totalPengajuan = pengajuans.size)
                }
            } catch (e: Exception) {
                _pengajuanState.value = DataListUiState.Error(e.message ?: "Error")
            }
        }
    }

    private fun updateUserStats(users: List<User>) {
        val total = users.size
        val mhsCount = users.count { it.role.uppercase() == "MAHASISWA" }
        val kmhCount = users.count { it.role.uppercase() == "KEMAHASISWAAN" }

        calculatedStats = calculatedStats.copy(
            totalUsers = total,
            mahasiswa = mhsCount,
            kemahasiswaan = kmhCount
        )
    }
}