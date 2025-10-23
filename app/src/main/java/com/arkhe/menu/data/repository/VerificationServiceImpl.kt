package com.arkhe.menu.data.repository

import android.util.Log
import com.arkhe.menu.data.remote.dto.VerificationRequestDto
import com.arkhe.menu.data.remote.dto.VerificationResponseDto
import com.arkhe.menu.utils.Constants.PARAMETER_KEY
import com.arkhe.menu.utils.Constants.PARAMETER_VALUE_ACTIVATION_FLOW
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

class VerificationServiceImpl(
    private val httpClient: HttpClient
) : VerificationService {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    override suspend fun verification(
        userId: String,
        phone: String,
        mail: String
    ): VerificationResponseDto {
        return try {
            val requestDto = VerificationRequestDto(
                userId = userId,
                phone = phone,
                mail = mail
            )
            val requestJson = json.encodeToString(VerificationRequestDto.serializer(), requestDto)
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
                            json.decodeFromString<VerificationResponseDto>(responseText)
                        } catch (parseException: Exception) {
                            Log.e("ApiService", "JSON Parse Failed", parseException)
                            VerificationResponseDto(
                                status = "parse_error",
                                message = "JSON parsing failed: ${parseException.message}",
                                data = null
                            )
                        }
                    } else {
                        VerificationResponseDto(
                            status = "invalid_response",
                            message = "Server returned non-JSON response: $responseText",
                            data = null,
                        )
                    }
                }

                else -> {
                    VerificationResponseDto(
                        status = "unexpected_status",
                        message = "Status ${response.status}: $responseText",
                        data = null
                    )
                }
            }
        } catch (e: Exception) {
            VerificationResponseDto(
                status = "network_error",
                message = "Network error: ${e.message}",
                data = null
            )
        }
    }
}
