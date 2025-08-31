package com.arkhe.menu.domain.usecase.category

import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(
        sessionToken: String,
        forceRefresh: Boolean = false
    ): Flow<ApiResult<List<Category>>> {
        return repository.getCategories(sessionToken, forceRefresh)
    }
}

class GetCategoryUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(id: String): Category? {
        return repository.getCategory(id)
    }
}

class RefreshCategoriesUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(sessionToken: String): ApiResult<List<Category>> {
        return repository.refreshCategories(sessionToken)
    }
}

data class CategoryUseCases(
    val getCategories: GetCategoriesUseCase,
    val getCategory: GetCategoryUseCase,
    val refreshCategories: RefreshCategoriesUseCase
)
