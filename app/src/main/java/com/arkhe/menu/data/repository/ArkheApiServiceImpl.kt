package com.arkhe.menu.data.repository

import com.arkhe.menu.data.remote.api.ArkheApiService
import com.arkhe.menu.data.remote.dto.CategoryInfoDto
import com.arkhe.menu.data.remote.dto.CategoryRequestDto
import com.arkhe.menu.data.remote.dto.CategoryResponseDto
import com.arkhe.menu.data.remote.dto.InfoDataDto
import com.arkhe.menu.data.remote.dto.ProductInfoDto
import com.arkhe.menu.data.remote.dto.ProductRequestDto
import com.arkhe.menu.data.remote.dto.ProductResponseDto
import com.arkhe.menu.data.remote.dto.ProfileRequestDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import com.arkhe.menu.data.remote.dto.VerificationResponseDto
import com.arkhe.menu.utils.Constants
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

class ArkheApiServiceImpl(
    private val httpClient: HttpClient,
    private val verificationService: VerificationService
) : ArkheApiService {

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
        return verificationService.verification(userId, phone, mail)
    }

/*    override suspend fun verification(
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
    }*/

    override suspend fun getProfiles(sessionToken: String): ProfileResponseDto {
        return try {
            val requestDto = ProfileRequestDto(sessionToken = sessionToken)
            val requestJson = json.encodeToString(requestDto)

            val response: HttpResponse = httpClient.post {
                url(Constants.URL_BASE)
                parameter(Constants.PARAMETER_KEY, Constants.PARAMETER_VALUE_PROFILES)
                setBody(requestJson)
            }

            val responseText = response.bodyAsText()

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
                            /*------LOG RESPONSE JSON Parsing SUCCESS
                            asLogJsonResponseSuccess(TAG, parsedResponse)
                            * */

                            parsedResponse
                        } catch (parseException: Exception) {
                            /*------LOG RESPONSE JSON Parsing EXCEPTION
                            asLogJsonResponseException(TAG, parseException)
                            * */

                            ProfileResponseDto(
                                status = "parse_error",
                                message = "JSON parsing failed: ${parseException.message}. Raw response: $responseText",
                                data = emptyList(),
                                info = InfoDataDto.parseError()
                            )
                        }
                    } else {
                        /*------LOG RESPONSE non-JSON
                        asLogNonJsonResponse(TAG)
                        * */

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
                    /*------LOG RESPONSE HttpStatusCode 303
                    asLogResponseHttpStatusCode303(TAG, location, response, responseText)
                    * */

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
                    /*------LOG RESPONSE HttpStatusCode 405
                    asLogResponseHttpStatusCode405(TAG, response)
                    * */

                    return retryWithAlternativeMethod(sessionToken)
                }

                else -> {
                    /*------LOG RESPONSE Unexpected Status Codes
                    asLogResponseUnexpectedHttpStatusCode(TAG, response)
                    * */

                    ProfileResponseDto(
                        status = "unexpected_status",
                        message = "Status ${response.status}: $responseText",
                        data = emptyList(),
                        info = InfoDataDto.networkError()
                    )
                }
            }
        } catch (e: Exception) {
            /*------LOG RESPONSE Network Exception
            asLogNetworkException(TAG, e)
            * */

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
            /*------LOG ALTERNATIVE METHOD
            asLogResponseRetryWithAlternativeMethod(TAG, responseText)
            * */

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
            /*------LOG ALTERNATIVE METHOD EXCEPTION
            asLogResponseRetryWithAlternativeMethodFailed(TAG, e)
            * */

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
            /*------LOG REDIRECT METHOD
            asLogResponseFollowRedirectManually(TAG, location, response, responseText)
            * */

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
            /*------LOG REDIRECT METHOD FAILED
            asLogResponseFollowRedirectManuallyFailed(TAG, e)
            * */

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
                    if (responseText.trim().startsWith("{") || responseText.trim()
                            .startsWith("[")
                    ) {
                        try {
                            val parsedResponse =
                                json.decodeFromString<CategoryResponseDto>(responseText)
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

    override suspend fun getProducts(
        sessionToken: String,
        productCategoryId: String
    ): ProductResponseDto {
        return try {
            val requestDto = ProductRequestDto(
                sessionToken = sessionToken,
                productCategoryId = productCategoryId
            )
            val requestJson = json.encodeToString(requestDto)
            val response: HttpResponse = httpClient.post {
                url(Constants.URL_BASE)
                parameter(Constants.PARAMETER_KEY, Constants.PARAMETER_VALUE_PRODUCTS)
                setBody(requestJson)
            }
            val responseText = response.bodyAsText()
            when (response.status) {
                HttpStatusCode.OK -> {
                    if (responseText.trim().startsWith("{") || responseText.trim()
                            .startsWith("[")
                    ) {
                        try {
                            val parsedResponse =
                                json.decodeFromString<ProductResponseDto>(responseText)
                            parsedResponse
                        } catch (parseException: Exception) {
                            ProductResponseDto(
                                status = "parse_error",
                                message = "JSON parsing failed: ${parseException.message}",
                                data = emptyList(),
                                info = ProductInfoDto.parseError()
                            )
                        }
                    } else {
                        ProductResponseDto(
                            status = "invalid_response",
                            message = "Server returned non-JSON response: $responseText",
                            data = emptyList(),
                            info = ProductInfoDto.parseError()
                        )
                    }
                }

                else -> {
                    ProductResponseDto(
                        status = "unexpected_status",
                        message = "Status ${response.status}: $responseText",
                        data = emptyList(),
                        info = ProductInfoDto.networkError()
                    )
                }
            }
        } catch (e: Exception) {
            ProductResponseDto(
                status = "network_error",
                message = "Network error: ${e.message}",
                data = emptyList(),
                info = ProductInfoDto.networkError()
            )
        }
    }
}