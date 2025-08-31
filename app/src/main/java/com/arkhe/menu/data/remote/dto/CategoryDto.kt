package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryRequestDto(
    val sessionToken: String
)

@Serializable
data class CategoryResponseDto(
    val status: String,
    val message: String,
    val data: List<CategoryDataDto>,
    val info: CategoryInfoDto
)

@Serializable
data class CategoryDataDto(
    val id: String,
    val name: String,
    val type: String,
    val productCount: String,
    val initiation: String,
    val research: String,
    val ready: String,
    val informationId: String,
    val informationEn: String,
    val colorBackground: String,
    val colorIcon: String
)

@Serializable
data class CategoryInfoDto(
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String
) {
    companion object {
        fun empty() = CategoryInfoDto(
            action = "",
            actionInformationId = "",
            actionInformationEn = ""
        )

        fun parseError() = CategoryInfoDto(
            action = "parse_error",
            actionInformationId = "JSON parsing failed",
            actionInformationEn = "JSON parsing failed"
        )

        fun networkError() = CategoryInfoDto(
            action = "network_error",
            actionInformationId = "Network request failed",
            actionInformationEn = "Network request failed"
        )
    }
}