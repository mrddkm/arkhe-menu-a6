package com.arkhe.menu.data.repository

import android.util.Log
import com.arkhe.menu.data.local.dao.CategoryDao
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.mapper.toEntityList
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.data.remote.api.ArkheApiService
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

    companion object {
        private const val TAG = "CategoryRepositoryImpl"
    }

    override fun getCategories(
        sessionToken: String,
        forceRefresh: Boolean
    ): Flow<SafeApiResult<List<Category>>> {
        Log.d(TAG, "🎯 getCategories - forceRefresh: $forceRefresh")

        return flow {
            if (forceRefresh) {
                // Force refresh flow
                Log.d(TAG, "🔄 Starting force refresh...")
                emit(SafeApiResult.Loading)

                val syncResult = refreshCategories(sessionToken)
                emit(syncResult)
            } else {
                // Offline-first flow
                Log.d(TAG, "💾 Starting offline-first flow...")

                // First, emit local data
                dao.getAllCategories().collect { entities ->
                    val categories = entities.map { it.toDomain() }
                    Log.d(TAG, "💾 Local categories: ${categories.size}")

                    if (categories.isNotEmpty()) {
                        Log.d(TAG, "✅ Emitting local data")
                        emit(SafeApiResult.Success(categories))
                    } else {
                        Log.d(TAG, "📭 No local data, starting auto-sync...")
                        emit(SafeApiResult.Loading)

                        // Auto-sync if no local data
                        val syncResult = refreshCategories(sessionToken)
                        emit(syncResult)
                    }
                }
            }
        }
    }

    override suspend fun getCategory(id: String): Category? {
        Log.d(TAG, "🔍 Getting category by id: $id")
        return withContext(Dispatchers.IO) {
            dao.getCategory(id)?.toDomain()
        }
    }

    override suspend fun refreshCategories(sessionToken: String): SafeApiResult<List<Category>> {
        Log.d(TAG, "🔄 refreshCategories called with token: ${sessionToken.take(8)}...")

        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🌐 Fetching categories from API...")
                val response = api.getCategories(sessionToken)

                Log.d(TAG, "📡 API Response received:")
                Log.d(TAG, "  Status: ${response.status}")
                Log.d(TAG, "  Message: ${response.message}")
                Log.d(TAG, "  Data count: ${response.data.size}")

                val entities = response.toEntityList()
                Log.d(TAG, "🔄 Mapped to ${entities.size} entities")

                // Clear and insert new data
                Log.d(TAG, "🗑️ Clearing local data...")
                dao.deleteAllCategories()

                Log.d(TAG, "💾 Inserting ${entities.size} entities...")
                dao.insertCategories(entities)

                val categories = entities.map { it.toDomain() }
                Log.d(TAG, "✅ Successfully synced ${categories.size} categories")

                SafeApiResult.Success(categories)

            } catch (e: Exception) {
                Log.e(TAG, "❌ Error refreshing categories: ${e.message}", e)
                SafeApiResult.Error(e)
            }
        }
    }

    /**
     * Get categories as flow for observing changes - for ViewModel init
     */
    fun getCategoriesFlow(): Flow<List<Category>> {
        return dao.getAllCategories().map { entities ->
            val categories = entities.map { it.toDomain() }
            Log.d(TAG, "💾 Flow emission: ${categories.size} categories")
            categories
        }
    }

    /**
     * Check if local database is empty
     */
    private suspend fun isEmpty(): Boolean {
        val count = dao.getCategoryCount()
        Log.d(TAG, "📊 isEmpty check: count = $count")
        return count == 0
    }

    /**
     * Sync categories (used for manual refresh)
     */
    suspend fun syncCategories(sessionToken: String): SafeApiResult<List<Category>> {
        Log.d(TAG, "🔄 syncCategories called")
        return refreshCategories(sessionToken)
    }
}