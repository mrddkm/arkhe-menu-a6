package com.arkhe.menu.domain.repository

import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getProfiles(sessionToken: String, forceRefresh: Boolean = false): Flow<ApiResult<List<Profile>>>
    suspend fun getProfile(nameShort: String): Profile?
    suspend fun refreshProfiles(sessionToken: String): ApiResult<List<Profile>>
}