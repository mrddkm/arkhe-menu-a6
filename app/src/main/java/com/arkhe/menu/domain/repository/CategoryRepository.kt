package com.arkhe.menu.domain.repository

import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategories(sessionToken: String, forceRefresh: Boolean = false): Flow<ApiResult<List<Category>>>
    suspend fun getCategory(id: String): Category?
    suspend fun refreshCategories(sessionToken: String): ApiResult<List<Category>>
}