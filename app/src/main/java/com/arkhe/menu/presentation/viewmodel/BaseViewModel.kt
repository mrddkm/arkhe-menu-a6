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
            sessionManager.sessionToken.collect { token ->
                if (token != null && !isInitialized) {
                    Log.d(TAG, "Token available, initializing data")
                    loadData(token, forceRefresh = false)
                    isInitialized = true
                }
            }
        }
    }

    protected fun loadData(token: String? = null, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                val sessionToken = token ?: sessionManager.getTokenForApiCall()
                Log.d(TAG, "Loading data - forceRefresh: $forceRefresh")

                if (!forceRefresh) {
                    fetchData(sessionToken, false).collect { result ->
                        Log.d(TAG, "Data received: ${result::class.simpleName}")
                        _state.value = result
                    }
                } else {
                    _state.value = SafeApiResult.Loading
                    val syncResult = syncData(sessionToken)
                    _state.value = syncResult
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error loading data: ${e.message}", e)
                _state.value = SafeApiResult.Error(e)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val token = sessionManager.getTokenForApiCall()
            loadData(token, forceRefresh = true)
        }
    }

    fun ensureDataLoaded() {
        if (!isInitialized) {
            viewModelScope.launch {
                val token = sessionManager.getTokenForApiCall()
                loadData(token, forceRefresh = false)
                isInitialized = true
            }
        }
    }
}