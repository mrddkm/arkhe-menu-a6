package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val error: String? = null
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val refreshToken: String,
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    val role: String,
    val fullName: String
)