package com.arkhe.menu.domain.repository

import com.arkhe.menu.data.remote.api.SafeResourceResult
import com.arkhe.menu.data.remote.dto.ActivationResponseDto
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun performActivationStep(
        step: String,
        userId: String?,
        mail: String?,
        phone: String?,
        activationCode: String?,
        newPassword: String?,
        sessionActivation: String?,
        isPinActive: Boolean?,
        deviceId: String?,
        manufacturer: String?,
        brand: String?,
        model: String?,
        device: String?,
        product: String?,
        osVersion: String?,
        sdkLevel: String?,
        securityPatch: String?,
        deviceType: String?,
        appVersionName: String?,
        appVersionCode: String?
    ): Flow<SafeResourceResult<ActivationResponseDto>>

    /*Local secure PIN logic*/
    suspend fun savePinHashed(pin: String)
    suspend fun checkPin(pinInput: String): Boolean
    suspend fun incrementPinAttempts()
    suspend fun resetPinAttempts()
    suspend fun getPinAttempts(): Int

    /*Local auth flags (from DataStore)*/
    suspend fun setActivated(value: Boolean)
    suspend fun isActivated(): Boolean
    suspend fun setSignedIn(value: Boolean)
    suspend fun isSignedIn(): Boolean

    suspend fun deactivatedAuthState()
    suspend fun signedOutAuthState()

    /*real-time flows*/
    val isActivatedFlow: Flow<Boolean>
    val isSignedInFlow: Flow<Boolean>
}
