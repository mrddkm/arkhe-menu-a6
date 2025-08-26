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

    /*Navigate to Screen*/
    fun navigateToMainContent(contentType: String) {
        _uiState.value = _uiState.value.copy(
            isInMainContent = true,
            showBottomBar = false,
            currentContentType = contentType
        )
    }

    fun navigateBackToMain() {
        _uiState.value = _uiState.value.copy(
            isInMainContent = false,
            showBottomBar = true,
            currentContentType = "",
            currentScreen = NavigationRoute.MAIN
        )
    }

    fun navigateToProfile() {
        val currentBottomNavTitle = _uiState.value.selectedBottomNavItem.title
        _uiState.value = _uiState.value.copy(
            isInMainContent = true,
            showBottomBar = false,
            currentContentType = currentBottomNavTitle,
            currentScreen = NavigationRoute.PROFILE
        )
    }

    fun navigateToOrganization() {
        val currentBottomNavTitle = _uiState.value.selectedBottomNavItem.title
        _uiState.value = _uiState.value.copy(
            isInMainContent = true,
            showBottomBar = false,
            currentContentType = currentBottomNavTitle,
            currentScreen = NavigationRoute.ORGANIZATION
        )
    }

    fun navigateToCustomer() {
        val currentBottomNavTitle = _uiState.value.selectedBottomNavItem.title
        _uiState.value = _uiState.value.copy(
            isInMainContent = true,
            showBottomBar = false,
            currentContentType = currentBottomNavTitle,
            currentScreen = NavigationRoute.CUSTOMER
        )
    }

    fun navigateToCategory() {
        val currentBottomNavTitle = _uiState.value.selectedBottomNavItem.title
        _uiState.value = _uiState.value.copy(
            isInMainContent = true,
            showBottomBar = false,
            currentContentType = currentBottomNavTitle,
            currentScreen = NavigationRoute.CATEGORIES
        )
    }

    fun navigateToProducts() {
        val currentBottomNavTitle = _uiState.value.selectedBottomNavItem.title
        _uiState.value = _uiState.value.copy(
            isInMainContent = true,
            showBottomBar = false,
            currentContentType = currentBottomNavTitle,
            currentScreen = NavigationRoute.PRODUCTS
        )
    }

    /*Navigate State*/
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

            NavigationRoute.PROFILE -> {
                val bottomNavTitle = currentState.selectedBottomNavItem.title
                if (!currentState.isInMainContent || currentState.showBottomBar || currentState.currentContentType != bottomNavTitle) {
                    _uiState.value = currentState.copy(
                        isInMainContent = true,
                        showBottomBar = false,
                        currentContentType = bottomNavTitle
                    )
                }
            }

            NavigationRoute.ORGANIZATION -> {
                val bottomNavTitle = currentState.selectedBottomNavItem.title
                if (!currentState.isInMainContent || currentState.showBottomBar || currentState.currentContentType != bottomNavTitle) {
                    _uiState.value = currentState.copy(
                        isInMainContent = true,
                        showBottomBar = false,
                        currentContentType = bottomNavTitle
                    )
                }
            }

            NavigationRoute.CUSTOMER -> {
                val bottomNavTitle = currentState.selectedBottomNavItem.title
                if (!currentState.isInMainContent || currentState.showBottomBar || currentState.currentContentType != bottomNavTitle) {
                    _uiState.value = currentState.copy(
                        isInMainContent = true,
                        showBottomBar = false,
                        currentContentType = bottomNavTitle
                    )
                }
            }

            NavigationRoute.CATEGORIES -> {
                val bottomNavTitle = currentState.selectedBottomNavItem.title
                if (!currentState.isInMainContent || currentState.showBottomBar || currentState.currentContentType != bottomNavTitle) {
                    _uiState.value = currentState.copy(
                        isInMainContent = true,
                        showBottomBar = false,
                        currentContentType = bottomNavTitle
                    )
                }
            }

            NavigationRoute.PRODUCTS -> {
                val bottomNavTitle = currentState.selectedBottomNavItem.title
                if (!currentState.isInMainContent || currentState.showBottomBar || currentState.currentContentType != bottomNavTitle) {
                    _uiState.value = currentState.copy(
                        isInMainContent = true,
                        showBottomBar = false,
                        currentContentType = bottomNavTitle
                    )
                }
            }

            NavigationRoute.CREATE_RECEIPT -> {
                val bottomNavTitle = currentState.selectedBottomNavItem.title
                if (!currentState.isInMainContent || currentState.showBottomBar || currentState.currentContentType != bottomNavTitle) {
                    _uiState.value = currentState.copy(
                        isInMainContent = true,
                        showBottomBar = false,
                        currentContentType = bottomNavTitle
                    )
                }
            }

            NavigationRoute.RECEIPT_LIST -> {
                val bottomNavTitle = currentState.selectedBottomNavItem.title
                if (!currentState.isInMainContent || currentState.showBottomBar || currentState.currentContentType != bottomNavTitle) {
                    _uiState.value = currentState.copy(
                        isInMainContent = true,
                        showBottomBar = false,
                        currentContentType = bottomNavTitle
                    )
                }
            }
        }
    }

    /*Other Functions*/
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
    val currentScreen: String = NavigationRoute.MAIN,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class BottomNavItem(val title: String) {
    DOCS("Docs"),
    TRIPKEUN("tripkeun"),
    ACTIVITY("Activity")
}