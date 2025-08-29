package com.arkhe.menu.domain.usecase.profile

import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetProfilesUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        sessionToken: String,
        forceRefresh: Boolean = false
    ): Flow<ApiResult<List<Profile>>> {
        return repository.getProfiles(sessionToken, forceRefresh)
    }
}