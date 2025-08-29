package com.arkhe.menu.data.repository

import android.util.Log
import com.arkhe.menu.data.local.LocalDataSource
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.mapper.toDomainList
import com.arkhe.menu.data.mapper.toEntityList
import com.arkhe.menu.data.remote.RemoteDataSource
import com.arkhe.menu.data.remote.api.NetworkErrorHandler
import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.NetworkException
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take

class ProfileRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ProfileRepository {

    companion object {
        private const val TAG = "ProfileRepository"
    }

    override suspend fun getProfiles(
        sessionToken: String,
        forceRefresh: Boolean
    ): Flow<ApiResult<List<Profile>>> = flow {
        Log.d(TAG, "getProfiles called - forceRefresh: $forceRefresh, token: $sessionToken")
        emit(ApiResult.Loading)

        // Emit cached data first if available and not forcing refresh
        if (!forceRefresh) {
            try {
                Log.d(TAG, "Attempting to load cached data...")
                localDataSource.getAllProfiles()
                    .take(1)
                    .collect { entities ->
                        Log.d(TAG, "Found ${entities.size} cached profiles")
                        if (entities.isNotEmpty()) {
                            emit(ApiResult.Success(entities.toDomainList()))
                        }
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to load cached data: ${e.message}")
            }
        }

        // Fetch fresh data from remote
        Log.d(TAG, "Fetching data from remote API...")
        when (val remoteResult = remoteDataSource.getProfiles(sessionToken)) {
            is ApiResult.Success -> {
                Log.d(
                    TAG,
                    "Remote API success - status: ${remoteResult.data.status}, message: ${remoteResult.data.message}"
                )
                Log.d(TAG, "Data count: ${remoteResult.data.data.size}")

                when {
                    remoteResult.data.status == "success" && remoteResult.data.data.isNotEmpty() -> {
                        Log.d(TAG, "Processing successful response with data")
                        try {
                            // Save to local database
                            val entities = remoteResult.data.toEntityList()
                            Log.d(TAG, "Saving ${entities.size} entities to local database")
                            localDataSource.deleteAllProfiles()
                            localDataSource.insertProfiles(entities)

                            // Emit fresh data
                            emit(ApiResult.Success(remoteResult.data.toDomainList()))
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to save to local database: ${e.message}", e)
                            // Even if local save fails, still return remote data
                            emit(ApiResult.Success(remoteResult.data.toDomainList()))
                        }
                    }

                    remoteResult.data.status == "debug" -> {
                        Log.w(TAG, "API returned debug response: ${remoteResult.data.message}")
                        emit(ApiResult.Error(Exception("Debug: ${remoteResult.data.message}")))
                    }

                    remoteResult.data.data.isEmpty() -> {
                        Log.w(TAG, "API returned empty data array")
                        emit(ApiResult.Error(Exception("API returned empty data array. Status: ${remoteResult.data.status}, Message: ${remoteResult.data.message}")))
                    }

                    else -> {
                        Log.w(TAG, "API returned unsuccessful status: ${remoteResult.data.status}")
                        emit(ApiResult.Error(Exception("API error - Status: ${remoteResult.data.status}, Message: ${remoteResult.data.message}")))
                    }
                }
            }

            is ApiResult.Error -> {
                Log.e(TAG, "Remote API error: ${remoteResult.exception.message}")
                // If we have cached data and remote fails, don't emit error unless forced refresh
                val hasLocalData = try {
                    localDataSource.hasProfiles()
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to check local data: ${e.message}")
                    false
                }

                Log.d(TAG, "Has local data: $hasLocalData, force refresh: $forceRefresh")

                if (forceRefresh || !hasLocalData) {
                    val errorMessage = if (remoteResult.exception is NetworkException) {
                        NetworkErrorHandler.getErrorMessage(remoteResult.exception)
                    } else {
                        remoteResult.exception.message ?: "Unknown error occurred"
                    }
                    Log.e(TAG, "Emitting error: $errorMessage")
                    emit(ApiResult.Error(Exception(errorMessage)))
                }
            }

            ApiResult.Loading -> {
                Log.w(TAG, "Remote API returned Loading state - this shouldn't happen")
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProfile(nameShort: String): Profile? {
        return try {
            Log.d(TAG, "Getting profile for nameShort: $nameShort")
            val profile = localDataSource.getProfile(nameShort)?.toDomain()
            Log.d(TAG, "Found profile: ${profile?.nameLong}")
            profile
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get profile: ${e.message}", e)
            null
        }
    }

    override suspend fun refreshProfiles(sessionToken: String): ApiResult<List<Profile>> {
        Log.d(TAG, "refreshProfiles called with token: $sessionToken")
        return when (val remoteResult = remoteDataSource.getProfiles(sessionToken)) {
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
                            localDataSource.deleteAllProfiles()
                            localDataSource.insertProfiles(entities)

                            ApiResult.Success(remoteResult.data.toDomainList())
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to save refreshed data: ${e.message}", e)
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