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
            Log.d(TAG, "🌐 Starting fetchRemoteEntities with token: $token")
            val response = api.getCategories(token)

            Log.d(TAG, "📡 API Response received:")
            Log.d(TAG, "  Status: ${response.status}")
            Log.d(TAG, "  Message: ${response.message}")
            Log.d(TAG, "  Data count: ${response.data.size}")

            val entities = response.toEntityList()
            Log.d(TAG, "🔄 Mapped to ${entities.size} entities")

            // Log sample entity untuk debug
            if (entities.isNotEmpty()) {
                val sample = entities.first()
                Log.d(TAG, "📝 Sample entity: id=${sample.id}, name=${sample.name}")
            }

            entities
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error in fetchRemoteEntities: ${e.message}", e)
            throw e
        }
    }


    override suspend fun isEmpty(): Boolean {
        val count = dao.getCategoryCount()
        Log.d(TAG, "📊 isEmpty check: count = $count")
        return count == 0
    }

    // 🔧 SIMPLIFIED getCategories - lebih straightforward
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

                val syncResult = sync(sessionToken)
                emit(syncResult)
            } else {
                // Offline-first flow
                Log.d(TAG, "💾 Starting offline-first flow...")

                // First, emit local data
                super.getAll().collect { localCategories ->
                    Log.d(TAG, "💾 Local categories: ${localCategories.size}")

                    if (localCategories.isNotEmpty()) {
                        Log.d(TAG, "✅ Emitting local data")
                        emit(SafeApiResult.Success(localCategories))
                    } else {
                        Log.d(TAG, "📭 No local data, starting auto-sync...")
                        emit(SafeApiResult.Loading)

                        // Auto-sync if no local data
                        val syncResult = sync(sessionToken)
                        emit(syncResult)
                    }
                }
            }
        }
    }

    override suspend fun getCategory(id: String): Category? {
        Log.d(TAG, "🔍 Getting category by id: $id")
        return dao.getCategory(id)?.toDomain()
    }

    override suspend fun refreshCategories(sessionToken: String): SafeApiResult<List<Category>> {
        Log.d(TAG, "🔄 refreshCategories called")
        return super.sync(sessionToken)
    }

    private suspend fun debugLocalData() {
        try {
            val count = dao.getCategoryCount()
            val categories = dao.getAllCategories()
            Log.d(TAG, "🐛 Direct DB check - Count: $count")

            categories.collect { entities ->
                Log.d(TAG, "🐛 Direct DB flow - Entities: ${entities.size}")
                entities.take(3).forEach { entity ->
                    Log.d(TAG, "🐛 Sample entity: ${entity.id} - ${entity.name}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "🐛 Error checking DB: ${e.message}", e)
        }
    }
}