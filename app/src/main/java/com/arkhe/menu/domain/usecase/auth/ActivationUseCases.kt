package com.arkhe.menu.domain.usecase.auth

import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.auth.Verification
import com.arkhe.menu.domain.repository.AuthRepository

class VerificationUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        userId: String,
        phone: String,
        mail: String
    ): SafeApiResult<Verification> {
        return repository.verification(userId, phone, mail)
    }
}

data class ActivationUseCases(
    val verification: VerificationUseCase
)