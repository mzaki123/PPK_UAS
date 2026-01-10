package com.example.uas.ui.admin.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.UserRepository
import com.example.uas.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.squareup.moshi.JsonDataException

sealed class UserUiState {
    object Idle : UserUiState()
    object Loading : UserUiState()
    data class Success(val users: List<User>) : UserUiState()
    data class Error(val message: String) : UserUiState()
}

sealed class UserDetailUiState {
    object Idle : UserDetailUiState()
    object Loading : UserDetailUiState()
    data class Success(val user: User) : UserDetailUiState()
    data class Error(val message: String) : UserDetailUiState()
}

sealed class DeleteUserUiState {
    object Idle : DeleteUserUiState()
    object Loading : DeleteUserUiState()
    object Success : DeleteUserUiState()
    data class Error(val message: String) : DeleteUserUiState()
}

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userState = MutableStateFlow<UserUiState>(UserUiState.Idle)
    val userState: StateFlow<UserUiState> get() = _filteredUsers

    private val _userDetailState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Idle)
    val userDetailState: StateFlow<UserDetailUiState> = _userDetailState

    private val _deleteUserState = MutableStateFlow<DeleteUserUiState>(DeleteUserUiState.Idle)
    val deleteUserState: StateFlow<DeleteUserUiState> = _deleteUserState

    private var _originalUsers = listOf<User>()
    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow("Semua")
    private val _filteredUsers = MutableStateFlow<UserUiState>(UserUiState.Idle)

    init {
        combine(_searchQuery, _selectedFilter) { query, filter ->
            filterUsers(query, filter)
        }.launchIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedFilter(filter: String) {
        _selectedFilter.value = filter
    }

    private fun filterUsers(query: String, filter: String) {
        val filtered = _originalUsers.filter { user ->
            // Sekarang aman karena user.name sudah Nullable (String?)
            val nameToSearch = when {
                user.role.equals("ADMIN", ignoreCase = true) -> "Sistem Administrator"
                else -> user.name ?: ""
            }

            val matchesQuery = nameToSearch.contains(query, ignoreCase = true) ||
                    user.email.contains(query, ignoreCase = true)

            val matchesFilter = if (filter == "Semua") true
            else user.role.equals(filter, ignoreCase = true)

            matchesQuery && matchesFilter
        }
        _filteredUsers.value = UserUiState.Success(filtered)
    }

    fun getAllUsers() {
        viewModelScope.launch {
            _userState.value = UserUiState.Loading
            try {
                val response = userRepository.getAllUsers()
                if (response.isSuccessful) {
                    _originalUsers = response.body() ?: emptyList()
                    filterUsers(_searchQuery.value, _selectedFilter.value)
                } else {
                    _userState.value = UserUiState.Error("Gagal memuat data (Status: ${response.code()})")
                }
            } catch (e: JsonDataException) {
                // Pesan error khusus jika parsing JSON gagal
                val errorMsg = "Gagal Parsing: Field 'nama' di JSON server tidak cocok dengan 'name' di Android, atau ada nilai null yang dipaksa masuk."
                _userState.value = UserUiState.Error(errorMsg)
            } catch (e: Exception) {
                _userState.value = UserUiState.Error("Masalah Jaringan: ${e.message}")
            }
        }
    }

    fun getUserById(userId: Long) {
        _userDetailState.value = UserDetailUiState.Loading
        val user = _originalUsers.find { it.id == userId }
        if (user != null) {
            _userDetailState.value = UserDetailUiState.Success(user)
        } else {
            _userDetailState.value = UserDetailUiState.Error("User tidak ditemukan di cache!")
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            _deleteUserState.value = DeleteUserUiState.Loading
            try {
                val response = userRepository.deleteUser(userId)
                if (response.isSuccessful) {
                    _deleteUserState.value = DeleteUserUiState.Success
                    getAllUsers()
                } else {
                    _deleteUserState.value = DeleteUserUiState.Error("Gagal hapus. Periksa izin akses Admin.")
                }
            } catch (e: Exception) {
                _deleteUserState.value = DeleteUserUiState.Error(e.message ?: "Error")
            }
        }
    }
}
