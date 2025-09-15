package com.arkhe.menu.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryRequest(
    val sessionToken: String
)

@Serializable
data class CategoryResponse(
    val status: String,
    val message: String,
    val data: List<CategoryData>,
    val info: CategoryInfoData
)

@Serializable
data class CategoryData(
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
data class CategoryInfoData(
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String
)

/*Domain model for UI*/
data class Category(
    val id: String,
    val name: String,
    val type: String,
    val productCount: Int,
    val initiation: Int,
    val research: Int,
    val ready: Int,
    val information: CategoryInformationLanguage,
    val colors: CategoryColors,
    val actionInfo: CategoryActionInfo
)

data class CategoryColors(
    val backgroundColor: String,
    val iconColor: String
)

data class CategoryActionInfo(
    val action: String,
    val information: CategoryInformationLanguage
)

data class CategoryInformationLanguage(
    val indonesian: String,
    val english: String
)