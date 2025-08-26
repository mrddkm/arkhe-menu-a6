package com.arkhe.menu.data.repository

import com.arkhe.menu.data.local.LocalDataSource
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.mapper.toDomainList
import com.arkhe.menu.data.mapper.toEntityList
import com.arkhe.menu.data.remote.RemoteDataSource
import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ProfileRepository {

    override suspend fun getProfiles(
        sessionToken: String,
        forceRefresh: Boolean
    ): Flow<ApiResult<List<Profile>>> = flow {
        emit(ApiResult.Loading)

        try {
            // Emit cached data first if available and not forcing refresh
            if (!forceRefresh && localDataSource.hasProfiles()) {
                localDataSource.getAllProfiles()
                    .map { entities ->
                        entities.toDomainList()
                    }
                    .collect { profiles ->
                        if (profiles.isNotEmpty()) {
                            emit(ApiResult.Success(profiles))
                        }
                    }
            }

            // Fetch fresh data from remote
            val remoteResult = refreshProfiles(sessionToken)
            when (remoteResult) {
                is ApiResult.Success -> {
                    emit(ApiResult.Success(remoteResult.data))
                }
                is ApiResult.Error -> {
                    // If we have cached data and remote fails, don't emit error
                    if (!localDataSource.hasProfiles()) {
                        emit(ApiResult.Error(remoteResult.exception))
                    }
                }
                ApiResult.Loading -> {
                    // Should not happen in refreshProfiles
                }
            }
        } catch (e: Exception) {
            // If we have cached data, don't emit error
            if (!localDataSource.hasProfiles()) {
                emit(ApiResult.Error(e))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProfile(nameShort: String): Profile? {
        return withContext(Dispatchers.IO) {
            localDataSource.getProfile(nameShort)?.toDomain()
        }
    }

    override suspend fun refreshProfiles(sessionToken: String): ApiResult<List<Profile>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = remoteDataSource.getProfiles(sessionToken)

                if (response.status == "success" && response.data.isNotEmpty()) {
                    // Save to local database
                    val entities = response.data.toEntityList()
                    localDataSource.deleteAllProfiles()
                    localDataSource.insertProfiles(entities)

                    // Return domain models
                    ApiResult.Success(response.data.toDomainList())
                } else {
                    ApiResult.Error(Exception("API returned empty data or failed status"))
                }
            } catch (e: Exception) {
                ApiResult.Error(e)
            }
        }
    }
}