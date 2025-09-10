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

    private val _getProfile = MutableStateFlow<Profile?>(null)
    val getProfile: StateFlow<Profile?> = _getProfile.asStateFlow()

    private var isInitialized = false

    init {
        Log.d("init", "## ProfileViewModel::initialized ##")
    }

    fun loadProfiles(token: String? = null, forceRefresh: Boolean = false) {
        Log.d(TAG, "========== loadProfiles ==========")
        Log.d(TAG, "forceRefresh: $forceRefresh")

        viewModelScope.launch {
            try {
                _profilesState.value = SafeApiResult.Loading

                // Get token from enhanced SessionManager
                val sessionToken = sessionManager.getTokenForApiCall()
                Log.d(TAG, "🔑 Token from SessionManager: $sessionToken")

                profileUseCases.getProfiles(sessionToken, forceRefresh)
                    .collect { result ->
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

                                // Download images after successful profile load
                                downloadImages()
                            }

                            is SafeApiResult.Error -> {
                                Log.e(TAG, "❌ Error loading profiles: ${result.exception.message}")
                                _profilesState.value = result

                                // Handle token-related errors
                                handleTokenError(result.exception, forceRefresh)
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception in loadProfiles: ${e.message}", e)
                _profilesState.value = SafeApiResult.Error(e)
            }
        }
    }

    /**
     * Handle token-related errors with automatic retry
     */
    private fun handleTokenError(exception: Throwable, alreadyRetried: Boolean) {
        val errorMessage = exception.message?.lowercase() ?: ""
        val isTokenError = errorMessage.contains("token") ||
                errorMessage.contains("unauthorized") ||
                errorMessage.contains("authentication")

        if (isTokenError && !alreadyRetried) {
            Log.d(TAG, "🔄 Token error detected, refreshing and retrying...")
            viewModelScope.launch {
                try {
                    // Use enhanced SessionManager to ensure token
                    val newToken = sessionManager.ensureTokenAvailable()
                    Log.d(TAG, "🔑 Retrying with refreshed token: $newToken")
                    loadProfiles(forceRefresh = true) // Retry with force refresh
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Error in token refresh retry: ${e.message}")
                }
            }
        }
    }

    /**
     * Ensure data is loaded - main entry point for UI
     */
    fun ensureDataLoaded() {
        Log.d(
            TAG,
            "ensureDataLoaded called - isInitialized: $isInitialized, currentState: ${_profilesState.value::class.simpleName}"
        )

        when {
            !isInitialized -> {
                Log.d(TAG, "🆕 First time loading data...")
                loadProfiles(forceRefresh = false)
                isInitialized = true
            }

            _profilesState.value is SafeApiResult.Error -> {
                Log.d(TAG, "🔄 Retrying after error...")
                loadProfiles(forceRefresh = false)
            }

            _profilesState.value is SafeApiResult.Loading -> {
                Log.d(TAG, "⏳ Already loading, waiting...")
            }

            else -> {
                Log.d(TAG, "✅ Data already loaded")
            }
        }
    }

    /**
     * Force refresh profiles data
     */
    fun refreshProfiles() {
        Log.d(TAG, "refreshProfiles called")
        loadProfiles(forceRefresh = true)
    }

    /**
     * Set current profile
     */
    fun getProfile(profile: Profile) {
        Log.d(TAG, "📋 Profile selected: ${profile.nameShort}")
        _getProfile.value = profile
    }

    /**
     * Download profile images
     */
    private fun downloadImages() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "📥 Starting image download...")
                profileUseCases.downloadProfileImages()
                Log.d(TAG, "✅ Image download completed")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error downloading images: ${e.message}", e)
            }
        }
    }

    /**
     * Get local image path for profile
     */
    suspend fun getProfileImagePath(nameShort: String): String? {
        return try {
            val path = profileUseCases.getProfileImagePath(nameShort)
            Log.d(TAG, "📁 Image path for $nameShort: $path")
            path
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error getting image path for $nameShort: ${e.message}")
            null
        }
    }

    /**
     * Get current session token for external use
     */
    fun getCurrentToken(): String {
        return sessionManager.getCurrentTokenSyncOrDefault()
    }

    /**
     * Check if we have a valid token
     */
    fun hasValidToken(): Boolean {
        return sessionManager.hasValidToken()
    }
}