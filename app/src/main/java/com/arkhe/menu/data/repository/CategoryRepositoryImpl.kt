package com.arkhe.menu.data.repository

import android.util.Log
import com.arkhe.menu.data.local.LocalDataSource
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.mapper.toDomainList
import com.arkhe.menu.data.mapper.toEntityList
import com.arkhe.menu.data.remote.RemoteDataSource
import com.arkhe.menu.data.remote.api.NetworkErrorHandler
import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.model.NetworkException
import com.arkhe.menu.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take

class CategoryRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : CategoryRepository {

    companion object {
        private const val TAG = "CategoryRepository"
    }

    override suspend fun getCategories(
        sessionToken: String,
        forceRefresh: Boolean
    ): Flow<ApiResult<List<Category>>> = flow {
        Log.d(TAG, "getCategories called - forceRefresh: $forceRefresh, token: $sessionToken")
        emit(ApiResult.Loading)

        // Check if we have local data first
        val hasLocalData = try {
            localDataSource.hasCategories()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to check local categories data: ${e.message}")
            false
        }

        Log.d(TAG, "Has local categories data: $hasLocalData")

        // Emit cached data first if available and not forcing refresh
        if (!forceRefresh && hasLocalData) {
            try {
                Log.d(TAG, "Loading cached categories...")
                localDataSource.getAllCategories()
                    .take(1)
                    .collect { entities ->
                        Log.d(TAG, "Found ${entities.size} cached categories")
                        if (entities.isNotEmpty()) {
                            emit(ApiResult.Success(entities.toDomainList()))
                        }
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to load cached categories: ${e.message}")
            }
        }

        // Always fetch fresh data from remote if:
        // 1. Force refresh is requested, OR
        // 2. No local data exists (first install)
        if (forceRefresh || !hasLocalData) {
            Log.d(TAG, "Fetching categories from remote API... (forceRefresh: $forceRefresh, hasLocalData: $hasLocalData)")
            when (val remoteResult = remoteDataSource.getCategories(sessionToken)) {
                is ApiResult.Success -> {
                    Log.d(
                        TAG,
                        "Remote API success - status: ${remoteResult.data.status}, message: ${remoteResult.data.message}"
                    )
                    Log.d(TAG, "Categories count: ${remoteResult.data.data.size}")

                    when {
                        remoteResult.data.status == "success" && remoteResult.data.data.isNotEmpty() -> {
                            Log.d(TAG, "Processing successful response with categories data")
                            try {
                                // Save to local database
                                val entities = remoteResult.data.toEntityList()
                                Log.d(TAG, "Saving ${entities.size} category entities to local database")
                                localDataSource.deleteAllCategories()
                                localDataSource.insertCategories(entities)

                                // Emit fresh data
                                emit(ApiResult.Success(remoteResult.data.toDomainList()))
                            } catch (e: Exception) {
                                Log.e(TAG, "Failed to save categories to local database: ${e.message}", e)
                                // Even if local save fails, still return remote data
                                emit(ApiResult.Success(remoteResult.data.toDomainList()))
                            }
                        }

                        remoteResult.data.status == "debug" -> {
                            Log.w(TAG, "API returned debug response: ${remoteResult.data.message}")
                            emit(ApiResult.Error(Exception("Debug: ${remoteResult.data.message}")))
                        }

                        remoteResult.data.data.isEmpty() -> {
                            Log.w(TAG, "API returned empty categories array")
                            emit(ApiResult.Error(Exception("API returned empty categories array. Status: ${remoteResult.data.status}, Message: ${remoteResult.data.message}")))
                        }

                        else -> {
                            Log.w(TAG, "API returned unsuccessful status: ${remoteResult.data.status}")
                            emit(ApiResult.Error(Exception("API error - Status: ${remoteResult.data.status}, Message: ${remoteResult.data.message}")))
                        }
                    }
                }

                is ApiResult.Error -> {
                    Log.e(TAG, "Remote API error: ${remoteResult.exception.message}")

                    // Always emit error if it's the first install or force refresh
                    val errorMessage = if (remoteResult.exception is NetworkException) {
                        NetworkErrorHandler.getErrorMessage(remoteResult.exception)
                    } else {
                        remoteResult.exception.message ?: "Unknown error occurred"
                    }
                    Log.e(TAG, "Emitting error: $errorMessage")
                    emit(ApiResult.Error(Exception(errorMessage)))
                    // If we have local data and it's not a forced refresh, we already emitted the cached data above
                }

                ApiResult.Loading -> {
                    Log.w(TAG, "Remote API returned Loading state - this shouldn't happen")
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCategory(id: String): Category? {
        return try {
            Log.d(TAG, "Getting category for id: $id")
            val category = localDataSource.getCategory(id)?.toDomain()
            Log.d(TAG, "Found category: ${category?.name}")
            category
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get category: ${e.message}", e)
            null
        }
    }

    override suspend fun refreshCategories(sessionToken: String): ApiResult<List<Category>> {
        Log.d(TAG, "refreshCategories called with token: $sessionToken")
        return when (val remoteResult = remoteDataSource.getCategories(sessionToken)) {
            is ApiResult.Success -> {
                Log.d(
                    TAG,
                    "Refresh success - status: ${remoteResult.data.status}, data count: ${remoteResult.data.data.size}"
                )

                when {
                    remoteResult.data.status == "success" && remoteResult.data.data.isNotEmpty() -> {
                        try {
                            // Save to local database
                            val entities = remoteResult.data.toEntityList()
                            localDataSource.deleteAllCategories()
                            localDataSource.insertCategories(entities)

                            ApiResult.Success(remoteResult.data.toDomainList())
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to save refreshed categories data: ${e.message}", e)
                            // Even if local save fails, still return remote data
                            ApiResult.Success(remoteResult.data.toDomainList())
                        }
                    }

                    remoteResult.data.status == "debug" -> {
                        ApiResult.Error(Exception("Debug response: ${remoteResult.data.message}"))
                    }

                    else -> {
                        ApiResult.Error(Exception("Refresh failed - Status: ${remoteResult.data.status}, Message: ${remoteResult.data.message}"))
                    }
                }
            }

            is ApiResult.Error -> {
                Log.e(TAG, "Refresh error: ${remoteResult.exception.message}")
                val errorMessage = if (remoteResult.exception is NetworkException) {
                    NetworkErrorHandler.getErrorMessage(remoteResult.exception)
                } else {
                    remoteResult.exception.message ?: "Unknown error occurred"
                }
                ApiResult.Error(Exception(errorMessage))
            }

            ApiResult.Loading -> {
                Log.w(TAG, "Refresh returned Loading state - this shouldn't happen")
                ApiResult.Error(Exception("Unexpected loading state"))
            }
        }
    }
}