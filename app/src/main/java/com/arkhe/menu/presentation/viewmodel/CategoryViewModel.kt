@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.model.CategoryName
import com.arkhe.menu.domain.usecase.category.CategoryUseCases
import com.arkhe.menu.utils.Constants.CurrentLanguage.ENGLISH
import com.arkhe.menu.utils.Constants.CurrentLanguage.INDONESIAN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryUseCases: CategoryUseCases,
    private val sessionManager: SessionManager
) : ViewModel() {

    companion object {
        private const val TAG = "CategoryViewModel"
    }

    private val _categoriesState =
        MutableStateFlow<SafeApiResult<List<Category>>>(SafeApiResult.Loading)
    val categoriesState: StateFlow<SafeApiResult<List<Category>>> = _categoriesState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    private val _lastScrollPosition = MutableStateFlow(0)
    val lastScrollPosition: StateFlow<Int> = _lastScrollPosition.asStateFlow()

    private var isInitialized = false

    init {
        Log.d("init", "## CategoryViewModel::initialized ##")
        Log.d(TAG, "  - categoryUseCases: $categoryUseCases")
        Log.d(TAG, "  - sessionManager: $sessionManager")

        viewModelScope.launch {
            try {
                categoryUseCases.getCategories(sessionManager.getTokenForApiCall())
                    .collectLatest { categoriesResult ->
                        Log.d(TAG, "📊 Categories result: ${categoriesResult::class.simpleName}")
                        _categoriesState.value = categoriesResult
                    }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error in init: ${e.message}", e)
                _categoriesState.value = SafeApiResult.Error(e)
            }
        }
    }

    fun loadCategories(forceRefresh: Boolean = false) {
        Log.d(TAG, "========== loadCategories ==========")
        Log.d(TAG, "forceRefresh: $forceRefresh")

        if (!forceRefresh) {
            Log.d(TAG, "Using local DB data flow, no API call.")
            return
        }

        viewModelScope.launch {
            try {
                _categoriesState.value = SafeApiResult.Loading

                val sessionToken = sessionManager.getTokenForApiCall()
                Log.d(TAG, "🔑 Token from SessionManager: ${sessionToken.take(8)}...")

                val result = categoryUseCases.refreshCategories(sessionToken)
                when (result) {
                    is SafeApiResult.Loading -> {
                        Log.d(TAG, "⏳ Categories loading...")
                        _categoriesState.value = result
                    }

                    is SafeApiResult.Success -> {
                        Log.d(TAG, "✅ Categories loaded successfully: ${result.data.size} items")
                        result.data.forEach { category ->
                            Log.d(TAG, "📋 Category: ${category.name}")
                        }
                        _categoriesState.value = result
                    }

                    is SafeApiResult.Error -> {
                        Log.e(TAG, "❌ Error loading categories: ${result.exception.message}")
                        _categoriesState.value = result
                        handleTokenError(result.exception, forceRefresh)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception in loadCategories: ${e.message}", e)
                _categoriesState.value = SafeApiResult.Error(e)
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
                    Log.d(TAG, "🔑 Retrying with refreshed token: ${newToken.take(8)}...")
                    loadCategories(forceRefresh = true)
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Error in token refresh retry: ${e.message}")
                }
            }
        }
    }

    fun ensureDataLoaded() {
        Log.d(
            TAG,
            "ensureDataLoaded called - isInitialized: $isInitialized, currentState: ${_categoriesState.value::class.simpleName}"
        )

        if (!isInitialized) {
            viewModelScope.launch {
                try {
                    val token = sessionManager.getTokenForApiCall()
                    val categoriesResult = categoryUseCases.getCategories(token).firstOrNull()

                    when (categoriesResult) {
                        is SafeApiResult.Success -> {
                            if (categoriesResult.data.isEmpty()) {
                                Log.d(TAG, "🆕 No local data found, syncing categories from API...")
                                loadCategories(forceRefresh = true)
                            } else {
                                Log.d(TAG, "🆗 Local data found, no need to sync.")
                                _categoriesState.value = categoriesResult
                            }
                        }

                        is SafeApiResult.Error -> {
                            Log.e(
                                TAG,
                                "❌ Error getting categories: ${categoriesResult.exception.message}"
                            )
                            _categoriesState.value = categoriesResult
                        }

                        SafeApiResult.Loading -> {
                            Log.d(TAG, "🆗 Loading...")
                        }

                        null -> {
                            Log.d(TAG, "🆗 null result, loading fresh data...")
                            loadCategories(forceRefresh = true)
                        }
                    }
                    isInitialized = true
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Exception in ensureDataLoaded: ${e.message}", e)
                    _categoriesState.value = SafeApiResult.Error(e)
                }
            }
        } else {
            when (_categoriesState.value) {
                is SafeApiResult.Error -> {
                    Log.d(TAG, "🔄 Retrying after error...")
                    loadCategories(forceRefresh = false)
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

    fun refreshCategories() {
        Log.d(TAG, "refreshCategories called")
        loadCategories(forceRefresh = true)
    }

    fun selectCategory(category: Category) {
        try {
            Log.d(TAG, "📌 Category selected: ${category.name}")
            _selectedCategory.value = category
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error selecting category: ${e.message}", e)
        }
    }

    fun getActionInfo(language: String = ENGLISH): String {
        val products = (_categoriesState.value as? SafeApiResult.Success)?.data?.firstOrNull()
        return if (language == INDONESIAN) {
            products?.actionInfo?.information?.indonesian ?: ""
        } else {
            products?.actionInfo?.information?.english ?: ""
        }
    }

    fun updateScrollPosition(position: Int) {
        try {
            _lastScrollPosition.value = position
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error updating scroll position: ${e.message}", e)
        }
    }

    fun getCategoryNames(): List<CategoryName> {
        return (categoriesState.value as? SafeApiResult.Success)?.data?.map {
            CategoryName(
                id = it.id,
                name = it.name,
                colors = it.colors   // ambil dari Category
            )
        } ?: emptyList()
    }

    fun getCategoryTypes(): List<String> {
        return (categoriesState.value as? SafeApiResult.Success)?.data
            ?.map { it.type }
            ?.distinct()
            ?: emptyList()
    }

    fun getScrollPosition(): Int = _lastScrollPosition.value
}