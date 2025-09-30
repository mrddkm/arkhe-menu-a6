@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductRequest(
    val sessionToken: String,
    val productCategoryId: String
)

@Serializable
data class ProductResponse(
    val status: String,
    val message: String,
    val data: List<ProductData>,
    val info: ProductInfoData
)

@Serializable
data class ProductData(
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
data class ProductInfoData(
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String
)

/*Domain model for UI*/
data class Product(
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
    val hikeLevelInformation: ProductInformationLanguage,
    val information: ProductInformationLanguage,
    val actionInfo: ProductActionInfo,
    val localImagePath: String? = null
) {
    val seriesPrefix: String
        get() {
            val fullName = productFullName
            val hashIndex = fullName.indexOf('#')
            return if (hashIndex > 0) {
                fullName.substring(0, hashIndex).trim()
            } else {
                productFullName
            }
        }
}

/*UI grouping model*/
data class ProductByGroup(
    val seriesName: String,
    val products: List<Product>
)

data class ProductByCategory(
    val name: String,
    val products: List<Product>
)

data class ProductByType(
    val type: String,
    val products: List<Product>
)

data class ProductActionInfo(
    val action: String,
    val information: ProductInformationLanguage
)

data class ProductStatistics(
    val total: Int,
    val ready: Int,
    val research: Int,
    val initiation: Int
)

data class ProductInformationLanguage(
    val indonesian: String,
    val english: String
)