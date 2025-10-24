package com.arkhe.menu.data.remote

import com.arkhe.menu.data.remote.api.ArkheApiService
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.data.remote.api.safeApiCall
import com.arkhe.menu.data.remote.dto.CategoryResponseDto
import com.arkhe.menu.data.remote.dto.ProductResponseDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import com.arkhe.menu.data.remote.dto.ActivationResponseDto

class RemoteDataSource(
    private val apiService: ArkheApiService
) {
    suspend fun performActivation(
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
    ): SafeApiResult<ActivationResponseDto> {
        return safeApiCall {
            apiService.performActivation(
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
    }

    suspend fun signIn(userId: String, password: String): SafeApiResult<SignInResponseDto> {
        return safeApiCall {
            apiService.signIn(userId, password)
        }
    }

    suspend fun getProfiles(sessionToken: String): SafeApiResult<ProfileResponseDto> {
        return safeApiCall {
            apiService.getProfiles(sessionToken)
        }
    }

    suspend fun getCategories(sessionToken: String): SafeApiResult<CategoryResponseDto> {
        return safeApiCall {
            apiService.getCategories(sessionToken)
        }
    }

    suspend fun getProducts(
        sessionToken: String,
        productCategoryId: String = "ALL"
    ): SafeApiResult<ProductResponseDto> {
        return safeApiCall {
            apiService.getProducts(sessionToken, productCategoryId)
        }
    }
}