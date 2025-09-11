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

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    private val _lastScrollPosition = MutableStateFlow(0)
    val lastScrollPosition: StateFlow<Int> = _lastScrollPosition.asStateFlow()

    override suspend fun fetchData(
        token: String,
        forceRefresh: Boolean
    ): Flow<SafeApiResult<List<Category>>> {
        Log.d(TAG, "Fetching categories data - forceRefresh: $forceRefresh")
        return categoryUseCases.getCategories(token, forceRefresh)
    }

    override suspend fun syncData(token: String): SafeApiResult<List<Category>> {
        Log.d(TAG, "Syncing categories data")
        return categoryUseCases.refreshCategories(token)
    }

    // ✅ Category-specific methods
    fun selectCategory(category: Category) {
        Log.d(TAG, "Category selected: ${category.name}")
        _selectedCategory.value = category
    }

    fun updateScrollPosition(position: Int) {
        _lastScrollPosition.value = position
    }

    fun getScrollPosition(): Int = _lastScrollPosition.value

    // ✅ Convenience Methods for UI
    fun loadCategories(forceRefresh: Boolean = false) {
        Log.d(TAG, "loadCategories called - forceRefresh: $forceRefresh")
        loadData(forceRefresh = forceRefresh)
    }

    fun refreshCategories() {
        Log.d(TAG, "refreshCategories called")
        refresh()
    }
}