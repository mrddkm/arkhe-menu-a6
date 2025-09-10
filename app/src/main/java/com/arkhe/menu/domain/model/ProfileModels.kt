package com.arkhe.menu.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileRequest(
    val sessionToken: String
)

@Serializable
data class ProfileResponse(
    val status: String,
    val message: String,
    val data: List<ProfileData>,
    val info: InfoData
)

@Serializable
data class ProfileData(
    val nameShort: String,
    val nameLong: String,
    val birthDate: String,
    val logo: String,
    val googleMaps: String,
    val whatsApp: String,
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

@Serializable
data class InfoData(
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String
)

/*Domain model for UI*/
data class Profile(
    val nameShort: String,
    val nameLong: String,
    val birthDate: String,
    val logo: String,
    val socialMedia: SocialMedia,
    val tagline: String,
    val quotes: String,
    val information: ProfileInformation,
    val actionInfo: ActionInfo,
    val localImagePath: String? = null
)

data class ActionInfo(
    val action: String,
    val information: ProfileInformation
)

data class SocialMedia(
    val googleMaps: String,
    val whatsApp: String,
    val instagram: String,
    val tiktok: String,
    val youtube: String
)

data class ProfileInformation(
    val indonesian: String,
    val english: String
)