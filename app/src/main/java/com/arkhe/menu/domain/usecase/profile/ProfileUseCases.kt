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

class GetProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(nameShort: String): Profile? {
        return repository.getProfile(nameShort)
    }
}

class RefreshProfilesUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(sessionToken: String): ApiResult<List<Profile>> {
        return repository.refreshProfiles(sessionToken)
    }
}

data class ProfileUseCases(
    val getProfiles: GetProfilesUseCase,
    val getProfile: GetProfileUseCase,
    val refreshProfiles: RefreshProfilesUseCase
)