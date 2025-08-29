package com.arkhe.menu.domain.usecase.profile

import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(nameShort: String): Profile? {
        return repository.getProfile(nameShort)
    }
}