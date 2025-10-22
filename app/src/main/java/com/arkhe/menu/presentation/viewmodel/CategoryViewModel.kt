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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
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

    private val _selectedParentTab = MutableStateFlow(0)
    val selectedParentTab: StateFlow<Int> = _selectedParentTab.asStateFlow()

    private val _selectedCategoryTab = MutableStateFlow(0)
    val selectedCategoryTab: StateFlow<Int> = _selectedCategoryTab.asStateFlow()

    private val _selectedTypeTab = MutableStateFlow(0)
    val selectedTypeTab: StateFlow<Int> = _selectedTypeTab.asStateFlow()

    private var isInitialized = false

    init {
        viewModelScope.launch {
            try {
                categoryUseCases.getCategories(sessionManager.getTokenForApiCall())
                    .collectLatest { categoriesResult ->
                        _categoriesState.value = categoriesResult
                    }
            } catch (e: Exception) {
                _categoriesState.value = SafeApiResult.Error(e)
            }
        }
    }

    fun loadCategories(forceRefresh: Boolean = false) {
        if (!forceRefresh) {
            return
        }

        viewModelScope.launch {
            try {
                _categoriesState.value = SafeApiResult.Loading

                val sessionToken = sessionManager.getTokenForApiCall()

                val result = categoryUseCases.refreshCategories(sessionToken)
                when (result) {
                    is SafeApiResult.Loading -> {
                        _categoriesState.value = result
                    }

                    is SafeApiResult.Success -> {
                        result.data.forEach { category ->
                            Log.d(TAG, "📋 Category: ${category.name}")
                        }
                        _categoriesState.value = result
                    }

                    is SafeApiResult.Error -> {
                        _categoriesState.value = result
                        handleTokenError(result.exception, forceRefresh)
                    }
                }
            } catch (e: Exception) {
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
            viewModelScope.launch {
                try {
                    val newToken = sessionManager.ensureTokenAvailable()
                    loadCategories(forceRefresh = true)
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Error in token refresh retry: ${e.message}")
                }
            }
        }
    }

    fun ensureDataLoaded() {
        if (!isInitialized) {
            viewModelScope.launch {
                try {
                    val token = sessionManager.getTokenForApiCall()
                    val categoriesResult = categoryUseCases.getCategories(token).firstOrNull()

                    when (categoriesResult) {
                        is SafeApiResult.Success -> {
                            if (categoriesResult.data.isEmpty()) {
                                loadCategories(forceRefresh = true)
                            } else {
                                _categoriesState.value = categoriesResult
                            }
                        }

                        is SafeApiResult.Error -> {
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
                    _categoriesState.value = SafeApiResult.Error(e)
                }
            }
        } else {
            when (_categoriesState.value) {
                is SafeApiResult.Error -> {
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
        loadCategories(forceRefresh = true)
    }

    fun selectCategory(category: Category) {
        try {
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
                colors = it.colors
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

    fun updateParentTab(index: Int) {
        _selectedParentTab.value = index
    }

    fun updateCategoryTab(index: Int) {
        _selectedCategoryTab.value = index
    }

    fun updateTypeTab(index: Int) {
        _selectedTypeTab.value = index
    }
}