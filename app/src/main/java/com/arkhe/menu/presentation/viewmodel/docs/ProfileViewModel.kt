package com.arkhe.menu.presentation.viewmodel.docs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.domain.usecase.GetUserRoleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserRoleUseCase: GetUserRoleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            getUserRoleUseCase().collect { role ->
                _uiState.value = _uiState.value.copy(
                    userRole = role,
                    userName = "John Doe", // Placeholder
                    userEmail = "john.doe@tripkeun.com" // Placeholder
                )
            }
        }
    }

    fun updateProfile(name: String, email: String) {
        _uiState.value = _uiState.value.copy(
            userName = name,
            userEmail = email
        )
    }
}

data class ProfileUiState(
    val userRole: UserRole = UserRole.UNSPECIFIED,
    val userName: String = "",
    val userEmail: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)