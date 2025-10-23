package com.arkhe.menu.data.repository

import com.arkhe.menu.data.local.preferences.AuthPreferences
import com.arkhe.menu.data.local.security.SecurePinStorage
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.remote.RemoteDataSource
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.auth.Verification
import com.arkhe.menu.domain.repository.AuthRepository
import com.arkhe.menu.utils.Constants.ResponseStatus.SUCCESS
import com.arkhe.menu.utils.Constants.URL_BASE
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

/**
 * Implementation using Ktor client for real backend requests.
 * Designed to connect to Google Apps Script (GAS) via OkHttp engine.
 */
class AuthRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val client: HttpClient,
    private val authPreferences: AuthPreferences,
    private val securePinStorage: SecurePinStorage
) : AuthRepository {

    @Serializable
    private data class ApiResponse(val status: String, val message: String? = null)

    override val isActivatedFlow: Flow<Boolean> = authPreferences.isActivatedFlow
    override val isSignedInFlow: Flow<Boolean> = authPreferences.isSignedInFlow

    override suspend fun verification(
        userId: String,
        phone: String,
        mail: String
    ): SafeApiResult<Verification> {
        return when (
            val remoteResult = remoteDataSource.verification(
                userId = userId,
                phone = phone,
                mail = mail
            )
        ) {
            is SafeApiResult.Success -> {
                if (remoteResult.data.status == SUCCESS && remoteResult.data.data != null) {
                    SafeApiResult.Success(remoteResult.data.toDomain())
                } else {
                    SafeApiResult.Failure(Exception(remoteResult.data.message))
                }
            }

            is SafeApiResult.Failure -> {
                SafeApiResult.Failure(remoteResult.exception)
            }

            is SafeApiResult.Loading -> {
                SafeApiResult.Loading
            }
        }
    }

    override suspend fun verifyActivationCode(code: String): Result<String> {
        return try {
            val response: HttpResponse = client.post(URL_BASE) {
                contentType(ContentType.Application.Json)
                setBody(mapOf("action" to "verifyCode", "code" to code))
            }
            val body: ApiResponse = response.body()
            if (body.status == "ok") Result.success("Code verified")
            else Result.failure(Exception(body.message ?: "Invalid code"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createPassword(password: String): Result<String> {
        return try {
            val response: HttpResponse = client.post(URL_BASE) {
                contentType(ContentType.Application.Json)
                setBody(mapOf("action" to "createPassword", "password" to password))
            }
            val body: ApiResponse = response.body()
            if (body.status == "ok") Result.success("Password created")
            else Result.failure(Exception(body.message ?: "Create password failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(userId: String, password: String): Result<String> {
        return try {
            val response: HttpResponse = client.post(URL_BASE) {
                contentType(ContentType.Application.Json)
                setBody(mapOf("action" to "signIn", "userId" to userId, "password" to password))
            }
            val body: ApiResponse = response.body()
            if (body.status == "ok") Result.success("Login success")
            else Result.failure(Exception(body.message ?: "Login failed"))
        } catch (e: Exception) {
            Result.failure(e)
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
