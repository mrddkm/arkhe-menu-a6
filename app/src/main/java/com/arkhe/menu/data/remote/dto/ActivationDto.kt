package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerificationRequestDto(
    val userId: String,
    val mail: String,
    val phone: String
)

@Serializable
data class VerificationResponseDto(
    val status: String,
    val message: String,
    val data: VerificationDataDto,
    val info: VerificationInfoDto
)

@Serializable
data class VerificationDataDto(
    val userId: String,
    val name: String,
)

@Serializable
data class VerificationInfoDto(
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String,
) {
    companion object {
        fun parseError() = InfoDataDto(
            action = "parse_error",
            actionInformationId = "JSON parsing failed",
            actionInformationEn = "JSON parsing failed"
        )

        fun networkError() = InfoDataDto(
            action = "network_error",
            actionInformationId = "Network request failed",
            actionInformationEn = "Network request failed"
        )
    }
}