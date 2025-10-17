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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionManager: SessionManager,
    private val scrollStateManager: ScrollStateManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _scrollAlpha = MutableStateFlow(1f)
    val scrollAlpha: StateFlow<Float> = _scrollAlpha.asStateFlow()

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            try {
                sessionManager.ensureTokenAvailable()
            } catch (_: Exception) {
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
        when {
            currentRoute != null && (currentRoute.startsWith("product_detail") || currentRoute.startsWith(
                "category_detail"
            )) -> {
                return
            }

            currentRoute == NavigationRoute.MAIN -> {
                if (currentState.isInMainContent &&
                    currentState.currentScreen != NavigationRoute.MAIN
                ) {
                    return
                }
                if (currentState.isInMainContent || !currentState.showBottomBar) {
                    _uiState.value = currentState.copy(
                        isInMainContent = false,
                        showBottomBar = true,
                        currentContentType = "",
                        currentScreen = NavigationRoute.MAIN
                    )
                }
            }

            currentRoute == NavigationRoute.PROFILE ||
                    currentRoute == NavigationRoute.ORGANIZATION ||
                    currentRoute == NavigationRoute.CUSTOMER ||
                    currentRoute == NavigationRoute.CATEGORIES ||
                    currentRoute == NavigationRoute.PRODUCTS -> {
                val bottomNavTitle = currentState.selectedBottomNavItem.title
                _uiState.value = currentState.copy(
                    isInMainContent = true,
                    showBottomBar = false,
                    currentContentType = bottomNavTitle,
                    currentScreen = currentRoute
                )
            }
        }
    }

    fun toggleProfileSettingsBottomSheet() {
        _uiState.value = _uiState.value.copy(
            showProfileSettingsBottomSheet = !_uiState.value.showProfileSettingsBottomSheet
        )
    }

    fun showProfileSettingsBottomSheet() {
        _uiState.update { it.copy(showProfileSettingsBottomSheet = true) }
    }

    fun onLanguageChangeStarted() {
        _uiState.value = _uiState.value.copy(
            showProfileSettingsBottomSheet = false,
            isLoadingOverlay = true
        )
    }

    fun onLanguageChangeCompleted() {
        _uiState.value = _uiState.value.copy(
            isLoadingOverlay = false
        )
        navigateBackToMain()
    }

    fun setError(error: String?) {
        _uiState.value = _uiState.value.copy(error = error)
    }

    override fun onCleared() {
        super.onCleared()
        scrollStateManager.clearAllScrollStates()
    }

    fun updateScrollAlpha(alpha: Float) {
        _scrollAlpha.value = alpha.coerceIn(0.4f, 1f)
    }

    fun showLoadingOverlay() {
        _uiState.update { it.copy(isLoadingOverlay = true) }
    }

    fun hideLoadingOverlay() {
        _uiState.update { it.copy(isLoadingOverlay = false) }
    }
}

data class MainUiState(
    val userRole: UserRole = UserRole.FAGA,
    val selectedBottomNavItem: BottomNavItem = BottomNavItem.DOCS,
    val isInMainContent: Boolean = false,
    val showBottomBar: Boolean = true,
    val showProfileSettingsBottomSheet: Boolean = false,
    val currentContentType: String = "",
    val currentScreen: String = NavigationRoute.MAIN,
    val isLoading: Boolean = false,
    val isLoadingOverlay: Boolean = false,
    val error: String? = null,
    val scrollAlpha: Float = 1f
)

enum class BottomNavItem(val title: String) {
    DOCS("Docs"),
    TRIPKEUN("tripkeun"),
    ACTIVITY("Activity")
}