package com.arkhe.menu.data.repository

import com.arkhe.menu.data.local.preferences.AuthPreferences
import com.arkhe.menu.data.local.security.SecurePinStorage
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.remote.RemoteDataSource
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.data.remote.api.SafeResourceResult
import com.arkhe.menu.domain.model.auth.ActivationResponse
import com.arkhe.menu.domain.model.auth.SignInResponse
import com.arkhe.menu.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val authPreferences: AuthPreferences,
    private val securePinStorage: SecurePinStorage
) : AuthRepository {

    override val isActivatedFlow: Flow<Boolean> = authPreferences.isActivatedFlow
    override val isSignedInFlow: Flow<Boolean> = authPreferences.isSignedInFlow

    override fun performActivationStep(
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
    ): Flow<SafeResourceResult<ActivationResponse>> {
        return flow {
            emit(SafeResourceResult.Loading())

            val resultFromDataSource = remoteDataSource.performActivation(
                step = step,
                userId = userId,
                mail = mail,
                phone = phone,
                activationCode = activationCode,
                newPassword = newPassword,
                sessionActivation = sessionActivation,
                isPinActive = isPinActive,
                deviceId = deviceId,
                manufacturer = manufacturer,
                brand = brand,
                model = model,
                device = device,
                product = product,
                osVersion = osVersion,
                sdkLevel = sdkLevel,
                securityPatch = securityPatch,
                deviceType = deviceType,
                appVersionName = appVersionName,
                appVersionCode = appVersionCode
            )

            when (resultFromDataSource) {
                is SafeApiResult.Success -> {
                    emit(SafeResourceResult.Success(resultFromDataSource.data.toDomain()))
                }

                is SafeApiResult.Failure -> {
                    emit(
                        SafeResourceResult.Failure(
                            resultFromDataSource.exception.message ?: "An unknown error occurred"
                        )
                    )
                }

                is SafeApiResult.Loading -> {}
            }
        }
    }

    override fun signIn(userId: String, password: String): Flow<SafeResourceResult<SignInResponse>> {
        return flow {
            emit(SafeResourceResult.Loading())
            // Panggil remoteDataSource untuk sign-in (ini perlu dibuat)
            val result = remoteDataSource.signIn(userId, password)
            when (result) {
                is SafeApiResult.Success -> {
                    // Mapper .toDomain() untuk SignInResponseDto perlu dibuat
                    emit(SafeResourceResult.Success(result.data.toDomain()))
                }
                is SafeApiResult.Failure -> {
                    emit(SafeResourceResult.Failure(result.exception.message ?: "Sign-in failed"))
                }
                is SafeApiResult.Loading -> {}
            }
        }
    }


    // ----------------- Local (PIN + Preferences) -----------------
    override suspend fun savePinHashed(pin: String) {
        securePinStorage.saveHashedPin(pin)
    }

    override suspend fun checkPin(pinInput: String): Boolean = securePinStorage.checkPin(pinInput)

    override suspend fun incrementPinAttempts() {
        securePinStorage.incrementAttempts()
    }

    override suspend fun resetPinAttempts() {
        securePinStorage.resetAttempts()
    }

    override suspend fun getPinAttempts(): Int = securePinStorage.getAttempts()

    override suspend fun setActivated(value: Boolean) = authPreferences.setActivated(value)
    override suspend fun isActivated(): Boolean = authPreferences.isActivated()
    override suspend fun setSignedIn(value: Boolean) = authPreferences.setSignedIn(value)
    override suspend fun isSignedIn(): Boolean = authPreferences.isSignedIn()

    override suspend fun deactivatedAuthState() {
        authPreferences.deactivatedAuthState()
    }

    override suspend fun signedOutAuthState() {
        authPreferences.signedOutAuthState()
    }
}
