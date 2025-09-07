package com.arkhe.menu.data.remote

import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.data.remote.api.TripkeunApiService
import com.arkhe.menu.data.remote.api.safeApiCall
import com.arkhe.menu.data.remote.dto.CategoryResponseDto
import com.arkhe.menu.data.remote.dto.ProductResponseDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto

class RemoteDataSource(
    private val apiService: TripkeunApiService
) {
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