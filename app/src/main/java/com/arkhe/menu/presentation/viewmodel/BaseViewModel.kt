package com.arkhe.menu.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.data.remote.api.SafeApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<Domain>(
    private val sessionManager: SessionManager
) : ViewModel() {

    companion object {
        private const val TAG = "BaseViewModel"
    }

    private val _state = MutableStateFlow<SafeApiResult<List<Domain>>>(SafeApiResult.Loading)
    val state: StateFlow<SafeApiResult<List<Domain>>> = _state.asStateFlow()

    private var isInitialized = false

    /*✅ Abstract Methods that must be implemented by Child Class*/
    abstract suspend fun fetchData(token: String, forceRefresh: Boolean): Flow<SafeApiResult<List<Domain>>>
    abstract suspend fun syncData(token: String): SafeApiResult<List<Domain>>

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch {
            try {
                sessionManager.sessionToken.collect { token ->
                    if (token != null && !isInitialized) {
                        Log.d(TAG, "Token available, initializing data")
                        loadData(token, forceRefresh = false)
                        isInitialized = true
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in initializeData: ${e.message}", e)
                _state.value = SafeApiResult.Error(e)
            }
        }
    }

    protected fun loadData(token: String? = null, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                // ✅ Better token handling
                val sessionToken = token ?: run {
                    val retrievedToken = sessionManager.getTokenForApiCall()
                    if (retrievedToken.isEmpty()) {
                        Log.e(TAG, "❌ No valid token available")
                        _state.value = SafeApiResult.Error(
                            IllegalStateException("Authentication token not available")
                        )
                        return@launch
                    }
                    retrievedToken
                }

                Log.d(TAG, "Loading data - forceRefresh: $forceRefresh")

                if (!forceRefresh) {
                    // ✅ Better error handling for fetchData
                    try {
                        fetchData(sessionToken, false).collect { result ->
                            Log.d(TAG, "Data received: ${result::class.simpleName}")
                            _state.value = result
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error in fetchData flow: ${e.message}", e)
                        _state.value = SafeApiResult.Error(e)
                    }
                } else {
                    _state.value = SafeApiResult.Loading
                    // ✅ Better error handling for syncData
                    try {
                        val syncResult = syncData(sessionToken)
                        _state.value = syncResult
                    } catch (e: Exception) {
                        Log.e(TAG, "Error in syncData: ${e.message}", e)
                        _state.value = SafeApiResult.Error(e)
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error loading data: ${e.message}", e)
                _state.value = SafeApiResult.Error(e)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val token = sessionManager.getTokenForApiCall()
                if (token.isEmpty()) {
                    Log.e(TAG, "❌ Cannot refresh: No valid token")
                    _state.value = SafeApiResult.Error(
                        IllegalStateException("Authentication token not available for refresh")
                    )
                    return@launch
                }
                loadData(token, forceRefresh = true)
            } catch (e: Exception) {
                Log.e(TAG, "Error in refresh: ${e.message}", e)
                _state.value = SafeApiResult.Error(e)
            }
        }
    }

    fun ensureDataLoaded() {
        if (!isInitialized) {
            viewModelScope.launch {
                try {
                    val token = sessionManager.getTokenForApiCall()
                    if (token.isEmpty()) {
                        Log.e(TAG, "❌ Cannot ensure data loaded: No valid token")
                        _state.value = SafeApiResult.Error(
                            IllegalStateException("Authentication token not available")
                        )
                        return@launch
                    }
                    loadData(token, forceRefresh = false)
                    isInitialized = true
                } catch (e: Exception) {
                    Log.e(TAG, "Error in ensureDataLoaded: ${e.message}", e)
                    _state.value = SafeApiResult.Error(e)
                }
            }
        }
    }

    // ✅ Add method to check if dependencies are properly initialized
    protected open fun validateDependencies(): Boolean {
        return true // Override in child classes
    }

    // ✅ Add method to handle dependency validation
    protected fun checkAndHandleDependencies() {
        if (!validateDependencies()) {
            Log.e(TAG, "❌ Dependencies validation failed")
            _state.value = SafeApiResult.Error(
                IllegalStateException("Required dependencies are not properly initialized")
            )
        }
    }
}