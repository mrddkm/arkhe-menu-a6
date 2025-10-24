package com.arkhe.menu.data.repository

import com.arkhe.menu.data.remote.dto.ActivationResponseDto

interface ActivationService {
    suspend fun performActivation(
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
    ): ActivationResponseDto
}
