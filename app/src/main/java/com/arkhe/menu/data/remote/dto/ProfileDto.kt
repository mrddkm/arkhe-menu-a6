package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRequestDto(
    @SerialName("session_token")
    val sessionToken: String
)

@Serializable
data class ProfileResponseDto(
    val status: String,
    val message: String,
    val data: List<ProfileDataDto>
)

@Serializable
data class ProfileDataDto(
    @SerialName("name_short")
    val nameShort: String,
    @SerialName("name_long")
    val nameLong: String,
    @SerialName("birth_date")
    val birthDate: String,
    @SerialName("google_maps")
    val googleMaps: String,
    val instagram: String,
    val tiktok: String,
    val youtube: String,
    val tagline: String,
    val quotes: String,
    @SerialName("information_id")
    val informationId: String,
    @SerialName("information_en")
    val informationEn: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("created_by_user_id")
    val createdByUserId: String,
    @SerialName("updated_by_user_id")
    val updatedByUserId: String
)