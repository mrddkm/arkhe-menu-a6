@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.domain.usecase.GetUserRoleUseCase
import com.arkhe.menu.presentation.navigation.NavigationRoute
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

    fun navigateToProfile() {
        _uiState.value = _uiState.value.copy(
            isInMainContent = true,
            showBottomBar = false,
            currentContentType = "Profile Tripkeun",
            currentScreen = "PROFILE_TRIPKEUN"
        )
    }

    fun navigateBackToMain() {
        _uiState.value = _uiState.value.copy(
            isInMainContent = false,
            showBottomBar = true,
            currentContentType = "",
            currentScreen = "MAIN"
        )
    }

    fun updateNavigationState(currentRoute: String?) {
        val currentState = _uiState.value

        when (currentRoute) {
            NavigationRoute.MAIN -> {
                if (currentState.isInMainContent || !currentState.showBottomBar) {
                    _uiState.value = currentState.copy(
                        isInMainContent = false,
                        showBottomBar = true,
                        currentContentType = ""
                    )
                }
            }

            NavigationRoute.PROFILE_TRIPKEUN -> {
                if (!currentState.isInMainContent || currentState.showBottomBar || currentState.currentContentType != "Profile Tripkeun") {
                    _uiState.value = currentState.copy(
                        isInMainContent = true,
                        showBottomBar = false,
                        currentContentType = "Profile Tripkeun"
                    )
                }
            }

            NavigationRoute.CREATE_RECEIPT -> {
                if (!currentState.isInMainContent || currentState.showBottomBar || currentState.currentContentType != "Create Receipt") {
                    _uiState.value = currentState.copy(
                        isInMainContent = true,
                        showBottomBar = false,
                        currentContentType = "Create Receipt"
                    )
                }
            }

            NavigationRoute.RECEIPT_LIST -> {
                if (!currentState.isInMainContent || currentState.showBottomBar || currentState.currentContentType != "Receipt List") {
                    _uiState.value = currentState.copy(
                        isInMainContent = true,
                        showBottomBar = false,
                        currentContentType = "Receipt List"
                    )
                }
            }
        }
    }

    fun toggleProfileBottomSheet() {
        _uiState.value = _uiState.value.copy(
            showProfileBottomSheet = !_uiState.value.showProfileBottomSheet
        )
    }

    @Suppress("Unused")
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
    val currentScreen: String = "MAIN",
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class BottomNavItem(val title: String) {
    DOCS("Docs"),
    TRIPKEUN("Tripkeun"),
    ACTIVITY("Activity")
}