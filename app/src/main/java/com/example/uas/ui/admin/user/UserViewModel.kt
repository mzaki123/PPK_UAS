package com.example.uas.ui.admin.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uas.data.repository.UserRepository
import com.example.uas.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    // Original list of users from the API
    private var _originalUsers = listOf<User>()

    // StateFlows for search and filter
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
            val matchesQuery = user.name.contains(query, ignoreCase = true) ||
                               user.email.contains(query, ignoreCase = true)
            val matchesFilter = if (filter == "Semua") true else user.role.equals(filter, ignoreCase = true)
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
                    filterUsers(_searchQuery.value, _selectedFilter.value) // Apply initial filter
                } else {
                    val error = "Failed to fetch users: ${response.message()}"
                    _userState.value = UserUiState.Error(error)
                    _filteredUsers.value = UserUiState.Error(error)
                }
            } catch (e: Exception) {
                val error = "An error occurred: ${e.message}"
                _userState.value = UserUiState.Error(error)
                _filteredUsers.value = UserUiState.Error(error)
            }
        }
    }

    fun getUserById(userId: Long) {
        viewModelScope.launch {
            _userDetailState.value = UserDetailUiState.Loading
            try {
                val response = userRepository.getUserById(userId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _userDetailState.value = UserDetailUiState.Success(it)
                    } ?: run {
                        _userDetailState.value = UserDetailUiState.Error("User not found")
                    }
                } else {
                    _userDetailState.value = UserDetailUiState.Error("Failed to fetch user details: ${response.message()}")
                }
            } catch (e: Exception) {
                _userDetailState.value = UserDetailUiState.Error("An error occurred: ${e.message}")
            }
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            _deleteUserState.value = DeleteUserUiState.Loading
            try {
                val response = userRepository.deleteUser(userId)
                if (response.isSuccessful) {
                    _deleteUserState.value = DeleteUserUiState.Success
                    // Refresh the user list after deletion
                    getAllUsers()
                } else {
                    _deleteUserState.value = DeleteUserUiState.Error("Failed to delete user: ${response.message()}")
                }
            } catch (e: Exception) {
                _deleteUserState.value = DeleteUserUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}
