package com.arkhe.menu.data.remote.api

import com.arkhe.menu.data.remote.dto.ActivationResponseDto
import com.arkhe.menu.data.remote.dto.CategoryResponseDto
import com.arkhe.menu.data.remote.dto.ProductResponseDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import com.arkhe.menu.data.remote.dto.SignInResponseDto

interface ArkheApiService {
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
    ): ActivationResponseDto

    suspend fun signIn(
        sessionActivation: String,
        userId: String,
        password: String
    ): SignInResponseDto

    suspend fun getProfiles(sessionToken: String): ProfileResponseDto
    suspend fun getCategories(sessionToken: String): CategoryResponseDto
    suspend fun getProducts(
        sessionToken: String,
        productCategoryId: String = "ALL"
    ): ProductResponseDto
}