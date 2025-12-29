package com.example.uas.ui.kemahasiswaan.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailPengajuanViewModel : ViewModel() {

    // private val _pengajuanDetail = MutableStateFlow<PengajuanDetail?>(null)
    // val pengajuanDetail = _pengajuanDetail.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes = _notes.asStateFlow()

    fun onNotesChange(newNotes: String) {
        _notes.value = newNotes
    }

    fun loadPengajuanDetail(id: Int) {
        viewModelScope.launch {
            // TODO: Fetch detail from repository
        }
    }

    fun approvePengajuan() {
        viewModelScope.launch {
            // TODO: Call repository to approve
        }
    }

    fun rejectPengajuan() {
        viewModelScope.launch {
            // TODO: Call repository to reject with notes
        }
    }
}
