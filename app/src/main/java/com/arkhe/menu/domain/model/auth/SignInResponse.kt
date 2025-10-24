package com.arkhe.menu.domain.model.auth

data class SignInResponse(
    val status: String,
    val message: String,
    val sessionToken: String? = null
)
