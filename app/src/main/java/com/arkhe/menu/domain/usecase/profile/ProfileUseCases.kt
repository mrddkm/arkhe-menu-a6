package com.arkhe.menu.domain.usecase.profile

import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetProfilesUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        sessionToken: String,
        forceRefresh: Boolean = false
    ): Flow<SafeApiResult<List<Profile>>> {
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
    suspend operator fun invoke(sessionToken: String): SafeApiResult<List<Profile>> {
        return repository.refreshProfiles(sessionToken)
    }
}

class DownloadProfileImagesUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): SafeApiResult<Unit> {
        return repository.downloadProfileImages()
    }
}

class GetProfileImagePathUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(nameShort: String): String? {
        return repository.getProfileImagePath(nameShort)
    }
}

data class ProfileUseCases(
    val getProfiles: GetProfilesUseCase,
    val getProfile: GetProfileUseCase,
    val refreshProfiles: RefreshProfilesUseCase,
    val downloadProfileImages: DownloadProfileImagesUseCase,
    val getProfileImagePath: GetProfileImagePathUseCase
)