package com.arkhe.menu.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Contract for authentication-related operations.
 * Keeps domain layer independent from implementation.
 */
interface AuthRepository {
    suspend fun requestActivation(userId: String, phone: String, email: String): Result<String>
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

    suspend fun resetAuthState()

    // ✅ Tambahan real-time flow
    val isActivatedFlow: Flow<Boolean>
    val isSignedInFlow: Flow<Boolean>
}
