package com.arkhe.menu.domain.repository

import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.auth.Verification
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun verification(
        userId: String,
        phone: String,
        mail: String
    ): SafeApiResult<Verification>

    suspend fun verifyActivationCode(code: String): Result<String>
    suspend fun createPassword(password: String): Result<String>
    suspend fun signIn(userId: String, password: String): Result<String>

    // Local secure PIN logic
    suspend fun savePinHashed(pin: String)
    suspend fun checkPin(pinInput: String): Boolean
    suspend fun incrementPinAttempts()
    suspend fun resetPinAttempts()
    suspend fun getPinAttempts(): Int

    // Local auth flags (from DataStore)
    suspend fun setActivated(value: Boolean)
    suspend fun isActivated(): Boolean
    suspend fun setSignedIn(value: Boolean)
    suspend fun isSignedIn(): Boolean

    suspend fun deactivatedAuthState()
    suspend fun signedOutAuthState()

    // ✅ Tambahan real-time flow
    val isActivatedFlow: Flow<Boolean>
    val isSignedInFlow: Flow<Boolean>
}
