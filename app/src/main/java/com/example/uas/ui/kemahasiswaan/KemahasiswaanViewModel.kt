package com.example.uas.ui.kemahasiswaan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.KemahasiswaanRepository
import com.example.uas.model.KemahasiswaanDto
import com.example.uas.model.Pengajuan
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// --- UI STATES ---
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

sealed class ProfileKemahasiswaanUiState {
    object Loading : ProfileKemahasiswaanUiState()
    data class Success(val profile: KemahasiswaanDto) : ProfileKemahasiswaanUiState()
    data class Error(val message: String) : ProfileKemahasiswaanUiState()
}

class KemahasiswaanViewModel(private val repository: KemahasiswaanRepository) : ViewModel() {

    // --- 1. DEKLARASI VARIABEL (WAJIB DI ATAS BLOK INIT LUR!) ---

    private var _originalList = listOf<Pengajuan>()

    private val _profileState = MutableStateFlow<ProfileKemahasiswaanUiState>(ProfileKemahasiswaanUiState.Loading)
    val profileState = _profileState.asStateFlow()

    private val _pengajuanListState = MutableStateFlow<PengajuanListUiState>(PengajuanListUiState.Idle)
    val pengajuanListState = _pengajuanListState.asStateFlow()

    private val _pengajuanDetailState = MutableStateFlow<PengajuanDetailUiState>(PengajuanDetailUiState.Idle)
    val pengajuanDetailState = _pengajuanDetailState.asStateFlow()

    private val _updateStatusState = MutableStateFlow<UpdateStatusUiState>(UpdateStatusUiState.Idle)
    val updateStatusState = _updateStatusState.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes = _notes.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow("Semua")

    private val _filteredListState = MutableStateFlow<PengajuanListUiState>(PengajuanListUiState.Idle)
    val filteredListState = _filteredListState.asStateFlow()

    // --- 2. BLOK INIT (SETELAH SEMUA GELAS DISIAPKAN) ---
    init {
        // Setup Filter Reaktif
        combine(_searchQuery, _selectedFilter) { query, filter ->
            if (_originalList.isNotEmpty()) {
                applyFilter(query, filter)
            }
        }.launchIn(viewModelScope)

        // Tarik data awal
        fetchAllData()
    }

    fun fetchAllData() {
        getKemahasiswaanProfile()
        getAllPengajuan()
    }

    // --- 3. FUNGSI LOGIKA BISA DI TARUH DI BAWAH ---

    fun onSearchChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChange(filter: String) {
        _selectedFilter.value = filter
    }

    fun onNotesChange(newNotes: String) {
        _notes.value = newNotes
    }

    fun getKemahasiswaanProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileKemahasiswaanUiState.Loading
            try {
                val response = repository.getKemahasiswaanProfile()
                if (response.isSuccessful && response.body() != null) {
                    _profileState.value = ProfileKemahasiswaanUiState.Success(response.body()!!)
                } else {
                    _profileState.value = ProfileKemahasiswaanUiState.Error("Gagal memuat profil")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileKemahasiswaanUiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun getAllPengajuan() {
        _filteredListState.value = PengajuanListUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getAllPengajuan()
                if (response.isSuccessful) {
                    _originalList = response.body() ?: emptyList()
                    _pengajuanListState.value = PengajuanListUiState.Success(_originalList)
                    applyFilter(_searchQuery.value, _selectedFilter.value)
                } else {
                    _filteredListState.value = PengajuanListUiState.Error("Gagal mengambil data: ${response.code()}")
                }
            } catch (e: Exception) {
                _filteredListState.value = PengajuanListUiState.Error(e.message ?: "Kesalahan koneksi")
            }
        }
    }

    private fun applyFilter(query: String, filter: String) {
        val filtered = _originalList.filter { pengajuan ->
            val matchesSearch = pengajuan.mahasiswaNama.contains(query, ignoreCase = true) ||
                    pengajuan.mahasiswaNim.contains(query, ignoreCase = true)
            val matchesFilter = if (filter == "Semua") true
            else pengajuan.status.equals(filter, ignoreCase = true)
            matchesSearch && matchesFilter
        }
        _filteredListState.value = PengajuanListUiState.Success(filtered)
    }

    fun getPengajuanById(id: Long) {
        _pengajuanDetailState.value = PengajuanDetailUiState.Loading
        val detail = _originalList.find { it.id == id }

        if (detail != null) {
            _pengajuanDetailState.value = PengajuanDetailUiState.Success(detail)
            _notes.value = ""
        } else {
            _pengajuanDetailState.value = PengajuanDetailUiState.Error("Data tidak ditemukan di memori lokal.")
        }
    }

    fun updatePengajuanStatus(id: Long, status: String) {
        viewModelScope.launch {
            _updateStatusState.value = UpdateStatusUiState.Loading
            try {
                val response = repository.updatePengajuanStatus(id, status)
                if (response.isSuccessful) {
                    _updateStatusState.value = UpdateStatusUiState.Success(response.body()!!)
                    getAllPengajuan() // Refresh list
                } else {
                    _updateStatusState.value = UpdateStatusUiState.Error("Gagal memperbarui status")
                }
            } catch (e: Exception) {
                _updateStatusState.value = UpdateStatusUiState.Error(e.message ?: "Kesalahan sistem")
            }
        }
    }

    fun resetUpdateState() {
        _updateStatusState.value = UpdateStatusUiState.Idle
    }
}