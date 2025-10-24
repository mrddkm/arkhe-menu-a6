package com.arkhe.menu.domain.usecase.auth

import com.arkhe.menu.data.remote.api.SafeResourceResult
import com.arkhe.menu.domain.model.auth.ActivationResponse
import com.arkhe.menu.domain.model.auth.SignInResponse
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
        isPinActive: Boolean?,
        deviceId: String?,
        manufacturer: String?,
        brand: String?,
        model: String?,
        device: String?,
        product: String?,
        osVersion: String?,
        sdkLevel: String?,
        securityPatch: String?,
        deviceType: String?,
        appVersionName: String?,
        appVersionCode: String?
    ): Flow<SafeResourceResult<ActivationResponse>> {
        return authRepository.performActivationStep(
            step = step,
            userId = userId,
            mail = mail,
            phone = phone,
            activationCode = activationCode,
            newPassword = newPassword,
            sessionActivation = sessionActivation,
            isPinActive = isPinActive,
            deviceId = deviceId,
            manufacturer = manufacturer,
            brand = brand,
            model = model,
            device = device,
            product = product,
            osVersion = osVersion,
            sdkLevel = sdkLevel,
            securityPatch = securityPatch,
            deviceType = deviceType,
            appVersionName = appVersionName,
            appVersionCode = appVersionCode
        )
    }
}

class SignInUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        sessionActivation: String,
        userId: String,
        password: String
    ): Flow<SafeResourceResult<SignInResponse>> {
        return authRepository.signIn(sessionActivation, userId, password)
    }
}

data class ActivationUseCases(
    val activationStepUseCase: ActivationUseCase,
    val signInUseCase: SignInUseCase
)