package com.arkhe.menu.domain.model.auth

data class ActivationResponse(
    val status: String,
    val message: String,
    val userId: String? = null,
    val name: String? = null,
    val sessionActivation: String? = null
)
