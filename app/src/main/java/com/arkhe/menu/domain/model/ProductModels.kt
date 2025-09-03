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
    val info: ProductInfo
)

@Serializable
data class ProductData(
    val id: String,
    val productCategoryId: String,
    val categoryName: String,
    val categoryType: String,
    val productCode: String,
    val productFullName: String,
    val productDestination: String,
    val status: String,
    val informationId: String,
    val informationEn: String
)

@Serializable
data class ProductInfo(
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
    val productDestination: String,
    val status: String,
    val information: ProductInformation,
    val actionInfo: ProductActionInfo
) {
    /*Extract series prefix from productFullName (e.g., "Chipkeun" from "Chipkeun #01")*/
    val seriesPrefix: String
        get() {
            val fullName = productFullName
            val hashIndex = fullName.indexOf('#')
            return if (hashIndex > 0) {
                fullName.substring(0, hashIndex).trim()
            } else {
                // Fallback to productCategoryId or first word
                productCategoryId.ifEmpty {
                    fullName.split(" ").firstOrNull() ?: fullName
                }
            }
        }
}

data class ProductInformation(
    val indonesian: String,
    val english: String
)

data class ProductActionInfo(
    val action: String,
    val information: ProductInformation
)

/*UI grouping model*/
data class ProductGroup(
    val seriesName: String,
    val products: List<Product>
)