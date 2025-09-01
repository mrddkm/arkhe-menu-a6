package com.arkhe.menu.data.remote

import com.arkhe.menu.data.remote.api.TripkeunApiService
import com.arkhe.menu.data.remote.api.safeApiCall
import com.arkhe.menu.data.remote.dto.CategoryResponseDto
import com.arkhe.menu.data.remote.dto.ProductResponseDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import com.arkhe.menu.domain.model.ApiResult

class RemoteDataSource(
    private val apiService: TripkeunApiService
) {
    suspend fun getProfiles(sessionToken: String): ApiResult<ProfileResponseDto> {
        return safeApiCall {
            apiService.getProfiles(sessionToken)
        }
    }

    suspend fun getCategories(sessionToken: String): ApiResult<CategoryResponseDto> {
        return safeApiCall {
            apiService.getCategories(sessionToken)
        }
    }

    suspend fun getProducts(
        sessionToken: String,
        productCategoryId: String = "ALL"
    ): ApiResult<ProductResponseDto> {
        return safeApiCall {
            apiService.getProducts(sessionToken, productCategoryId)
        }
    }
}