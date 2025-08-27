package com.arkhe.menu.data.remote.api

import android.util.Log
import com.arkhe.menu.data.remote.dto.ProfileRequestDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

interface TripkeunApiService {
    suspend fun getProfiles(sessionToken: String): ProfileResponseDto
}

class TripkeunApiServiceImpl(
    private val httpClient: HttpClient
) : TripkeunApiService {

    companion object {
        private const val TAG = "TripkeunApiService"
        private const val BASE_URL =
            "https://script.google.com/macros/s/AKfycbxzU5sK4C50geQL1-PWtFm8NeQTErOcY0QGw8XvMfFUOTPKgAYYBZO8p5UuAMVeJ1PCQw/exec"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    override suspend fun getProfiles(sessionToken: String): ProfileResponseDto {
        Log.d(TAG, "========== API REQUEST START ==========")

        return try {
            val urlWithQuery = "$BASE_URL?action=profiles"
            val requestDto = ProfileRequestDto(sessionToken = sessionToken)

            val response: HttpResponse = httpClient.post(urlWithQuery) {
                setBody(Json.encodeToString(ProfileRequestDto.serializer(), requestDto))
            }


            // LOG REQUEST DETAILS
            Log.d(TAG, "🚀 REQUEST DETAILS:")
            Log.d(TAG, "   Method: ${response.request.method}")
            Log.d(TAG, "   Headers: ${response.request.headers.entries()}")
            Log.d(TAG, "📤 REQUEST BODY:")
            Log.d(TAG, "   Raw JSON: ${response.request.content}")
            Log.d(TAG, "   Body Length: ${response.request.content.contentLength} characters")

            // LOG RESPONSE DETAILS
            Log.d(TAG, "📥 RESPONSE DETAILS:")
            Log.d(TAG, "   Status Code: ${response.status.value}")
            Log.d(TAG, "   Status Description: ${response.status.description}")
            Log.d(TAG, "   Final URL: ${response.call.request.url}")
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
                    if (responseText.trim().startsWith("{") || responseText.trim()
                            .startsWith("[")
                    ) {
                        Log.d(TAG, "✅ JSON Response detected, parsing...")
                        try {
                            val parsedResponse =
                                json.decodeFromString<ProfileResponseDto>(responseText)
                            Log.d(TAG, "✅ JSON Parsing SUCCESS:")
                            Log.d(TAG, "   Status: ${parsedResponse.status}")
                            Log.d(TAG, "   Message: ${parsedResponse.message}")
                            Log.d(TAG, "   Data Count: ${parsedResponse.data.size}")
                            Log.d(TAG, "========== API REQUEST SUCCESS ==========")
                            parsedResponse
                        } catch (parseException: Exception) {
                            Log.e(
                                TAG,
                                "❌ JSON Parsing FAILED: ${parseException.message}",
                                parseException
                            )
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
                    Log.w(TAG, "========== API REQUEST REDIRECT ==========")
                    ProfileResponseDto(
                        status = "redirect",
                        message = "Redirect to: $location. Response: $responseText",
                        data = emptyList()
                    )
                }

                HttpStatusCode.MethodNotAllowed -> { // 405
                    Log.e(TAG, "❌ Method Not Allowed (405)")
                    Log.e(TAG, "   Server only allows: ${response.headers["Allow"]}")
                    Log.e(TAG, "========== API REQUEST METHOD NOT ALLOWED ==========")
                    ProfileResponseDto(
                        status = "method_not_allowed",
                        message = "POST method not allowed. Allowed methods: ${response.headers["Allow"]}. Response: $responseText",
                        data = emptyList()
                    )
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
}