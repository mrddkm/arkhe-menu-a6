@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.ActionInfo
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.usecase.profile.ProfileUseCases
import com.arkhe.menu.utils.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileUseCases: ProfileUseCases,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var loadProfilesJob: Job? = null

    init {
        Log.d("init", "## ProfileViewModel::initialized ##")
        loadProfiles()
    }

    fun loadProfiles(forceRefresh: Boolean = false) {
        Log.d("ProfileViewModel", "========== START ==========")
        loadProfilesJob?.cancel()

        loadProfilesJob = viewModelScope.launch {
            try {
                val token = sessionManager.sessionToken.first()

                if (token != null) {
                    Log.d("ProfileViewModel", "========== USE TOKEN ==========")
                    Log.d("ProfileViewModel", "✅ USE TOKEN: $token")

                    profileUseCases.getProfiles(token, forceRefresh).collect { result ->
                        when (result) {
                            is SafeApiResult.Loading -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = true,
                                    error = null
                                )
                            }

                            is SafeApiResult.Success -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    profiles = result.data,
                                    error = null
                                )
                            }

                            is SafeApiResult.Error -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    error = result.exception.message ?: "Unknown error occurred"
                                )
                            }
                        }
                    }
                } else {
                    Log.d("ProfileViewModel", "========== DEFAULT TOKEN ==========")
                    val defaultToken = Constants.Simulation.TOKEN
                    sessionManager.saveSession(defaultToken)
                    Log.d("ProfileViewModel", "⚠️ DEFAULT TOKEN: $defaultToken")
                    Log.d("ProfileViewModel", "===================================")

                    profileUseCases.getProfiles(defaultToken, forceRefresh).collect { result ->
                        when (result) {
                            is SafeApiResult.Loading -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = true,
                                    error = null
                                )
                            }

                            is SafeApiResult.Success -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    profiles = result.data,
                                    error = null
                                )
                            }

                            is SafeApiResult.Error -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    error = result.exception.message ?: "Unknown error occurred"
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load profiles"
                )
            }
        }
    }

    fun refreshProfiles() {
        viewModelScope.launch {
            try {
                val token = sessionManager.sessionToken.first() ?: "WWUmyoU9yWjVUsn8"
                _uiState.value = _uiState.value.copy(isRefreshing = true)

                when (val result = profileUseCases.refreshProfiles(token)) {
                    is SafeApiResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            profiles = result.data,
                            error = null
                        )
                    }

                    is SafeApiResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            error = result.exception.message ?: "Failed to refresh profiles"
                        )
                    }

                    SafeApiResult.Loading -> {
                        // Should not happen in direct API call
                        _uiState.value = _uiState.value.copy(isRefreshing = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    error = e.message ?: "Failed to refresh profiles"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun getActionInfo(): ActionInfo? {
        return _uiState.value.actionInfo.find { it.action == "profile" }
    }

    fun getProfile(): Profile? {
        return _uiState.value.profiles.find { it.nameShort == "tripkeun" }
    }

    override fun onCleared() {
        super.onCleared()
        loadProfilesJob?.cancel()
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val profiles: List<Profile> = emptyList(),
    val actionInfo: List<ActionInfo> = emptyList(),
    val error: String? = null
)