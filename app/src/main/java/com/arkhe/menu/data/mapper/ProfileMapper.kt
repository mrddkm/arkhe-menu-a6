package com.arkhe.menu.data.mapper

import com.arkhe.menu.data.local.entity.ProfileEntity
import com.arkhe.menu.data.remote.dto.InfoDataDto
import com.arkhe.menu.data.remote.dto.ProfileDataDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import com.arkhe.menu.domain.model.ActionInfo
import com.arkhe.menu.domain.model.Profile
import com.arkhe.menu.domain.model.ProfileInformation
import com.arkhe.menu.domain.model.SocialMedia

/*DTO to Entity*/
fun ProfileDataDto.toEntity(infoData: InfoDataDto): ProfileEntity {
    return ProfileEntity(
        action = infoData.action,
        actionInformationId = infoData.actionInformationId,
        actionInformationEn = infoData.actionInformationEn,
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

/*Entity to Domain*/
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
        ),
        actionInfo = ActionInfo(
            action = action,
            information = ProfileInformation(
                indonesian = actionInformationId,
                english = actionInformationEn
            )
        )
    )
}

/*DTO to Domain (direct conversion)*/
fun ProfileDataDto.toDomain(infoData: InfoDataDto): Profile {
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
        ),
        actionInfo = ActionInfo(
            action = infoData.action,
            information = ProfileInformation(
                indonesian = infoData.actionInformationId,
                english = infoData.actionInformationEn
            )
        )
    )
}


/*List extensions with ProfileResponseDto*/
fun ProfileResponseDto.toEntityList(): List<ProfileEntity> {
    return this.data.map { it.toEntity(this.info) }
}

@JvmName("toDomainListFromEntity")
fun List<ProfileEntity>.toDomainList(): List<Profile> {
    return this.map { it.toDomain() }
}

@JvmName("toDomainListFromResponse")
fun ProfileResponseDto.toDomainList(): List<Profile> {
    return this.data.map { it.toDomain(this.info) }
}