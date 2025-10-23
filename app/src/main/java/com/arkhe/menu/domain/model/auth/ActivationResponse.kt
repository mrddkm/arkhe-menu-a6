package com.arkhe.menu.domain.model.auth

data class ActivationResponse(
    val status: String,
    val message: String,
    val sessionActivation: String? = null
)
