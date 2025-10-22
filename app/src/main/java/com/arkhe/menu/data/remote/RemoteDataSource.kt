package com.arkhe.menu.data.remote

import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.data.remote.api.ArkheApiService
import com.arkhe.menu.data.remote.api.safeApiCall
import com.arkhe.menu.data.remote.dto.CategoryResponseDto
import com.arkhe.menu.data.remote.dto.ProductResponseDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import com.arkhe.menu.data.remote.dto.VerificationResponseDto

class RemoteDataSource(
    private val apiService: ArkheApiService
) {
    suspend fun verification(
        userId: String,
        phone: String,
        mail: String
    ): SafeApiResult<VerificationResponseDto> {
        return safeApiCall {
            apiService.verification(userId, phone, mail)
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