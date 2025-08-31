package com.arkhe.menu.data.remote.api

import com.arkhe.menu.data.remote.dto.CategoryInfoDto
import com.arkhe.menu.data.remote.dto.CategoryRequestDto
import com.arkhe.menu.data.remote.dto.CategoryResponseDto
import com.arkhe.menu.data.remote.dto.InfoDataDto
import com.arkhe.menu.data.remote.dto.ProfileRequestDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import com.arkhe.menu.utils.Constants
import com.arkhe.menu.utils.Logger.asLogDetailsResponse
import com.arkhe.menu.utils.Logger.asLogJsonResponseException
import com.arkhe.menu.utils.Logger.asLogJsonResponseSuccess
import com.arkhe.menu.utils.Logger.asLogNetworkException
import com.arkhe.menu.utils.Logger.asLogNonJsonResponse
import com.arkhe.menu.utils.Logger.asLogResponseFollowRedirectManually
import com.arkhe.menu.utils.Logger.asLogResponseFollowRedirectManuallyFailed
import com.arkhe.menu.utils.Logger.asLogResponseHttpStatusCode303
import com.arkhe.menu.utils.Logger.asLogResponseHttpStatusCode405
import com.arkhe.menu.utils.Logger.asLogResponseRetryWithAlternativeMethod
import com.arkhe.menu.utils.Logger.asLogResponseRetryWithAlternativeMethodFailed
import com.arkhe.menu.utils.Logger.asLogResponseUnexpectedHttpStatusCode
import com.arkhe.menu.utils.Logger.asLogSendingRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.parametersOf
import kotlinx.serialization.json.Json

interface TripkeunApiService {
    suspend fun getProfiles(sessionToken: String): ProfileResponseDto
    suspend fun getCategories(sessionToken: String): CategoryResponseDto
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
        return try {
            val requestDto = ProfileRequestDto(sessionToken = sessionToken)
            val requestJson = json.encodeToString(requestDto)

            val response: HttpResponse = httpClient.post {
                url(Constants.URL_BASE)
                parameter(Constants.PARAMETER_KEY, Constants.PARAMETER_VALUE_PROFILES)
                setBody(requestJson)
            }
            /*------LOG REQUEST DETAILS*/
            asLogSendingRequest(TAG, requestJson, Constants, response)

            val responseText = response.bodyAsText()

            /*------LOG RESPONSE DETAILS*/
            asLogDetailsResponse(TAG, response, responseText)
            /*Handle different status codes*/
            when (response.status) {
                HttpStatusCode.OK -> {
                    if (responseText.trim().startsWith("{") || responseText.trim()
                            .startsWith("[")
                    ) {
                        try {
                            val parsedResponse =
                                json.decodeFromString<ProfileResponseDto>(
                                    responseText
                                )
                            /*------LOG RESPONSE JSON Parsing SUCCESS*/
                            asLogJsonResponseSuccess(TAG, parsedResponse)

                            parsedResponse
                        } catch (parseException: Exception) {
                            /*------LOG RESPONSE JSON Parsing EXCEPTION*/
                            asLogJsonResponseException(TAG, parseException)

                            ProfileResponseDto(
                                status = "parse_error",
                                message = "JSON parsing failed: ${parseException.message}. Raw response: $responseText",
                                data = emptyList(),
                                info = InfoDataDto.parseError()
                            )
                        }
                    } else {
                        /*------LOG RESPONSE non-JSON*/
                        asLogNonJsonResponse(TAG)

                        ProfileResponseDto(
                            status = "invalid_response",
                            message = "Server returned non-JSON response: $responseText",
                            data = emptyList(),
                            info = InfoDataDto.parseError()
                        )
                    }
                }

                HttpStatusCode.Found, /*302*/
                HttpStatusCode.MovedPermanently, /*301*/
                HttpStatusCode.SeeOther -> { /*303*/
                    val location = response.headers["Location"]
                    /*------LOG RESPONSE HttpStatusCode 303*/
                    asLogResponseHttpStatusCode303(TAG, location, response, responseText)

                    /*Try to follow redirect manually if needed*/
                    if (location != null && location.isNotEmpty()) {
                        return followRedirectManually(location, requestDto)
                    } else {
                        ProfileResponseDto(
                            status = "redirect_no_location",
                            message = "Redirect without location header. Response: $responseText",
                            data = emptyList(),
                            info = InfoDataDto.networkError()
                        )
                    }
                }

                HttpStatusCode.MethodNotAllowed -> { /*405*/
                    /*------LOG RESPONSE HttpStatusCode 405*/
                    asLogResponseHttpStatusCode405(TAG, response)

                    return retryWithAlternativeMethod(sessionToken)
                }

                else -> {
                    /*------LOG RESPONSE Unexpected Status Codes*/
                    asLogResponseUnexpectedHttpStatusCode(TAG, response)

                    ProfileResponseDto(
                        status = "unexpected_status",
                        message = "Status ${response.status}: $responseText",
                        data = emptyList(),
                        info = InfoDataDto.networkError()
                    )
                }
            }
        } catch (e: Exception) {
            /*------LOG RESPONSE Network Exception*/
            asLogNetworkException(TAG, e)

            ProfileResponseDto(
                status = "network_error",
                message = "Network error: ${e.message}",
                data = emptyList(),
                info = InfoDataDto.networkError()
            )
        }
    }

    private suspend fun retryWithAlternativeMethod(sessionToken: String): ProfileResponseDto {
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
            /*------LOG ALTERNATIVE METHOD*/
            asLogResponseRetryWithAlternativeMethod(TAG, responseText)

            when (response.status) {
                HttpStatusCode.OK -> {
                    if (responseText.trim().startsWith("{") || responseText.trim()
                            .startsWith("[")
                    ) {
                        json.decodeFromString<ProfileResponseDto>(responseText)
                    } else {
                        ProfileResponseDto(
                            status = "invalid_response",
                            message = "Server returned non-JSON response: $responseText",
                            data = emptyList(),
                            info = InfoDataDto.networkError()
                        )
                    }
                }

                else -> {
                    ProfileResponseDto(
                        status = "alternative_failed",
                        message = "Alternative method also failed with status ${response.status}: $responseText",
                        data = emptyList(),
                        info = InfoDataDto.networkError()
                    )
                }
            }
        } catch (e: Exception) {
            /*------LOG ALTERNATIVE METHOD EXCEPTION*/
            asLogResponseRetryWithAlternativeMethodFailed(TAG, e)

            ProfileResponseDto(
                status = "alternative_error",
                message = "Alternative method error: ${e.message}",
                data = emptyList(),
                info = InfoDataDto.networkError()
            )
        }
    }

    private suspend fun followRedirectManually(
        location: String,
        requestDto: ProfileRequestDto
    ): ProfileResponseDto {
        return try {
            val requestJson = json.encodeToString(requestDto)

            val response: HttpResponse = httpClient.post {
                url(location)
                parameter(Constants.PARAMETER_KEY, Constants.PARAMETER_VALUE_PROFILES)
                setBody(requestJson)
            }

            val responseText = response.bodyAsText()
            /*------LOG REDIRECT METHOD*/
            asLogResponseFollowRedirectManually(TAG, location, response, responseText)

            when (response.status) {
                HttpStatusCode.OK -> {
                    if (responseText.trim().startsWith("{") || responseText.trim()
                            .startsWith("[")
                    ) {
                        json.decodeFromString<ProfileResponseDto>(responseText)
                    } else {
                        ProfileResponseDto(
                            status = "redirect_invalid_response",
                            message = "Redirect returned non-JSON response: $responseText",
                            data = emptyList(),
                            info = InfoDataDto.networkError()
                        )
                    }
                }

                else -> {
                    ProfileResponseDto(
                        status = "redirect_failed",
                        message = "Redirect failed with status ${response.status}: $responseText",
                        data = emptyList(),
                        info = InfoDataDto.networkError()
                    )
                }
            }
        } catch (e: Exception) {
            /*------LOG REDIRECT METHOD FAILED*/
            asLogResponseFollowRedirectManuallyFailed(TAG, e)
            ProfileResponseDto(
                status = "redirect_error",
                message = "Manual redirect error: ${e.message}",
                data = emptyList(),
                info = InfoDataDto.networkError()
            )
        }
    }

    override suspend fun getCategories(sessionToken: String): CategoryResponseDto {
        return try {
            val requestDto = CategoryRequestDto(sessionToken = sessionToken)
            val requestJson = json.encodeToString(requestDto)

            val response: HttpResponse = httpClient.post {
                url(Constants.URL_BASE)
                parameter(Constants.PARAMETER_KEY, Constants.PARAMETER_VALUE_CATEGORY)
                setBody(requestJson)
            }

            val responseText = response.bodyAsText()

            when (response.status) {
                HttpStatusCode.OK -> {
                    if (responseText.trim().startsWith("{") || responseText.trim().startsWith("[")) {
                        try {
                            val parsedResponse = json.decodeFromString<CategoryResponseDto>(responseText)
                            parsedResponse
                        } catch (parseException: Exception) {
                            CategoryResponseDto(
                                status = "parse_error",
                                message = "JSON parsing failed: ${parseException.message}",
                                data = emptyList(),
                                info = CategoryInfoDto.parseError()
                            )
                        }
                    } else {
                        CategoryResponseDto(
                            status = "invalid_response",
                            message = "Server returned non-JSON response: $responseText",
                            data = emptyList(),
                            info = CategoryInfoDto.parseError()
                        )
                    }
                }
                else -> {
                    CategoryResponseDto(
                        status = "unexpected_status",
                        message = "Status ${response.status}: $responseText",
                        data = emptyList(),
                        info = CategoryInfoDto.networkError()
                    )
                }
            }
        } catch (e: Exception) {
            CategoryResponseDto(
                status = "network_error",
                message = "Network error: ${e.message}",
                data = emptyList(),
                info = CategoryInfoDto.networkError()
            )
        }
    }
}