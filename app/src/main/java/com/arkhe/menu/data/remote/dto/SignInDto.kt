package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequestDto(
    val sessionActivation: String,
    val userId: String,
    val password: String
)

@Serializable
data class SignInResponseDto(
    val status: String,
    val message: String,
    val data: User? = null
)