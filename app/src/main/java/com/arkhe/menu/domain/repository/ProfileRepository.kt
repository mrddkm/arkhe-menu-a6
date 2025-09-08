package com.arkhe.menu.domain.repository

import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getProfiles(
        sessionToken: String,
        forceRefresh: Boolean = false
    ): Flow<SafeApiResult<List<Profile>>>

    suspend fun getProfile(nameShort: String): Profile?
    suspend fun refreshProfiles(sessionToken: String): SafeApiResult<List<Profile>>
    suspend fun downloadProfileImages(): SafeApiResult<Unit>
    suspend fun getProfileImagePath(nameShort: String): String?
}