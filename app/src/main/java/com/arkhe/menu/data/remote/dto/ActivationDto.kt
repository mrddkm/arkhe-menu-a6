package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerificationRequestDto(
    val userId: String,
    val mail: String,
    val phone: String
)

@Serializable
data class VerificationResponseDto(
    val status: String,
    val message: String,
    val data: VerificationDataDto? = null
)

@Serializable
data class VerificationDataDto(
    val userId: String = "",
    val name: String = "",
)