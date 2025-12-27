package com.example.uas.ui.kemahasiswaan

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DaftarPengajuanViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("Semua")
    val selectedFilter = _selectedFilter.asStateFlow()

    // TODO: Fetch pengajuan list from repository
    private val _pengajuanList = MutableStateFlow(dummyPengajuanList)
    val pengajuanList = _pengajuanList.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChange(filter: String) {
        _selectedFilter.value = filter
    }
}
