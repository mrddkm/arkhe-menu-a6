package com.arkhe.menu.presentation.viewmodel

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

    private val _categoriesState = MutableStateFlow<ApiResult<List<Category>>>(ApiResult.Loading)
    val categoriesState: StateFlow<ApiResult<List<Category>>> = _categoriesState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                val sessionToken = sessionManager.sessionToken.first()
                if (sessionToken != null) {
                    categoryUseCases.getCategories(sessionToken, forceRefresh)
                        .collect { result ->
                            _categoriesState.value = result
                        }
                } else {
                    _categoriesState.value =
                        ApiResult.Error(Exception("No session token available"))
                }
            } catch (e: Exception) {
                _categoriesState.value = ApiResult.Error(e)
            }
        }
    }

    fun refreshCategories() {
        loadCategories(forceRefresh = true)
    }

    fun selectCategory(category: Category) {
        _selectedCategory.value = category
    }

    fun clearSelectedCategory() {
        _selectedCategory.value = null
    }

    suspend fun getCategoryById(id: String): Category? {
        return categoryUseCases.getCategory(id)
    }
}