package com.arkhe.menu.data.remote.api

import android.util.Log
import com.arkhe.menu.data.remote.dto.ProfileRequestDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import com.arkhe.menu.presentation.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.parametersOf
import kotlinx.serialization.json.Json

interface TripkeunApiService {
    suspend fun getProfiles(sessionToken: String): ProfileResponseDto
}

class TripkeunApiServiceImpl(
    private val httpClient: HttpClient
) : TripkeunApiService {

    companion object {
        private const val TAG = "TripkeunApiService"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    override suspend fun getProfiles(sessionToken: String): ProfileResponseDto {
        Log.d(TAG, "========== API REQUEST START ==========")

        return try {
            val requestDto = ProfileRequestDto(sessionToken = sessionToken)
            val requestJson = json.encodeToString(requestDto)

            Log.d(TAG, "🚀 SENDING REQUEST:")
            Log.d(TAG, "   URL: ${Constants.URL_BASE}")
            Log.d(TAG, "   Action: ${Constants.PARAMETER_VALUE_PROFILES}")
            Log.d(TAG, "   Payload: $requestJson")

            // Method 1: Gunakan URL lengkap dengan POST body
            val response: HttpResponse = httpClient.post {
                url(Constants.URL_BASE)
                parameter(Constants.PARAMETER_KEY, Constants.PARAMETER_VALUE_PROFILES)
                setBody(requestJson)
            }

            // Alternative Method 2: Jika method 1 gagal, coba submitForm
            // val response: HttpResponse = httpClient.submitForm(
            //     url = Constants.URL_BASE,
            //     formParameters = Parameters.build {
            //         append(Constants.PARAMETER_KEY, Constants.PARAMETER_VALUE_PROFILES)
            //     }
            // ) {
            //     setBody(requestJson)
            // }

            // LOG REQUEST DETAILS
            Log.d(TAG, "📤 REQUEST SENT:")
            Log.d(TAG, "   Method: ${response.request.method.value}")
            Log.d(TAG, "   URL: ${response.request.url}")
            Log.d(TAG, "   Headers:")
            response.request.headers.entries().forEach { (key, values) ->
                values.forEach { value ->
                    Log.d(TAG, "      $key: $value")
                }
            }

            // LOG RESPONSE DETAILS
            Log.d(TAG, "📥 RESPONSE RECEIVED:")
            Log.d(TAG, "   Status Code: ${response.status.value}")
            Log.d(TAG, "   Status Description: ${response.status.description}")
            Log.d(TAG, "   Response Headers:")

            response.headers.entries().forEach { (key, values) ->
                values.forEach { value ->
                    Log.d(TAG, "      $key: $value")
                }
            }

            val responseText = response.bodyAsText()
            Log.d(TAG, "📄 RESPONSE BODY:")
            Log.d(TAG, "   Body Length: ${responseText.length} characters")
            Log.d(TAG, "   Raw Response: $responseText")

            // Handle different status codes
            when (response.status) {
                HttpStatusCode.OK -> {
                    if (responseText.trim().startsWith("{") || responseText.trim().startsWith("[")) {
                        Log.d(TAG, "✅ JSON Response detected, parsing...")
                        try {
                            val parsedResponse = json.decodeFromString<ProfileResponseDto>(responseText)
                            Log.d(TAG, "✅ JSON Parsing SUCCESS:")
                            Log.d(TAG, "   Status: ${parsedResponse.status}")
                            Log.d(TAG, "   Message: ${parsedResponse.message}")
                            Log.d(TAG, "   Data Count: ${parsedResponse.data.size}")
                            Log.d(TAG, "========== API REQUEST SUCCESS ==========")
                            parsedResponse
                        } catch (parseException: Exception) {
                            Log.e(TAG, "❌ JSON Parsing FAILED: ${parseException.message}", parseException)
                            Log.e(TAG, "========== API REQUEST PARSE ERROR ==========")
                            ProfileResponseDto(
                                status = "parse_error",
                                message = "JSON parsing failed: ${parseException.message}. Raw response: $responseText",
                                data = emptyList()
                            )
                        }
                    } else {
                        Log.w(TAG, "⚠️ Non-JSON Response received")
                        Log.w(TAG, "========== API REQUEST NON-JSON ==========")
                        ProfileResponseDto(
                            status = "invalid_response",
                            message = "Server returned non-JSON response: $responseText",
                            data = emptyList()
                        )
                    }
                }

                HttpStatusCode.Found, // 302
                HttpStatusCode.MovedPermanently, // 301
                HttpStatusCode.SeeOther -> { // 303
                    Log.w(TAG, "🔄 Redirect Response received")
                    val location = response.headers["Location"]
                    Log.w(TAG, "   Redirect Location: $location")
                    Log.w(TAG, "   Original URL: ${response.request.url}")
                    Log.w(TAG, "   Response Body: $responseText")
                    Log.w(TAG, "========== API REQUEST REDIRECT ==========")

                    // Try to follow redirect manually if needed
                    if (location != null && location.isNotEmpty()) {
                        Log.w(TAG, "🔄 Following redirect manually...")
                        return followRedirectManually(location, requestDto)
                    } else {
                        ProfileResponseDto(
                            status = "redirect_no_location",
                            message = "Redirect without location header. Response: $responseText",
                            data = emptyList()
                        )
                    }
                }

                HttpStatusCode.MethodNotAllowed -> { // 405
                    Log.e(TAG, "❌ Method Not Allowed (405)")
                    Log.e(TAG, "   Server only allows: ${response.headers["Allow"]}")
                    Log.e(TAG, "   Current method: ${response.request.method.value}")
                    Log.e(TAG, "   Request URL: ${response.request.url}")
                    Log.e(TAG, "========== API REQUEST METHOD NOT ALLOWED ==========")

                    // Try alternative method
                    Log.w(TAG, "🔄 Retrying with alternative method...")
                    return retryWithAlternativeMethod(sessionToken)
                }

                else -> {
                    Log.e(TAG, "❌ Unexpected Status Code: ${response.status}")
                    Log.e(TAG, "========== API REQUEST UNEXPECTED STATUS ==========")
                    ProfileResponseDto(
                        status = "unexpected_status",
                        message = "Status ${response.status}: $responseText",
                        data = emptyList()
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 API CALL EXCEPTION: ${e.message}", e)
            Log.e(TAG, "   Exception Type: ${e.javaClass.simpleName}")
            Log.e(TAG, "   Stack Trace:")
            e.stackTrace.take(10).forEach { stackElement ->
                Log.e(TAG, "      at $stackElement")
            }
            Log.e(TAG, "========== API REQUEST FAILED ==========")
            ProfileResponseDto(
                status = "network_error",
                message = "Network error: ${e.message}",
                data = emptyList()
            )
        }
    }

    private suspend fun retryWithAlternativeMethod(sessionToken: String): ProfileResponseDto {
        Log.d(TAG, "🔄 ALTERNATIVE METHOD: Using submitForm")

        return try {
            val requestDto = ProfileRequestDto(sessionToken = sessionToken)
            val requestJson = json.encodeToString(requestDto)

            val response: HttpResponse = httpClient.submitForm(
                url = Constants.URL_BASE,
                formParameters = parametersOf(
                    Constants.PARAMETER_KEY to listOf(Constants.PARAMETER_VALUE_PROFILES)
                )
            ) {
                setBody(requestJson)
            }

            val responseText = response.bodyAsText()
            Log.d(TAG, "📥 ALTERNATIVE RESPONSE: $responseText")

            when (response.status) {
                HttpStatusCode.OK -> {
                    if (responseText.trim().startsWith("{") || responseText.trim().startsWith("[")) {
                        json.decodeFromString<ProfileResponseDto>(responseText)
                    } else {
                        ProfileResponseDto(
                            status = "invalid_response",
                            message = "Server returned non-JSON response: $responseText",
                            data = emptyList()
                        )
                    }
                }
                else -> {
                    ProfileResponseDto(
                        status = "alternative_failed",
                        message = "Alternative method also failed with status ${response.status}: $responseText",
                        data = emptyList()
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 ALTERNATIVE METHOD FAILED: ${e.message}", e)
            ProfileResponseDto(
                status = "alternative_error",
                message = "Alternative method error: ${e.message}",
                data = emptyList()
            )
        }
    }

    private suspend fun followRedirectManually(location: String, requestDto: ProfileRequestDto): ProfileResponseDto {
        Log.d(TAG, "🔄 MANUAL REDIRECT: Following to $location")

        return try {
            val requestJson = json.encodeToString(requestDto)

            val response: HttpResponse = httpClient.post {
                url(location)
                parameter(Constants.PARAMETER_KEY, Constants.PARAMETER_VALUE_PROFILES)
                setBody(requestJson)
            }

            val responseText = response.bodyAsText()
            Log.d(TAG, "📥 REDIRECT RESPONSE: Status ${response.status.value}")
            Log.d(TAG, "📥 REDIRECT BODY: $responseText")

            when (response.status) {
                HttpStatusCode.OK -> {
                    if (responseText.trim().startsWith("{") || responseText.trim().startsWith("[")) {
                        json.decodeFromString<ProfileResponseDto>(responseText)
                    } else {
                        ProfileResponseDto(
                            status = "redirect_invalid_response",
                            message = "Redirect returned non-JSON response: $responseText",
                            data = emptyList()
                        )
                    }
                }
                else -> {
                    ProfileResponseDto(
                        status = "redirect_failed",
                        message = "Redirect failed with status ${response.status}: $responseText",
                        data = emptyList()
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 MANUAL REDIRECT FAILED: ${e.message}", e)
            ProfileResponseDto(
                status = "redirect_error",
                message = "Manual redirect error: ${e.message}",
                data = emptyList()
            )
        }
    }
}