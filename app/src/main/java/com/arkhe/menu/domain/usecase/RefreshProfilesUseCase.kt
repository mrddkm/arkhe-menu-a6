package com.arkhe.menu.domain.usecase

import com.arkhe.menu.domain.model.ApiResult
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.repository.ProfileRepository

class RefreshProfilesUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(sessionToken: String): ApiResult<List<Profile>> {
        return repository.refreshProfiles(sessionToken)
    }
}