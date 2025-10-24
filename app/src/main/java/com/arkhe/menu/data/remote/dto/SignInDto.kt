package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequestDto(
    val userId: String,
    val password: String
)

@Serializable
data class SignInResponseDto(
    val status: String,
    val message: String,
    val data: SignInDataDto? = null
)

@Serializable
data class SignInDataDto(
    val sessionToken: String? = null,
    val userName: String? = null
    // Tambahkan field lain yang dikembalikan oleh API sign-in
)
