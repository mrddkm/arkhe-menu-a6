package com.arkhe.menu.data.mapper

import com.arkhe.menu.data.local.entity.ProductEntity
import com.arkhe.menu.data.remote.dto.ProductDataDto
import com.arkhe.menu.data.remote.dto.ProductInfoDto
import com.arkhe.menu.data.remote.dto.ProductResponseDto
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductActionInfo
import com.arkhe.menu.domain.model.ProductInformationLanguage

/*DTO to Entity*/
fun ProductDataDto.toEntity(infoData: ProductInfoDto): ProductEntity {
    return ProductEntity(
        id = id,
        productCategoryId = productCategoryId,
        categoryName = categoryName,
        categoryType = categoryType,
        productCode = productCode,
        productFullName = productFullName,
        productTagLine = productTagLine,
        productDestination = productDestination,
        logo = logo,
        status = status,
        hikeDistance = hikeDistance,
        hikeDuration = hikeDuration,
        hikeElevationGain = hikeElevationGain,
        hikeAltitude = hikeAltitude,
        hikeLevelId = hikeLevelId,
        hikeLevelName = hikeLevelName,
        hikeLevelInformationId = hikeLevelInformationId,
        hikeLevelInformationEn = hikeLevelInformationEn,
        informationId = informationId,
        informationEn = informationEn,
        action = infoData.action,
        actionInformationId = infoData.actionInformationId,
        actionInformationEn = infoData.actionInformationEn
    )
}

/*Entity to Domain*/
fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        productCategoryId = productCategoryId,
        categoryName = categoryName,
        categoryType = categoryType,
        productCode = productCode,
        productFullName = productFullName,
        productTagLine = productTagLine,
        productDestination = productDestination,
        logo = logo,
        status = status,
        hikeDistance = hikeDistance,
        hikeDuration = hikeDuration,
        hikeElevationGain = hikeElevationGain,
        hikeAltitude = hikeAltitude,
        hikeLevelId = hikeLevelId,
        hikeLevelName = hikeLevelName,
        hikeLevelInformation = ProductInformationLanguage(
            indonesian = hikeLevelInformationId,
            english = hikeLevelInformationEn
        ),
        information = ProductInformationLanguage(
            indonesian = informationId,
            english = informationEn
        ),
        actionInfo = ProductActionInfo(
            action = action,
            information = ProductInformationLanguage(
                indonesian = actionInformationId,
                english = actionInformationEn
            )
        ),
        localImagePath = localImagePath
    )
}

/*DTO to Domain (direct conversion)*/
fun ProductDataDto.toDomain(infoData: ProductInfoDto): Product {
    return Product(
        id = id,
        productCategoryId = productCategoryId,
        categoryName = categoryName,
        categoryType = categoryType,
        productCode = productCode,
        productFullName = productFullName,
        productTagLine = productTagLine,
        productDestination = productDestination,
        logo = logo,
        status = status,
        hikeDistance = hikeDistance,
        hikeDuration = hikeDuration,
        hikeElevationGain = hikeElevationGain,
        hikeAltitude = hikeAltitude,
        hikeLevelId = hikeLevelId,
        hikeLevelName = hikeLevelName,
        hikeLevelInformation = ProductInformationLanguage(
            indonesian = hikeLevelInformationId,
            english = hikeLevelInformationEn
        ),
        information = ProductInformationLanguage(
            indonesian = informationId,
            english = informationEn
        ),
        actionInfo = ProductActionInfo(
            action = infoData.action,
            information = ProductInformationLanguage(
                indonesian = infoData.actionInformationId,
                english = infoData.actionInformationEn
            )
        )
    )
}

/*List extensions with ProductResponseDto*/
fun ProductResponseDto.toEntityList(): List<ProductEntity> {
    return this.data.map { it.toEntity(this.info) }
}

@JvmName("toDomainListFromEntity")
fun List<ProductEntity>.toDomainList(): List<Product> {
    return this.map { it.toDomain() }
}

@JvmName("toDomainListFromResponse")
fun ProductResponseDto.toDomainList(): List<Product> {
    return this.data.map { it.toDomain(this.info) }
}