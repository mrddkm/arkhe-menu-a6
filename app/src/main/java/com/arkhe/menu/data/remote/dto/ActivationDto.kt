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

@Serializable
data class User(
    var id: String? = null,
    var userId: String? = null,
    var profileId: String? = null,
    var lastPasswordAt: String? = null,
    var isPinActive: Boolean? = null,
    var lastPinAt: String? = null,
    var isBiometricActive: Boolean? = null,
    var lastBiometricAt: String? = null,
    var activationCode: String? = null,
    var initial: String? = null,
    var nickName: String? = null,
    var alias: String? = null,
    var positionId: String? = null,
    var positionInitial: String? = null,
    var positionName: String? = null,
    var divisionId: String? = null,
    var divisionInitial: String? = null,
    var divisionName: String? = null,
    var roles: String? = null,
    var isSobatJalan: Boolean? = null,
    var isSobatKonten: Boolean? = null,
    var isSobatCerita: Boolean? = null,
    var isStatusActive: Boolean? = null,
    var sessionActivation: String? = null,
    var sessionActivationAt: String? = null,
    var sessionToken: String? = null,
    var sessionExpiredAt: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null,
    var lastActiveAt: String? = null
)

@Serializable
data class UserProfile(
    var profileId: String? = null,
    var isUser: Boolean? = null,
    var phone: String? = null,
    var mail: String? = null,
    var name: String? = null,
    var photo: String? = null,
    var birthday: String? = null,
    var gender: String? = null,
    var instagram: String? = null,
    var createdAt: String? = null,
    var lastAt: String? = null
)

@Serializable
data class Devices(
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