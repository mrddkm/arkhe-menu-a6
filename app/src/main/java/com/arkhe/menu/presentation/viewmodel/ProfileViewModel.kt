package com.arkhe.menu.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.usecase.profile.ProfileUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileUseCases: ProfileUseCases,
    private val sessionManager: SessionManager
) : ViewModel() {
    /*
        companion object {
            private const val TAG = "ProfileViewModel"
        }
    */
    private val _profilesState =
        MutableStateFlow<SafeApiResult<List<Profile>>>(SafeApiResult.Loading)
    val profilesState: StateFlow<SafeApiResult<List<Profile>>> = _profilesState.asStateFlow()

    private var isInitialized = false

    init {
        viewModelScope.launch {
            profileUseCases.getProfiles(sessionManager.getTokenForApiCall())
                .collectLatest { profilesResult ->
                    _profilesState.value = profilesResult
                }
        }
    }

    fun loadProfiles(forceRefresh: Boolean = false) {
        if (!forceRefresh) {
            return
        }
        viewModelScope.launch {
            try {
                _profilesState.value = SafeApiResult.Loading

                val sessionToken = sessionManager.getTokenForApiCall()

                val result = profileUseCases.syncProfiles(sessionToken)
                when (result) {
                    is SafeApiResult.Loading -> {
                        _profilesState.value = result
                    }

                    is SafeApiResult.Success -> {
                        _profilesState.value = result
                    }

                    is SafeApiResult.Error -> {
                        _profilesState.value = result
                        handleTokenError(result.exception, forceRefresh)
                    }
                }
            } catch (e: Exception) {
                _profilesState.value = SafeApiResult.Error(e)
            }
        }
    }

    private fun handleTokenError(exception: Throwable, alreadyRetried: Boolean) {
        val errorMessage = exception.message?.lowercase() ?: ""
        val isTokenError = errorMessage.contains("token") ||
                errorMessage.contains("unauthorized") ||
                errorMessage.contains("authentication")
        if (isTokenError && !alreadyRetried) {
            viewModelScope.launch {
                try {
                    loadProfiles(forceRefresh = true)
                } catch (_: Exception) {
                }
            }
        }
    }

    fun ensureDataLoaded() {
        if (!isInitialized) {
            viewModelScope.launch {
                val profilesResult = profileUseCases.getProfiles(
                    sessionManager.getTokenForApiCall()
                ).firstOrNull()
                when (profilesResult) {
                    is SafeApiResult.Success -> {
                        if (profilesResult.data.isEmpty()) {
                            loadProfiles(forceRefresh = true)
                        } else {
                            _profilesState.value = profilesResult
                        }
                    }

                    is SafeApiResult.Error -> {
                        _profilesState.value = profilesResult
                    }

                    SafeApiResult.Loading -> {
                    }

                    null -> {
                    }
                }
                isInitialized = true
            }
        } else {
            when (_profilesState.value) {
                is SafeApiResult.Error -> {
                    loadProfiles(forceRefresh = false)
                }

                is SafeApiResult.Loading -> {
                }

                else -> {
                }
            }
        }
    }

    fun refreshProfiles() {
        loadProfiles(forceRefresh = true)
    }
}