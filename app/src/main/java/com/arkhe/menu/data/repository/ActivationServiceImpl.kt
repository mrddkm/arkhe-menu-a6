package com.arkhe.menu.data.repository

import android.util.Log
import com.arkhe.menu.data.remote.dto.ActivationRequestDto
import com.arkhe.menu.data.remote.dto.ActivationResponseDto
import com.arkhe.menu.utils.Constants.PARAMETER_KEY
import com.arkhe.menu.utils.Constants.PARAMETER_VALUE_ACTIVATION_FLOW
import com.arkhe.menu.utils.Constants.ResponseStatus.FAILED
import com.arkhe.menu.utils.Constants.URL_BASE
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

class ActivationServiceImpl(
    private val httpClient: HttpClient
) : ActivationService {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    override suspend fun performActivation(
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
    ): ActivationResponseDto {
        return try {
            val requestDto = ActivationRequestDto(
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
            val requestJson = json.encodeToString(ActivationRequestDto.serializer(), requestDto)
            val response: HttpResponse = httpClient.post {
                url(URL_BASE)
                parameter(PARAMETER_KEY, PARAMETER_VALUE_ACTIVATION_FLOW)
                setBody(requestJson)
            }

            val responseText = response.bodyAsText()
            Log.d("ApiService", "Raw Verification Response: $responseText")

            when (response.status) {
                HttpStatusCode.OK -> {
                    if (responseText.trim().startsWith("{") || responseText.trim()
                            .startsWith("[")
                    ) {
                        try {
                            json.decodeFromString<ActivationResponseDto>(responseText)
                        } catch (parseException: Exception) {
                            Log.e("ApiService", "JSON Parse Failed", parseException)
                            ActivationResponseDto(
                                status = FAILED,
                                message = "JSON parsing failed: ${parseException.message}",
                                data = null
                            )
                        }
                    } else {
                        ActivationResponseDto(
                            status = FAILED,
                            message = "Server returned non-JSON response: $responseText",
                            data = null,
                        )
                    }
                }

                else -> {
                    ActivationResponseDto(
                        status = FAILED,
                        message = "Status ${response.status}: $responseText",
                        data = null
                    )
                }
            }
        } catch (e: Exception) {
            ActivationResponseDto(
                status = FAILED,
                message = "Network error: ${e.message}",
                data = null
            )
        }
    }
}
