@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.utils.ScrollStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionManager: SessionManager,
    private val scrollStateManager: ScrollStateManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            try {
                sessionManager.ensureTokenAvailable()
            } catch (e: Exception) {
                setError("Failed to initialize app")
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
        navigateToSubScreen(NavigationRoute.PROFILE)
    }

    fun navigateToOrganization() {
        navigateToSubScreen(NavigationRoute.ORGANIZATION)
    }

    fun navigateToCustomer() {
        navigateToSubScreen(NavigationRoute.CUSTOMER)
    }

    fun navigateToCategory() {
        navigateToSubScreen(NavigationRoute.CATEGORIES)
    }

    fun navigateToProducts() {
        navigateToSubScreen(NavigationRoute.PRODUCTS)
    }

    private fun navigateToSubScreen(route: String) {
        val currentBottomNavTitle = _uiState.value.selectedBottomNavItem.title

        _uiState.value = _uiState.value.copy(
            isInMainContent = true,
            showBottomBar = false,
            currentContentType = currentBottomNavTitle,
            currentScreen = route
        )
    }

    /*Navigation State Management*/
    fun updateNavigationState(currentRoute: String?) {
        val currentState = _uiState.value

        when (currentRoute) {
            NavigationRoute.MAIN -> {
                if (currentState.isInMainContent || !currentState.showBottomBar) {
                    _uiState.value = currentState.copy(
                        isInMainContent = false,
                        showBottomBar = true,
                        currentContentType = "",
                        currentScreen = NavigationRoute.MAIN
                    )
                }
            }

            NavigationRoute.PROFILE,
            NavigationRoute.ORGANIZATION,
            NavigationRoute.CUSTOMER,
            NavigationRoute.CATEGORIES,
            NavigationRoute.PRODUCTS -> {
                val bottomNavTitle = currentState.selectedBottomNavItem.title
                if (!currentState.isInMainContent || currentState.showBottomBar || currentState.currentContentType != bottomNavTitle) {
                    _uiState.value = currentState.copy(
                        isInMainContent = true,
                        showBottomBar = false,
                        currentContentType = bottomNavTitle,
                        currentScreen = currentRoute
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

    fun setError(error: String?) {
        _uiState.value = _uiState.value.copy(error = error)
    }

    override fun onCleared() {
        super.onCleared()
        scrollStateManager.clearAllScrollStates()
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