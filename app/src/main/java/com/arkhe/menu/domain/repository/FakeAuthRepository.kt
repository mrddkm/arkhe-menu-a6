package com.arkhe.menu.domain.repository

/**
 * For local UI testing before real backend ready.
 */
/*
class FakeAuthRepository(
    private val authPrefs: AuthPreferences,
    private val securePinStorage: SecurePinStorage
) : AuthRepository {

    override val isActivatedFlow: Flow<Boolean> = authPrefs.isActivatedFlow
    override val isSignedInFlow: Flow<Boolean> = authPrefs.isSignedInFlow

    override suspend fun verification(
        userId: String,
        phone: String,
        mail: String
    ): Result<String> {
        delay(600)
        return if (userId.isNotBlank()) Result.success("Activation request OK (dummy)")
        else Result.failure(Exception("Invalid User"))
    }

    override suspend fun verifyActivationCode(code: String): Result<String> {
        delay(500)
        return if (code == "1234") Result.success("Code verified")
        else Result.failure(Exception("Invalid code"))
    }

    override suspend fun createPassword(password: String): Result<String> {
        delay(400)
        return if (password.length >= 8) Result.success("Password created (dummy)")
        else Result.failure(Exception("Weak password"))
    }

    override suspend fun signIn(userId: String, password: String): Result<String> {
        delay(700)
        return if (userId == "230504" && password == "Qwer@123") Result.success("Login success")
        else Result.failure(Exception("Invalid credentials"))
    }

    override suspend fun savePinHashed(pin: String) {
        securePinStorage.saveHashedPin(pin)
    }

    override suspend fun checkPin(pinInput: String): Boolean = securePinStorage.checkPin(pinInput)

    override suspend fun incrementPinAttempts() = securePinStorage.incrementAttempts()
    override suspend fun resetPinAttempts() = securePinStorage.resetAttempts()
    override suspend fun getPinAttempts(): Int = securePinStorage.getAttempts()

    override suspend fun setActivated(value: Boolean) = authPrefs.setActivated(value)
    override suspend fun isActivated(): Boolean = authPrefs.isActivated()
    override suspend fun setSignedIn(value: Boolean) = authPrefs.setSignedIn(value)
    override suspend fun isSignedIn(): Boolean = authPrefs.isSignedIn()

    override suspend fun deactivatedAuthState() {
        authPrefs.deactivatedAuthState()
    }

    override suspend fun signedOutAuthState() {
        authPrefs.signedOutAuthState()
    }
}
*/
