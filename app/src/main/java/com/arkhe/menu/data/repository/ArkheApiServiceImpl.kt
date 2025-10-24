package com.arkhe.menu.data.repository

import com.arkhe.menu.data.remote.api.ArkheApiService
import com.arkhe.menu.data.remote.dto.ActivationResponseDto
import com.arkhe.menu.data.remote.dto.CategoryInfoDto
import com.arkhe.menu.data.remote.dto.CategoryRequestDto
import com.arkhe.menu.data.remote.dto.CategoryResponseDto
import com.arkhe.menu.data.remote.dto.InfoDataDto
import com.arkhe.menu.data.remote.dto.ProductInfoDto
import com.arkhe.menu.data.remote.dto.ProductRequestDto
import com.arkhe.menu.data.remote.dto.ProductResponseDto
import com.arkhe.menu.data.remote.dto.ProfileRequestDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import com.arkhe.menu.data.remote.dto.SignInRequestDto
import com.arkhe.menu.data.remote.dto.SignInResponseDto
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
    private val activationService: ActivationService
) : ArkheApiService {

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
        return activationService.performActivation(
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
    }

    override suspend fun signIn(
        sessionActivation: String,
        userId: String,
        password: String
    ): SignInResponseDto {
        return try {
            val requestDto = SignInRequestDto(
                sessionActivation = sessionActivation,
                userId = userId,
                password = password
            )
            val response: HttpResponse = httpClient.post {
                url(Constants.URL_BASE)
                parameter(Constants.PARAMETER_KEY, Constants.PARAMETER_VALUE_SIGN_IN)
                setBody(requestDto)
            }

            val responseText = response.bodyAsText()
            if (response.status == HttpStatusCode.OK) {
                json.decodeFromString<SignInResponseDto>(responseText)
            } else {
                SignInResponseDto(
                    status = "error",
                    message = "Sign-in failed: ${response.status}",
                    data = null
                )
            }
        } catch (e: Exception) {
            SignInResponseDto(
                status = "exception",
                message = e.message ?: "An unknown error occurred",
                data = null
            )
        }
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
                            parsedResponse
                        } catch (parseException: Exception) {
                            ProfileResponseDto(
                                status = "parse_error",
                                message = "JSON parsing failed: ${parseException.message}. Raw response: $responseText",
                                data = emptyList(),
                                info = InfoDataDto.parseError()
                            )
                        }
                    } else {
                        ProfileResponseDto(
                            status = Constants.ResponseStatus.FAILED,
                            message = "You have reached your free Google API quota limit. Please wait 24 hours before retrying.",
                            data = emptyList(),
                            info = InfoDataDto.parseError()
                        )
                    }
                }

                HttpStatusCode.Found, /*302*/
                HttpStatusCode.MovedPermanently, /*301*/
                HttpStatusCode.SeeOther -> { /*303*/
                    val location = response.headers["Location"]

                    /*Try to follow redirect manually if needed*/
                    if (location != null && location.isNotEmpty()) {
                        return followRedirectManually(location, requestDto)
                    } else {
                        ProfileResponseDto(
                            status = Constants.ResponseStatus.FAILED,
                            message = "Redirect without location header. Response: $responseText",
                            data = emptyList(),
                            info = InfoDataDto.networkError()
                        )
                    }
                }

                HttpStatusCode.MethodNotAllowed -> { /*405*/
                    return retryWithAlternativeMethod(sessionToken)
                }

                else -> {
                    ProfileResponseDto(
                        status = Constants.ResponseStatus.FAILED,
                        message = "Status ${response.status}: $responseText",
                        data = emptyList(),
                        info = InfoDataDto.networkError()
                    )
                }
            }
        } catch (e: Exception) {
            ProfileResponseDto(
                status = Constants.ResponseStatus.FAILED,
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

            when (response.status) {
                HttpStatusCode.OK -> {
                    if (
                        responseText.trim().startsWith("{") ||
                        responseText.trim().startsWith("[")
                    ) {
                        json.decodeFromString<ProfileResponseDto>(responseText)
                    } else {
                        ProfileResponseDto(
                            status = Constants.ResponseStatus.FAILED,
                            message = "You have reached your free Google API quota limit. Please wait 24 hours before retrying.",
                            data = emptyList(),
                            info = InfoDataDto.networkError()
                        )
                    }
                }

                else -> {
                    ProfileResponseDto(
                        status = Constants.ResponseStatus.FAILED,
                        message = "Alternative method also failed with status ${response.status}: $responseText",
                        data = emptyList(),
                        info = InfoDataDto.networkError()
                    )
                }
            }
        } catch (e: Exception) {
            ProfileResponseDto(
                status = Constants.ResponseStatus.FAILED,
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

            when (response.status) {
                HttpStatusCode.OK -> {
                    if (
                        responseText.trim().startsWith("{") ||
                        responseText.trim().startsWith("[")
                    ) {
                        json.decodeFromString<ProfileResponseDto>(responseText)
                    } else {
                        ProfileResponseDto(
                            status = Constants.ResponseStatus.FAILED,
                            message = "You have reached your free Google API quota limit. Please wait 24 hours before retrying.",
                            data = emptyList(),
                            info = InfoDataDto.networkError()
                        )
                    }
                }

                else -> {
                    ProfileResponseDto(
                        status = Constants.ResponseStatus.FAILED,
                        message = "Redirect failed with status ${response.status}: $responseText",
                        data = emptyList(),
                        info = InfoDataDto.networkError()
                    )
                }
            }
        } catch (e: Exception) {
            ProfileResponseDto(
                status = Constants.ResponseStatus.FAILED,
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
                    if (
                        responseText.trim().startsWith("{") ||
                        responseText.trim().startsWith("[")
                    ) {
                        try {
                            val parsedResponse =
                                json.decodeFromString<CategoryResponseDto>(responseText)
                            parsedResponse
                        } catch (parseException: Exception) {
                            CategoryResponseDto(
                                status = Constants.ResponseStatus.FAILED,
                                message = "JSON parsing failed: ${parseException.message}",
                                data = emptyList(),
                                info = CategoryInfoDto.parseError()
                            )
                        }
                    } else {
                        CategoryResponseDto(
                            status = Constants.ResponseStatus.FAILED,
                            message = "You have reached your free Google API quota limit. Please wait 24 hours before retrying.",
                            data = emptyList(),
                            info = CategoryInfoDto.parseError()
                        )
                    }
                }

                else -> {
                    CategoryResponseDto(
                        status = Constants.ResponseStatus.FAILED,
                        message = "Status ${response.status}: $responseText",
                        data = emptyList(),
                        info = CategoryInfoDto.networkError()
                    )
                }
            }
        } catch (e: Exception) {
            CategoryResponseDto(
                status = Constants.ResponseStatus.FAILED,
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
                    if (
                        responseText.trim().startsWith("{") ||
                        responseText.trim().startsWith("[")
                    ) {
                        try {
                            val parsedResponse =
                                json.decodeFromString<ProductResponseDto>(responseText)
                            parsedResponse
                        } catch (parseException: Exception) {
                            ProductResponseDto(
                                status = Constants.ResponseStatus.FAILED,
                                message = "JSON parsing failed: ${parseException.message}",
                                data = emptyList(),
                                info = ProductInfoDto.parseError()
                            )
                        }
                    } else {
                        ProductResponseDto(
                            status = Constants.ResponseStatus.FAILED,
                            message = "You have reached your free Google API quota limit. Please wait 24 hours before retrying.",
                            data = emptyList(),
                            info = ProductInfoDto.parseError()
                        )
                    }
                }

                else -> {
                    ProductResponseDto(
                        status = Constants.ResponseStatus.FAILED,
                        message = "Status ${response.status}: $responseText",
                        data = emptyList(),
                        info = ProductInfoDto.networkError()
                    )
                }
            }
        } catch (e: Exception) {
            ProductResponseDto(
                status = Constants.ResponseStatus.FAILED,
                message = "Network error: ${e.message}",
                data = emptyList(),
                info = ProductInfoDto.networkError()
            )
        }
    }
}