package com.arkhe.menu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.usecase.ProfileUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileUseCases: ProfileUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Default session token - dalam implementasi nyata harus dari authentication
    private val sessionToken = "WWUmyoU9yWjVUsn8"

    init {
        loadProfiles()
    }

    fun loadProfiles(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            profileUseCases.getProfiles(sessionToken, forceRefresh).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is ApiResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            profiles = result.data,
                            error = null
                        )
                    }
                    is ApiResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }
                }
            }
        }
    }

    fun refreshProfiles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            when (val result = profileUseCases.refreshProfiles(sessionToken)) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        profiles = result.data,
                        error = null
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = result.exception.message ?: "Failed to refresh profiles"
                    )
                }
                ApiResult.Loading -> {
                    // Should not happen
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun getTripkeunProfile(): Profile? {
        return _uiState.value.profiles.find { it.nameShort == "tripkeun" }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val profiles: List<Profile> = emptyList(),
    val error: String? = null
)