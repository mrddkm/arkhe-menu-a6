package com.arkhe.menu.data.repository

import android.util.Log
import com.arkhe.menu.data.local.dao.CategoryDao
import com.arkhe.menu.data.local.entity.CategoryEntity
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.mapper.toEntityList
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.data.remote.api.TripkeunApiService
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.repository.BaseRepository
import com.arkhe.menu.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val api: TripkeunApiService,
    private val dao: CategoryDao
) : BaseRepository<CategoryEntity, Category>(
    daoFlow = dao.getAllCategories(),
    insertEntities = { dao.insertCategories(it) },
    clearEntities = { dao.deleteAllCategories() },
    mapperToDomain = { entity -> entity.toDomain() }
), CategoryRepository {

    companion object {
        private const val TAG = "CategoryRepositoryImpl"
    }

    override suspend fun fetchRemoteEntities(token: String): List<CategoryEntity> {
        return try {
            Log.d(TAG, "Fetching categories from remote API...")
            val response = api.getCategories(token)

            Log.d(TAG, "API Response - Status: ${response.status}, Message: ${response.message}")
            Log.d(TAG, "API Response - Data count: ${response.data.size}")

            val entities = response.toEntityList()
            Log.d(TAG, "Successfully mapped ${entities.size} categories to entities")

            entities
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching categories from remote: ${e.message}", e)
            throw e
        }
    }

    override suspend fun isEmpty(): Boolean {
        return dao.getCategoryCount() == 0
    }

    override fun getCategories(
        sessionToken: String,
        forceRefresh: Boolean
    ): Flow<SafeApiResult<List<Category>>> {
        return if (forceRefresh) {
            Log.d(TAG, "Force refresh requested - syncing from remote")
            flow {
                emit(SafeApiResult.Loading)
                val syncResult = sync(sessionToken)
                emit(syncResult)
            }
        } else {
            Log.d(TAG, "Normal request - returning local data (offline-first)")
            super.getAll().map { categories ->
                Log.d(TAG, "Local categories found: ${categories.size}")
                if (categories.isNotEmpty()) {
                    SafeApiResult.Success(categories)
                } else {
                    Log.d(TAG, "No local data found - returning Loading state")
                    SafeApiResult.Loading
                }
            }
        }
    }

    override suspend fun getCategory(id: String): Category? {
        Log.d(TAG, "Getting category by id: $id")
        return dao.getCategory(id)?.toDomain()
    }

    override suspend fun refreshCategories(sessionToken: String): SafeApiResult<List<Category>> {
        Log.d(TAG, "Refreshing categories...")
        return super.sync(sessionToken)
    }
}