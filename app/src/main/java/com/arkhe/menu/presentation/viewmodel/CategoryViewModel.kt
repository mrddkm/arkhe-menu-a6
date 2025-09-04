package com.arkhe.menu.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.usecase.category.CategoryUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryUseCases: CategoryUseCases,
    private val sessionManager: SessionManager
) : ViewModel() {

    companion object {
        private const val TAG = "CategoryViewModel"
    }

    private val _categoriesState = MutableStateFlow<ApiResult<List<Category>>>(ApiResult.Loading)
    val categoriesState: StateFlow<ApiResult<List<Category>>> = _categoriesState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    // For maintaining scroll position
    private val _lastScrollPosition = MutableStateFlow(0)
    val lastScrollPosition: StateFlow<Int> = _lastScrollPosition.asStateFlow()

    private var isInitialized = false

    init {
        Log.d(TAG, "CategoryViewModel initialized")
    }

    fun loadCategories(forceRefresh: Boolean = false) {
        Log.d(TAG, "loadCategories called with forceRefresh: $forceRefresh")
        viewModelScope.launch {
            try {
                Log.d(TAG, "Getting session token...")
                val sessionToken = sessionManager.sessionToken.first()
                Log.d(TAG, "Session token: ${sessionToken?.take(20)}...")

                if (sessionToken != null) {
                    Log.d(TAG, "Starting categories fetch...")
                    _categoriesState.value = ApiResult.Loading

                    categoryUseCases.getCategories(sessionToken, forceRefresh)
                        .collect { result ->
                            Log.d(TAG, "Categories result received: ${result::class.simpleName}")
                            when (result) {
                                is ApiResult.Loading -> {
                                    Log.d(TAG, "Categories loading...")
                                }
                                is ApiResult.Success -> {
                                    Log.d(TAG, "Categories loaded successfully: ${result.data.size} items")
                                    result.data.forEach { category ->
                                        Log.d(TAG, "Category: ${category.id} - ${category.name}")
                                    }
                                }
                                is ApiResult.Error -> {
                                    Log.e(TAG, "Categories error: ${result.exception.message}")
                                }
                            }
                            _categoriesState.value = result
                        }
                } else {
                    Log.e(TAG, "No session token available")
                    _categoriesState.value = ApiResult.Error(Exception("No session token available"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in loadCategories: ${e.message}", e)
                _categoriesState.value = ApiResult.Error(e)
            }
        }
    }

    fun ensureDataLoaded() {
        Log.d(TAG, "ensureDataLoaded called - isInitialized: $isInitialized, currentState: ${_categoriesState.value::class.simpleName}")

        if (!isInitialized) {
            Log.d(TAG, "First time loading data...")
            loadCategories(forceRefresh = false)
            isInitialized = true
        } else if (_categoriesState.value is ApiResult.Error) {
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