package com.example.uas.ui.kemahasiswaan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfilKemahasiswaanViewModel : ViewModel() {

    // private val _userProfile = MutableStateFlow<UserProfile?>(null)
    // val userProfile = _userProfile.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            // TODO: Fetch user profile data
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            // TODO: Clear user session and navigate to login
        }
    }
}
