package com.arkhe.menu.data.mapper

import com.arkhe.menu.data.local.entity.CategoryEntity
import com.arkhe.menu.data.remote.dto.CategoryDataDto
import com.arkhe.menu.data.remote.dto.CategoryInfoDto
import com.arkhe.menu.data.remote.dto.CategoryResponseDto
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.model.CategoryActionInfo
import com.arkhe.menu.domain.model.CategoryColors
import com.arkhe.menu.domain.model.CategoryInformation

// DTO to Entity
fun CategoryDataDto.toEntity(infoData: CategoryInfoDto): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        type = type,
        productCount = productCount,
        initiation = initiation,
        research = research,
        ready = ready,
        informationId = informationId,
        informationEn = informationEn,
        colorBackground = colorBackground,
        colorIcon = colorIcon,
        action = infoData.action,
        actionInformationId = infoData.actionInformationId,
        actionInformationEn = infoData.actionInformationEn
    )
}

// Entity to Domain
fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        type = type,
        productCount = productCount.toIntOrNull() ?: 0,
        initiation = initiation.toIntOrNull() ?: 0,
        research = research.toIntOrNull() ?: 0,
        ready = ready.toIntOrNull() ?: 0,
        information = CategoryInformation(
            indonesian = informationId,
            english = informationEn
        ),
        colors = CategoryColors(
            backgroundColor = colorBackground,
            iconColor = colorIcon
        ),
        actionInfo = CategoryActionInfo(
            action = action,
            information = CategoryInformation(
                indonesian = actionInformationId,
                english = actionInformationEn
            )
        )
    )
}

// DTO to Domain (direct conversion)
fun CategoryDataDto.toDomain(infoData: CategoryInfoDto): Category {
    return Category(
        id = id,
        name = name,
        type = type,
        productCount = productCount.toIntOrNull() ?: 0,
        initiation = initiation.toIntOrNull() ?: 0,
        research = research.toIntOrNull() ?: 0,
        ready = ready.toIntOrNull() ?: 0,
        information = CategoryInformation(
            indonesian = informationId,
            english = informationEn
        ),
        colors = CategoryColors(
            backgroundColor = colorBackground,
            iconColor = colorIcon
        ),
        actionInfo = CategoryActionInfo(
            action = infoData.action,
            information = CategoryInformation(
                indonesian = infoData.actionInformationId,
                english = infoData.actionInformationEn
            )
        )
    )
}

// List extensions with CategoryResponseDto
fun CategoryResponseDto.toEntityList(): List<CategoryEntity> {
    return this.data.map { it.toEntity(this.info) }
}

@JvmName("toDomainListFromEntity")
fun List<CategoryEntity>.toDomainList(): List<Category> {
    return this.map { it.toDomain() }
}

@JvmName("toDomainListFromResponse")
fun CategoryResponseDto.toDomainList(): List<Category> {
    return this.data.map { it.toDomain(this.info) }
}