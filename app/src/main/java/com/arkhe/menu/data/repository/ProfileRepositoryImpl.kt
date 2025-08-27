package com.arkhe.menu.data.repository

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

    override suspend fun getProfiles(
        sessionToken: String,
        forceRefresh: Boolean
    ): Flow<ApiResult<List<Profile>>> = flow {
        emit(ApiResult.Loading)

        // Emit cached data first if available and not forcing refresh
        if (!forceRefresh) {
            try {
                localDataSource.getAllProfiles()
                    .take(1) // Take only the first emission to avoid infinite loop
                    .collect { entities ->
                        if (entities.isNotEmpty()) {
                            emit(ApiResult.Success(entities.toDomainList()))
                        }
                    }
            } catch (e: Exception) {
                // Ignore local database errors, continue with remote fetch
            }
        }

        // Fetch fresh data from remote
        when (val remoteResult = remoteDataSource.getProfiles(sessionToken)) {
            is ApiResult.Success -> {
                if (remoteResult.data.status == "success" && remoteResult.data.data.isNotEmpty()) {
                    try {
                        // Save to local database
                        val entities = remoteResult.data.data.toEntityList()
                        localDataSource.deleteAllProfiles()
                        localDataSource.insertProfiles(entities)

                        // Emit fresh data
                        emit(ApiResult.Success(remoteResult.data.data.toDomainList()))
                    } catch (e: Exception) {
                        // Even if local save fails, still return remote data
                        emit(ApiResult.Success(remoteResult.data.data.toDomainList()))
                    }
                } else {
                    emit(ApiResult.Error(Exception("API returned empty data or failed status")))
                }
            }

            is ApiResult.Error -> {
                // If we have cached data and remote fails, don't emit error unless forced refresh
                val hasLocalData = try {
                    localDataSource.hasProfiles()
                } catch (e: Exception) {
                    false
                }

                if (forceRefresh || !hasLocalData) {
                    val errorMessage = if (remoteResult.exception is NetworkException) {
                        NetworkErrorHandler.getErrorMessage(remoteResult.exception)
                    } else {
                        remoteResult.exception.message ?: "Unknown error occurred"
                    }
                    emit(ApiResult.Error(Exception(errorMessage)))
                }
            }

            ApiResult.Loading -> {
                // Should not happen
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProfile(nameShort: String): Profile? {
        return try {
            localDataSource.getProfile(nameShort)?.toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun refreshProfiles(sessionToken: String): ApiResult<List<Profile>> {
        return when (val remoteResult = remoteDataSource.getProfiles(sessionToken)) {
            is ApiResult.Success -> {
                if (remoteResult.data.status == "success" && remoteResult.data.data.isNotEmpty()) {
                    try {
                        // Save to local database
                        val entities = remoteResult.data.data.toEntityList()
                        localDataSource.deleteAllProfiles()
                        localDataSource.insertProfiles(entities)

                        ApiResult.Success(remoteResult.data.data.toDomainList())
                    } catch (e: Exception) {
                        // Even if local save fails, still return remote data
                        ApiResult.Success(remoteResult.data.data.toDomainList())
                    }
                } else {
                    ApiResult.Error(Exception("API returned empty data or failed status"))
                }
            }

            is ApiResult.Error -> {
                val errorMessage = if (remoteResult.exception is NetworkException) {
                    NetworkErrorHandler.getErrorMessage(remoteResult.exception)
                } else {
                    remoteResult.exception.message ?: "Unknown error occurred"
                }
                ApiResult.Error(Exception(errorMessage))
            }

            ApiResult.Loading -> {
                // Should not happen in direct API call
                ApiResult.Error(Exception("Unexpected loading state"))
            }
        }
    }
}