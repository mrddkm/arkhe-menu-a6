package com.arkhe.menu.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.usecase.category.CategoryUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // For maintaining scroll position
    private val _lastScrollPosition = MutableStateFlow(0)
    val lastScrollPosition: StateFlow<Int> = _lastScrollPosition.asStateFlow()

    private var isInitialized = false

    init {
        Log.d("init", "## CategoryViewModel::initialized ##")
        viewModelScope.launch {
            sessionManager.sessionToken.collect { token ->
                if (token != null) {
                    Log.d(TAG, "✅ Token $TAG: $token")
                    loadCategories(token)
                } else {
                    Log.w(TAG, "⚠️ Token $TAG: null")
                }
            }
        }
    }

    fun loadCategories(token: String? = null, forceRefresh: Boolean = false) {
        Log.d(TAG, "loadCategories token: $token")

        Log.d(TAG, "loadCategories called with forceRefresh: $forceRefresh")
        viewModelScope.launch {
            try {
                Log.d(TAG, "Getting session token...")

                if (token != null) {
                    Log.d(TAG, "Starting categories fetch...")
                    _categoriesState.value = SafeApiResult.Loading

                    categoryUseCases.getCategories(token, forceRefresh)
                        .collect { result ->
                            Log.d(TAG, "Categories result received: ${result::class.simpleName}")
                            when (result) {
                                is SafeApiResult.Loading -> {
                                    Log.d(TAG, "Categories loading...")
                                }

                                is SafeApiResult.Success -> {
                                    Log.d(
                                        TAG,
                                        "Categories loaded successfully: ${result.data.size} items"
                                    )
                                    result.data.forEach { category ->
                                        Log.d(TAG, "Category: ${category.id} - ${category.name}")
                                    }
                                }

                                is SafeApiResult.Error -> {
                                    Log.e(TAG, "Categories error: ${result.exception.message}")
                                }
                            }
                            _categoriesState.value = result
                        }
                } else {
                    Log.e(TAG, "No session token available")
                    _categoriesState.value =
                        SafeApiResult.Error(Exception("No session token available"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in loadCategories: ${e.message}", e)
                _categoriesState.value = SafeApiResult.Error(e)
            }
        }
    }

    fun ensureDataLoaded() {
        Log.d(
            TAG,
            "ensureDataLoaded called - isInitialized: $isInitialized, currentState: ${_categoriesState.value::class.simpleName}"
        )

        if (!isInitialized) {
            Log.d(TAG, "First time loading data...")
            loadCategories(forceRefresh = false)
            isInitialized = true
        } else if (_categoriesState.value is SafeApiResult.Error) {
            Log.d(TAG, "Retrying after error...")
            loadCategories(forceRefresh = false)
        } else {
            Log.d(TAG, "Data already loaded or loading")
        }
    }

    fun refreshCategories() {
        Log.d(TAG, "refreshCategories called")
        loadCategories(forceRefresh = true)
    }

    fun selectCategory(category: Category) {
        Log.d(TAG, "Category selected: ${category.name}")
        _selectedCategory.value = category
    }

    fun clearSelectedCategory() {
        Log.d(TAG, "Selected category cleared")
        _selectedCategory.value = null
    }

    fun updateScrollPosition(position: Int) {
        _lastScrollPosition.value = position
    }

    fun getScrollPosition(): Int = _lastScrollPosition.value

    suspend fun getCategoryById(id: String): Category? {
        Log.d(TAG, "Getting category by ID: $id")
        return categoryUseCases.getCategory(id)
    }
}