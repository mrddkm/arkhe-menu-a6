package com.arkhe.menu.data.repository

import android.util.Log
import com.arkhe.menu.data.local.LocalDataSource
import com.arkhe.menu.data.local.storage.ImageStorageManager
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.mapper.toDomainList
import com.arkhe.menu.data.mapper.toEntityList
import com.arkhe.menu.data.remote.RemoteDataSource
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take

class ProfileRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val imageStorageManager: ImageStorageManager
) : ProfileRepository {

    companion object {
        private const val TAG = "ProfileRepository"
    }

    override suspend fun getProfiles(
        sessionToken: String,
        forceRefresh: Boolean
    ): Flow<SafeApiResult<List<Profile>>> = flow {
        emit(SafeApiResult.Loading)

        var cachedEmitted = false
        if (!forceRefresh) {
            try {
                localDataSource.getAllProfiles()
                    .take(1)
                    .collect { entities ->
                        if (entities.isNotEmpty()) {
                            emit(SafeApiResult.Success(entities.toDomainList()))
                            cachedEmitted = true
                        }
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to load cached data: ${e.message}")
            }
        }

        if (forceRefresh || !cachedEmitted) {
            val result = syncProfiles(sessionToken)
            emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProfile(nameShort: String): Profile? {
        return try {
            val profile = localDataSource.getProfile(nameShort)?.toDomain()
            profile
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun refreshProfiles(sessionToken: String): SafeApiResult<List<Profile>> {
        return syncProfiles(sessionToken)
    }

    override suspend fun syncProfiles(sessionToken: String): SafeApiResult<List<Profile>> {
        return when (
            val remoteResult = remoteDataSource.getProfiles(sessionToken)
        ) {
            is SafeApiResult.Success -> {
                if (remoteResult.data.status == "success" && remoteResult.data.data.isNotEmpty()) {
                    try {
                        val entities = remoteResult.data.toEntityList().map { entity ->
                            val fileName = "profile_${entity.nameShort}_logo"
                            val localPath = if (entity.logo.isNotEmpty()) {
                                imageStorageManager.downloadAndSaveImage(entity.logo, fileName)
                            } else null
                            entity.copy(localImagePath = localPath)
                        }
                        localDataSource.deleteAllProfiles()
                        localDataSource.insertProfiles(entities)
                        SafeApiResult.Success(entities.toDomainList())
                    } catch (e: Exception) {
                        SafeApiResult.Failed(e)
                    }
                } else {
                    SafeApiResult.Failed(Exception("No data returned from API"))
                }
            }

            is SafeApiResult.Failed -> SafeApiResult.Failed(remoteResult.exception)
            SafeApiResult.Loading -> SafeApiResult.Failed(Exception("Unexpected loading state"))
        }
    }
}