package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActivationRequestDto(
    val step: String,
    val userId: String? = null,
    val mail: String? = null,
    val phone: String? = null,
    val activationCode: String? = null,
    val newPassword: String? = null,
    val sessionActivation: String? = null,
    val isPinActive: Boolean? = null,
    val deviceId: String? = null,
    val manufacturer: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val device: String? = null,
    val product: String? = null,
    val osVersion: String? = null,
    val sdkLevel: String? = null,
    val securityPatch: String? = null,
    val deviceType: String? = null,
    val appVersionName: String? = null,
    val appVersionCode: String? = null
)

@Serializable
data class ActivationResponseDto(
    val status: String,
    val message: String,
    val data: ActivationStepDataDto? = null
)

@Serializable
data class ActivationStepDataDto(
    val userId: String? = null,
    val name: String? = null,
    val sessionActivation: String? = null,
    val sessionActivationAt: String? = null,
    val isPasswordCreated: Boolean? = null,
    var user: User? = null,
    var userProfile: UserProfile? = null,
    var devices: ArrayList<Devices>? = null
)