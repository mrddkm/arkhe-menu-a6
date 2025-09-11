package com.arkhe.menu.presentation.viewmodel

import android.util.Log
import com.arkhe.menu.data.local.preferences.SessionManager
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.usecase.category.CategoryUseCases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewModel(
    private val categoryUseCases: CategoryUseCases,
    sessionManager: SessionManager
) : BaseViewModel<Category>(sessionManager) {

    companion object {
        private const val TAG = "CategoryViewModel"
    }

    init {
        @Suppress("SENSELESS_COMPARISON")
        if (categoryUseCases == null) {
            Log.e(TAG, "❌ categoryUseCases is NULL! Check DI setup.")
            throw IllegalStateException("CategoryUseCases is not injected!")
        }
    }

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    private val _lastScrollPosition = MutableStateFlow(0)
    val lastScrollPosition: StateFlow<Int> = _lastScrollPosition.asStateFlow()

    override suspend fun fetchData(
        token: String,
        forceRefresh: Boolean
    ): Flow<SafeApiResult<List<Category>>> {
        Log.d(TAG, "🔍 fetchData called - token: $token, forceRefresh: $forceRefresh")
        val result = categoryUseCases.getCategories(token, forceRefresh)
        Log.d(TAG, "📊 fetchData result flow created")
        return result
    }

    override suspend fun syncData(token: String): SafeApiResult<List<Category>> {
        Log.d(TAG, "🔄 syncData called with token: $token")
        val result = categoryUseCases.refreshCategories(token)
        Log.d(TAG, "📊 syncData result: ${result::class.simpleName}")
        return result
    }

    fun selectCategory(category: Category) {
        Log.d(TAG, "📌 Category selected: ${category.name}")
        _selectedCategory.value = category
    }

    fun updateScrollPosition(position: Int) {
        _lastScrollPosition.value = position
    }

    fun getScrollPosition(): Int = _lastScrollPosition.value

    // 🔧 DEBUGGING METHODS
    fun debugCurrentState() {
        Log.d(TAG, "🐛 Current state: ${state.value::class.simpleName}")
        when (val currentState = state.value) {
            is SafeApiResult.Success -> {
                Log.d(TAG, "🐛 Success data count: ${currentState.data.size}")
            }

            is SafeApiResult.Error -> {
                Log.d(TAG, "🐛 Error message: ${currentState.exception.message}")
            }

            is SafeApiResult.Loading -> {
                Log.d(TAG, "🐛 Currently loading...")
            }
        }
    }
}