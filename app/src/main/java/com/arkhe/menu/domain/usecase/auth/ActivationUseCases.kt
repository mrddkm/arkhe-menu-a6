package com.arkhe.menu.domain.usecase.auth

import com.arkhe.menu.data.remote.api.Resource
import com.arkhe.menu.domain.model.auth.ActivationResponse
import com.arkhe.menu.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ActivationUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        step: String,
        userId: String?,
        mail: String?,
        phone: String?,
        activationCode: String?,
        newPassword: String?,
        sessionActivation: String?,
        isPinActive: Boolean?
    ): Flow<Resource<ActivationResponse>> {
        return authRepository.performActivation(
            step = step,
            userId = userId,
            mail = mail,
            phone = phone,
            activationCode = activationCode,
            newPassword = newPassword,
            sessionActivation = sessionActivation,
            isPinActive = isPinActive
        )
    }
}

data class ActivationUseCases(
    val activation: ActivationUseCase
)