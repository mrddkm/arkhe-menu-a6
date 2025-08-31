package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileRequestDto(
    val sessionToken: String
)

@Serializable
data class ProfileResponseDto(
    val status: String,
    val message: String,
    val data: List<ProfileDataDto>,
    val info: InfoDataDto,
)

@Serializable
data class InfoDataDto(
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String,
) {
    companion object {
        @Suppress("Unused")
        fun empty() = InfoDataDto(
            action = "",
            actionInformationId = "",
            actionInformationEn = ""
        )

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

@Serializable
data class ProfileDataDto(
    val nameShort: String,
    val nameLong: String,
    val birthDate: String,
    val googleMaps: String,
    val instagram: String,
    val tiktok: String,
    val youtube: String,
    val tagline: String,
    val quotes: String,
    val informationId: String,
    val informationEn: String,
    val createdAt: String,
    val updatedAt: String,
    val createdByUserId: String,
    val updatedByUserId: String
)