package com.arkhe.menu.data.repository

import com.arkhe.menu.data.local.dao.CategoryDao
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.mapper.toEntityList
import com.arkhe.menu.data.remote.api.ArkheApiService
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(
    private val api: ArkheApiService,
    private val dao: CategoryDao
) : CategoryRepository {

    override fun getCategories(
        sessionToken: String,
        forceRefresh: Boolean
    ): Flow<SafeApiResult<List<Category>>> {
        return flow {
            if (forceRefresh) {
                emit(SafeApiResult.Loading)
                val syncResult = refreshCategories(sessionToken)
                emit(syncResult)
            } else {
                dao.getAllCategories().collect { entities ->
                    val categories = entities.map { it.toDomain() }
                    if (categories.isNotEmpty()) {
                        emit(SafeApiResult.Success(categories))
                    } else {
                        emit(SafeApiResult.Loading)
                        val syncResult = refreshCategories(sessionToken)
                        emit(syncResult)
                    }
                }
            }
        }
    }

    override suspend fun getCategory(id: String): Category? {
        return withContext(Dispatchers.IO) {
            dao.getCategory(id)?.toDomain()
        }
    }

    override suspend fun refreshCategories(sessionToken: String): SafeApiResult<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCategories(sessionToken)
                val entities = response.toEntityList()

                dao.deleteAllCategories()
                dao.insertCategories(entities)

                val categories = entities.map { it.toDomain() }
                SafeApiResult.Success(categories)

            } catch (e: Exception) {
                SafeApiResult.Failure(e)
            }
        }
    }

    /**
     * Get categories as flow for observing changes - for ViewModel init
     */
    fun getCategoriesFlow(): Flow<List<Category>> {
        return dao.getAllCategories().map { entities ->
            val categories = entities.map { it.toDomain() }
            categories
        }
    }

    /**
     * Check if local database is empty
     */
    private suspend fun isEmpty(): Boolean {
        val count = dao.getCategoryCount()
        return count == 0
    }

    /**
     * Sync categories (used for manual refresh)
     */
    suspend fun syncCategories(sessionToken: String): SafeApiResult<List<Category>> {
        return refreshCategories(sessionToken)
    }
}