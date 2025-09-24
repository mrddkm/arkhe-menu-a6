package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductRequestDto(
    val sessionToken: String,
    val productCategoryId: String
)

@Serializable
data class ProductResponseDto(
    val status: String,
    val message: String,
    val data: List<ProductDataDto>,
    val info: ProductInfoDto
)

@Serializable
data class ProductDataDto(
    val id: String,
    val productCategoryId: String,
    val categoryName: String,
    val categoryType: String,
    val productCode: String,
    val productFullName: String,
    val productTagLine: String,
    val productDestination: String,
    val logo: String,
    val status: String,
    val hikeDistance: String,
    val hikeDuration: String,
    val hikeElevationGain: String,
    val hikeAltitude: String,
    val hikeLevelId: String,
    val hikeLevelName: String,
    val hikeLevelInformationId: String,
    val hikeLevelInformationEn: String,
    val informationId: String,
    val informationEn: String
)

@Serializable
data class ProductInfoDto(
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String
) {
    companion object {
        fun parseError() = ProductInfoDto(
            action = "parse_error",
            actionInformationId = "JSON parsing failed",
            actionInformationEn = "JSON parsing failed"
        )

        fun networkError() = ProductInfoDto(
            action = "network_error",
            actionInformationId = "Network request failed",
            actionInformationEn = "Network request failed"
        )
    }
}