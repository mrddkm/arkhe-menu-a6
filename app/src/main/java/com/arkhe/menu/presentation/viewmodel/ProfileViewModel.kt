@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.usecase.profile.ProfileUseCases
import com.arkhe.menu.utils.Constants
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
        viewModelScope.launch {
            sessionManager.sessionToken.collect { token ->
                if (token != null) {
                    Log.d(TAG, "✅ Token ${TAG}: $token")
                    loadProfiles(token)
                } else {
                    Log.w(TAG, "⚠️ Token ${TAG}: null")
                }
            }
        }
    }

    fun loadProfiles(token: String? = null, forceRefresh: Boolean = false) {
        Log.d(TAG, "========== loadProfiles ==========")

        viewModelScope.launch {
            try {
                if (token != null) {
                    _profilesState.value = SafeApiResult.Loading
                    profileUseCases.getProfiles(token, forceRefresh)
                        .collect { result ->
                            when (result) {
                                is SafeApiResult.Loading -> {
                                    Log.d(TAG, "Categories loading...")
                                }

                                is SafeApiResult.Success -> {
                                    result.data.forEach { profile ->
                                        Log.d(TAG, profile.nameShort)

                                    }
                                }

                                is SafeApiResult.Error -> {
                                    result.exception.message?.let { Log.e(TAG, it) }
                                }
                            }
                            _profilesState.value = result
                        }
                    downloadImages()
                } else {
                    Log.e(TAG, "No session token available")
                    _profilesState.value =
                        SafeApiResult.Error(Exception("No session token available"))

                    val defaultToken = Constants.Simulation.TOKEN
                    sessionManager.saveSession(defaultToken)
                }
            } catch (e: Exception) {
                _profilesState.value = SafeApiResult.Error(e)
            }
        }
    }

    fun ensureDataLoaded() {
        Log.d(
            TAG,
            "ensureDataLoaded called - isInitialized: $isInitialized, currentState: ${_profilesState.value::class.simpleName}"
        )

        if (!isInitialized) {
            Log.d(TAG, "First time loading data...")
            loadProfiles(forceRefresh = false)
            isInitialized = true
        } else if (_profilesState.value is SafeApiResult.Error) {
            Log.d(TAG, "Retrying after error...")
            loadProfiles(forceRefresh = false)
        } else {
            Log.d(TAG, "Data already loaded or loading")
        }
    }

    fun refreshProfiles() {
        Log.d(TAG, "refreshProfiles called")
        loadProfiles(forceRefresh = true)
    }

    fun getProfile(profile: Profile) {
        Log.d(TAG, "Profile get: ${profile.nameShort}")
        _getProfile.value = profile
    }

    fun downloadImages() {
        viewModelScope.launch {
            try {
                profileUseCases.downloadProfileImages()
            } catch (e: Exception) {
                Log.e(TAG, "Error downloading images: ${e.message}", e)
            }
        }
    }

    suspend fun getProfileImagePath(nameShort: String): String? {
        return profileUseCases.getProfileImagePath(nameShort)
    }
}