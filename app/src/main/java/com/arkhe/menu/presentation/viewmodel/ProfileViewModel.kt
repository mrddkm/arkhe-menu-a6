@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.viewmodel

import android.util.Log
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

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val _profilesState =
        MutableStateFlow<SafeApiResult<List<Profile>>>(SafeApiResult.Loading)
    val profilesState: StateFlow<SafeApiResult<List<Profile>>> = _profilesState.asStateFlow()

    private var isInitialized = false

    init {
        Log.d("init", "## ProfileViewModel::initialized ##")
        viewModelScope.launch {
            profileUseCases.getProfiles(sessionManager.getTokenForApiCall())
                .collectLatest { profilesResult ->
                    _profilesState.value = profilesResult
                }
        }
    }

    fun loadProfiles(forceRefresh: Boolean = false) {
        Log.d(TAG, "========== loadProfiles ==========")
        Log.d(TAG, "forceRefresh: $forceRefresh")

        if (!forceRefresh) {
            // No need to call API, data comes from DB flow observed in init
            Log.d(TAG, "Using local DB data flow, no API call.")
            return
        }

        viewModelScope.launch {
            try {
                _profilesState.value = SafeApiResult.Loading

                val sessionToken = sessionManager.getTokenForApiCall()
                Log.d(TAG, "🔑 Token from SessionManager: $sessionToken")

                val result = profileUseCases.syncProfiles(sessionToken)
                when (result) {
                    is SafeApiResult.Loading -> {
                        Log.d(TAG, "⏳ Profiles loading...")
                        _profilesState.value = result
                    }

                    is SafeApiResult.Success -> {
                        Log.d(
                            TAG,
                            "✅ Profiles loaded successfully: ${result.data.size} items"
                        )
                        result.data.forEach { profile ->
                            Log.d(TAG, "📋 Profile: ${profile.nameShort}")
                        }
                        _profilesState.value = result
                    }

                    is SafeApiResult.Error -> {
                        Log.e(TAG, "❌ Error loading profiles: ${result.exception.message}")
                        _profilesState.value = result
                        handleTokenError(result.exception, forceRefresh)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception in loadProfiles: ${e.message}", e)
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
            Log.d(TAG, "🔄 Token error detected, refreshing and retrying...")
            viewModelScope.launch {
                try {
                    val newToken = sessionManager.ensureTokenAvailable()
                    Log.d(TAG, "🔑 Retrying with refreshed token: $newToken")
                    loadProfiles(forceRefresh = true)
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Error in token refresh retry: ${e.message}")
                }
            }
        }
    }

    fun ensureDataLoaded() {
        Log.d(
            TAG,
            "ensureDataLoaded called - isInitialized: $isInitialized, currentState: ${_profilesState.value::class.simpleName}"
        )

        if (!isInitialized) {
            viewModelScope.launch {
                val profilesResult =
                    profileUseCases.getProfiles(sessionManager.getTokenForApiCall()).firstOrNull()
                when (profilesResult) {
                    is SafeApiResult.Success -> {
                        if (profilesResult.data.isEmpty()) {
                            Log.d(TAG, "🆕 No local data found, syncing profiles from API...")
                            loadProfiles(forceRefresh = true)
                        } else {
                            Log.d(TAG, "🆗 Local data found, no need to sync.")
                            _profilesState.value = profilesResult
                        }
                    }

                    is SafeApiResult.Error -> {
                        _profilesState.value = profilesResult
                    }

                    SafeApiResult.Loading -> {
                        Log.d(TAG, "🆗 Loading...")
                    }

                    null -> {
                        Log.d(TAG, "🆗 null")
                    }
                }
                isInitialized = true
            }
        } else {
            when (_profilesState.value) {
                is SafeApiResult.Error -> {
                    Log.d(TAG, "🔄 Retrying after error...")
                    loadProfiles(forceRefresh = false)
                }

                is SafeApiResult.Loading -> {
                    Log.d(TAG, "⏳ Already loading, waiting...")
                }

                else -> {
                    Log.d(TAG, "✅ Data already loaded")
                }
            }
        }
    }

    fun refreshProfiles() {
        Log.d(TAG, "refreshProfiles called")
        loadProfiles(forceRefresh = true)
    }
}