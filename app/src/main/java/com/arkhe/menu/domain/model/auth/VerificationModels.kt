package com.arkhe.menu.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthVerificationRequest(
    val userId: String,
    val mail: String,
    val phone: String
)

@Serializable
data class AuthVerificationResponse(
    val status: String,
    val message: String,
    val data: VerificationData,
    val info: VerificationInfoData
)

@Serializable
data class VerificationData(
    val userId: String,
    val name: String,
)

@Serializable
data class VerificationInfoData(
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String
)

/*Domain model for UI*/
data class Verification(
    val userId: String,
    val name: String,
)