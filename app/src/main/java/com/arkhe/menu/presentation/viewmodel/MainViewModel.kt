@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.domain.usecase.GetUserRoleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getUserRoleUseCase: GetUserRoleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadUserRole()
    }

    private fun loadUserRole() {
        viewModelScope.launch {
            getUserRoleUseCase().collect { role ->
                _uiState.value = _uiState.value.copy(userRole = role)
            }
        }
    }

    fun selectBottomNavItem(item: BottomNavItem) {
        _uiState.value = _uiState.value.copy(
            selectedBottomNavItem = item,
            isInMainContent = false,
            showBottomBar = true
        )
    }

    fun navigateToMainContent(contentType: String) {
        _uiState.value = _uiState.value.copy(
            isInMainContent = true,
            showBottomBar = false,
            currentContentType = contentType
        )
    }

    fun navigateBack() {
        _uiState.value = _uiState.value.copy(
            isInMainContent = false,
            showBottomBar = true,
            currentContentType = ""
        )
    }

    fun toggleProfileBottomSheet() {
        _uiState.value = _uiState.value.copy(
            showProfileBottomSheet = !_uiState.value.showProfileBottomSheet
        )
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    fun setError(error: String?) {
        _uiState.value = _uiState.value.copy(error = error)
    }
}

data class MainUiState(
    val userRole: UserRole = UserRole.FAGA,
    val selectedBottomNavItem: BottomNavItem = BottomNavItem.DOCS,
    val isInMainContent: Boolean = false,
    val showBottomBar: Boolean = true,
    val showProfileBottomSheet: Boolean = false,
    val currentContentType: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class BottomNavItem(val title: String) {
    DOCS("Docs"),
    TRIPKEUN("Tripkeun"),
    ACTIVITY("Activity")
}