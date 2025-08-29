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
    val googleMaps: String,
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
    val socialMedia: SocialMedia,
    val tagline: String,
    val quotes: String,
    val information: ProfileInformation,
    val actionInfo: ActionInfo
)

data class ActionInfo(
    val action: String,
    val information: ProfileInformation
)

data class SocialMedia(
    val googleMaps: String,
    val instagram: String,
    val tiktok: String,
    val youtube: String
)

data class ProfileInformation(
    val indonesian: String,
    val english: String
)

/*Result wrapper for API responses*/
sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Throwable) : ApiResult<Nothing>()
    data object Loading : ApiResult<Nothing>()
}