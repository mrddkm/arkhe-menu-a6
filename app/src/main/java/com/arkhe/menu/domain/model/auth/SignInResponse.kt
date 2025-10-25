package com.arkhe.menu.domain.model.auth

data class SignInResponse(
    val status: String,
    val message: String,
    val sessionToken: String?,
    val userId: String?,
    val profileId: String?,
    val nickName: String?,
    val initial: String?,
    val positionName: String?,
    val divisionName: String?,
    val roles: List<String>?,
    val sessionExpiredAt: String?
)
