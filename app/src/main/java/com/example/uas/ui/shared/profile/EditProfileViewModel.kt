package com.example.uas.ui.shared.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.ProfileRepository
import com.example.uas.model.MahasiswaDto
import com.example.uas.model.KemahasiswaanDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State untuk proses update (Simpan)
sealed class EditProfileUiState {
    object Idle : EditProfileUiState()
    object Loading : EditProfileUiState()
    object Success : EditProfileUiState()
    data class Error(val message: String) : EditProfileUiState()
}

// State untuk memuat data awal (Fetch)
sealed class FetchProfileUiState {
    object Loading : FetchProfileUiState()
    data class Success(val data: Any) : FetchProfileUiState() // 'Any' agar fleksibel
    data class Error(val message: String) : FetchProfileUiState()
}

// SATU-SATUNYA ViewModel untuk semua fitur profil
class EditProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    // State data user (Bisa MahasiswaDto atau KemahasiswaanDto)
    private val _userState = MutableStateFlow<FetchProfileUiState>(FetchProfileUiState.Loading)
    val userState = _userState.asStateFlow()

    // State untuk tombol simpan
    private val _updateState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Idle)
    val updateState = _updateState.asStateFlow()

    init {
        fetchProfile()
    }

    fun isIdentifierEditable(data: Any?): Boolean {
        return when (data) {
            is MahasiswaDto -> data.nim.isNullOrBlank() // Bisa diedit jika NIM null atau kosong
            is KemahasiswaanDto -> data.nip.isNullOrBlank() // Bisa diedit jika NIP null atau kosong
            else -> false // Jika data user tidak ada, tidak bisa diedit
        }
    }


    fun fetchProfile() {
        _userState.value = FetchProfileUiState.Loading
        viewModelScope.launch {
            try {
                val data = repository.getProfileByRole()
                if (data != null) {
                    _userState.value = FetchProfileUiState.Success(data)
                } else {
                    _userState.value = FetchProfileUiState.Error("Data profil tidak ditemukan lur.")
                }
            } catch (e: Exception) {
                _userState.value = FetchProfileUiState.Error(e.message ?: "Gagal memuat profil")
            }
        }
    }

    fun saveChanges(updatedData: Any) {
        _updateState.value = EditProfileUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.updateProfileByRole(updatedData)
                if (response.isSuccessful) {
                    _updateState.value = EditProfileUiState.Success
                    // Refresh data setelah berhasil update
                    fetchProfile()
                } else {
                    val errMsg = when(response.code()) {
                        400 -> "Data yang kamu masukkan tidak valid ."
                        403 -> "Kamu tidak punya akses untuk ubah data ini."
                        else -> "Gagal menyimpan perubahan."
                    }
                    _updateState.value = EditProfileUiState.Error(errMsg)
                }
            } catch (e: Exception) {
                _updateState.value = EditProfileUiState.Error(e.message ?: "Terjadi kesalahan koneksi")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = EditProfileUiState.Idle
    }
}
