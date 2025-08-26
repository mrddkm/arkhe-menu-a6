package com.arkhe.menu.data.mapper

import com.arkhe.menu.data.local.entity.ProfileEntity
import com.arkhe.menu.data.remote.dto.ProfileDataDto
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.model.ProfileInformation
import com.arkhe.menu.domain.model.SocialMedia

// DTO to Entity
fun ProfileDataDto.toEntity(): ProfileEntity {
    return ProfileEntity(
        nameShort = nameShort,
        nameLong = nameLong,
        birthDate = birthDate,
        googleMaps = googleMaps,
        instagram = instagram,
        tiktok = tiktok,
        youtube = youtube,
        tagline = tagline,
        quotes = quotes,
        informationId = informationId,
        informationEn = informationEn,
        createdAt = createdAt,
        updatedAt = updatedAt,
        createdByUserId = createdByUserId,
        updatedByUserId = updatedByUserId
    )
}

// Entity to Domain
fun ProfileEntity.toDomain(): Profile {
    return Profile(
        nameShort = nameShort,
        nameLong = nameLong,
        birthDate = birthDate,
        socialMedia = SocialMedia(
            googleMaps = googleMaps,
            instagram = instagram,
            tiktok = tiktok,
            youtube = youtube
        ),
        tagline = tagline,
        quotes = quotes,
        information = ProfileInformation(
            indonesian = informationId,
            english = informationEn
        )
    )
}

// DTO to Domain (direct conversion)
fun ProfileDataDto.toDomain(): Profile {
    return Profile(
        nameShort = nameShort,
        nameLong = nameLong,
        birthDate = birthDate,
        socialMedia = SocialMedia(
            googleMaps = googleMaps,
            instagram = instagram,
            tiktok = tiktok,
            youtube = youtube
        ),
        tagline = tagline,
        quotes = quotes,
        information = ProfileInformation(
            indonesian = informationId,
            english = informationEn
        )
    )
}

// List extensions
fun List<ProfileDataDto>.toEntityList(): List<ProfileEntity> {
    return this.map { it.toEntity() }
}

fun List<ProfileEntity>.toDomainList(): List<Profile> {
    return this.map { it.toDomain() }
}

fun List<ProfileDataDto>.toDomainList(): List<Profile> {
    return this.map { it.toDomain() }
}