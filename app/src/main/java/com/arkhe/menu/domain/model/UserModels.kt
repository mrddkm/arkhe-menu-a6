package com.arkhe.menu.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val sessionActivation: String,
    val userId: String,
    val password: String
)

@Serializable
data class UserResponse(
    val status: String,
    val message: String,
    val data: List<UserData>
)

@Serializable
data class UserData(
    val id: String,
    val userId: String,
    val password: String,
    val activationCode: String,
    val mail: String,
    val name: String,
    val phone: String,
    val initial: String,
    val nickName: String,
    val birthday: String,
    val gender: String,
    val photo: String,
    val positionId: String,
    val positionInitial: String,
    val positionName: String,
    val divisionId: String,
    val divisionInitial: String,
    val divisionName: String,
    val roles: String,
    val status: String,
    val sessionActivation: String,
    val sessionActivationAt: String,
    val sessionToken: String,
    val sessionExpiredAt: String,
    val createdAt: String,
    val updatedAt: String,
    val lastActiveAt: String,
    val rowIndex: Int,
)

/*Domain model for UI*/
data class User(
    val id: String,
    val userId: String,
    val mail: String,
    val name: String,
    val phone: String,
    val initial: String,
    val nickName: String,
    val birthday: String,
    val gender: String,
    val photo: String,
    val positionId: String,
    val positionInitial: String,
    val positionName: String,
    val divisionId: String,
    val divisionInitial: String,
    val divisionName: String,
    val roles: String,
    val status: String,
    val sessionActivation: String,
    val sessionActivationAt: String,
    val sessionToken: String,
    val sessionExpiredAt: String,
    val createdAt: String,
    val updatedAt: String,
    val lastActiveAt: String,
    val localImagePath: String? = null
)