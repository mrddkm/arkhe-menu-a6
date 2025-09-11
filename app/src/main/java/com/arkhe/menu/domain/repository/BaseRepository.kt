@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.domain.repository

import android.util.Log
import com.arkhe.menu.data.remote.api.SafeApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

abstract class BaseRepository<Entity, Domain>(
    private val daoFlow: Flow<List<Entity>>,
    private val insertEntities: suspend (List<Entity>) -> Unit,
    private val clearEntities: suspend () -> Unit,
    private val mapperToDomain: (Entity) -> Domain
) {
    companion object {
        private const val TAG = "BaseRepository"
    }

    protected abstract suspend fun fetchRemoteEntities(token: String): List<Entity>

    fun getAll(): Flow<List<Domain>> =
        daoFlow.map { entities ->
            Log.d(TAG, "💾 BaseRepository.getAll() - Local entities count: ${entities.size}")
            val domains = entities.map(mapperToDomain)
            Log.d(TAG, "💾 BaseRepository.getAll() - Mapped to ${domains.size} domain objects")
            domains
        }

    suspend fun sync(token: String): SafeApiResult<List<Domain>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🔄 BaseRepository.sync() - Starting with token: $token")

                val remoteEntities = fetchRemoteEntities(token)
                Log.d(TAG, "🌐 BaseRepository.sync() - Fetched ${remoteEntities.size} entities from remote")

                Log.d(TAG, "🗑️ BaseRepository.sync() - Clearing local data...")
                clearEntities()

                Log.d(TAG, "💾 BaseRepository.sync() - Inserting ${remoteEntities.size} entities...")
                insertEntities(remoteEntities)

                val domainObjects = remoteEntities.map(mapperToDomain)
                Log.d(TAG, "✅ BaseRepository.sync() - Successfully synced ${domainObjects.size} items")

                SafeApiResult.Success(domainObjects)
            } catch (e: Exception) {
                Log.e(TAG, "❌ BaseRepository.sync() - Failed: ${e.message}", e)
                SafeApiResult.Error(e)
            }
        }
    }

    @Suppress("UNUSED")
    open suspend fun isEmpty(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                false
            } catch (e: Exception) {
                Log.e(TAG, "Error checking if empty: ${e.message}")
                true
            }
        }
    }
}