package com.example.uas.ui.kemahasiswaan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.KemahasiswaanRepository
import com.example.uas.model.Pengajuan
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// State untuk daftar pengajuan
sealed class PengajuanListUiState {
    object Idle : PengajuanListUiState()
    object Loading : PengajuanListUiState()
    data class Success(val pengajuan: List<Pengajuan>) : PengajuanListUiState()
    data class Error(val message: String) : PengajuanListUiState()
}

// State untuk detail pengajuan (Menggunakan pencarian lokal)
sealed class PengajuanDetailUiState {
    object Idle : PengajuanDetailUiState()
    object Loading : PengajuanDetailUiState()
    data class Success(val pengajuan: Pengajuan) : PengajuanDetailUiState()
    data class Error(val message: String) : PengajuanDetailUiState()
}

// State untuk proses pembaruan status (ACC/Tolak)
sealed class UpdateStatusUiState {
    object Idle : UpdateStatusUiState()
    object Loading : UpdateStatusUiState()
    data class Success(val pengajuan: Pengajuan) : UpdateStatusUiState()
    data class Error(val message: String) : UpdateStatusUiState()
}

class KemahasiswaanViewModel(private val repository: KemahasiswaanRepository) : ViewModel() {

    // Menyimpan data asli dari server agar tidak hilang saat difilter
    private var _originalList = listOf<Pengajuan>()

    // State dasar untuk menyimpan list pengajuan mentah
    private val _pengajuanListState = MutableStateFlow<PengajuanListUiState>(PengajuanListUiState.Idle)
    val pengajuanListState = _pengajuanListState.asStateFlow()

    // State untuk detail dan pembaruan status
    private val _pengajuanDetailState = MutableStateFlow<PengajuanDetailUiState>(PengajuanDetailUiState.Idle)
    val pengajuanDetailState = _pengajuanDetailState.asStateFlow()

    private val _updateStatusState = MutableStateFlow<UpdateStatusUiState>(UpdateStatusUiState.Idle)
    val updateStatusState = _updateStatusState.asStateFlow()

    // --- STATE CATATAN (NOTES) ---
    // Tambahkan ini lur agar DetailPengajuanScreen tidak error
    private val _notes = MutableStateFlow("")
    val notes = _notes.asStateFlow()

    // State untuk parameter pencarian dan filter status
    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow("Semua")

    // State gabungan yang diamati oleh UI DaftarPengajuanScreen
    private val _filteredListState = MutableStateFlow<PengajuanListUiState>(PengajuanListUiState.Idle)
    val filteredListState = _filteredListState.asStateFlow()

    init {
        // Logika Reaktif: Otomatis memfilter data setiap kali ada perubahan pada query atau filter
        combine(_searchQuery, _selectedFilter) { query, filter ->
            if (_originalList.isNotEmpty()) {
                applyFilter(query, filter)
            }
        }.launchIn(viewModelScope)
    }

    // Fungsi untuk mengubah query pencarian
    fun onSearchChange(query: String) {
        _searchQuery.value = query
    }

    // Fungsi untuk mengubah filter status
    fun onFilterChange(filter: String) {
        _selectedFilter.value = filter
    }

    // Fungsi untuk mengubah catatan verifikasi
    fun onNotesChange(newNotes: String) {
        _notes.value = newNotes
    }

    /**
     * Mengambil semua data pengajuan dari API
     */
    fun getAllPengajuan() {
        _filteredListState.value = PengajuanListUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getAllPengajuan()
                if (response.isSuccessful) {
                    _originalList = response.body() ?: emptyList()
                    _pengajuanListState.value = PengajuanListUiState.Success(_originalList)
                    // Terapkan filter pertama kali setelah data berhasil diambil
                    applyFilter(_searchQuery.value, _selectedFilter.value)
                } else {
                    _filteredListState.value = PengajuanListUiState.Error("Gagal mengambil data: ${response.code()}")
                }
            } catch (e: Exception) {
                _filteredListState.value = PengajuanListUiState.Error(e.message ?: "Kesalahan koneksi")
            }
        }
    }

    /**
     * Fungsi pembantu untuk menyaring data berdasarkan query dan status
     */
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

    /**
     * MENCARI DETAIL DI MEMORI LOKAL
     * Karena backend tidak menyediakan API detail per ID, kita cari di list yang sudah ada.
     */
    fun getPengajuanById(id: Long) {
        _pengajuanDetailState.value = PengajuanDetailUiState.Loading
        val detail = _originalList.find { it.id == id }

        if (detail != null) {
            _pengajuanDetailState.value = PengajuanDetailUiState.Success(detail)
            // Reset catatan saat membuka detail baru
            _notes.value = ""
        } else {
            _pengajuanDetailState.value = PengajuanDetailUiState.Error("Data pengajuan tidak ditemukan di memori lokal.")
        }
    }

    /**
     * Mengirim permintaan pembaruan status ke API (SELESAI atau DITOLAK)
     */
    fun updatePengajuanStatus(id: Long, status: String) {
        viewModelScope.launch {
            _updateStatusState.value = UpdateStatusUiState.Loading
            try {
                // Di sini kamu bisa mengirim 'notes.value' jika API backend kamu mendukung parameter catatan
                val response = repository.updatePengajuanStatus(id, status)
                if (response.isSuccessful) {
                    _updateStatusState.value = UpdateStatusUiState.Success(response.body()!!)
                    // Refresh data list agar status terbaru langsung terlihat di daftar
                    getAllPengajuan()
                } else {
                    _updateStatusState.value = UpdateStatusUiState.Error("Gagal memperbarui status")
                }
            } catch (e: Exception) {
                _updateStatusState.value = UpdateStatusUiState.Error(e.message ?: "Kesalahan sistem")
            }
        }
    }

    /**
     * Reset state pembaruan status agar dialog konfirmasi tidak muncul kembali
     */
    fun resetUpdateState() {
        _updateStatusState.value = UpdateStatusUiState.Idle
    }
}