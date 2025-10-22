package com.arkhe.menu.data.remote.api

import com.arkhe.menu.data.remote.dto.CategoryResponseDto
import com.arkhe.menu.data.remote.dto.ProductResponseDto
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import com.arkhe.menu.data.remote.dto.VerificationResponseDto

interface ArkheApiService {
    suspend fun verification(
        userId: String,
        phone: String,
        mail: String
    ): VerificationResponseDto

    suspend fun getProfiles(sessionToken: String): ProfileResponseDto
    suspend fun getCategories(sessionToken: String): CategoryResponseDto
    suspend fun getProducts(
        sessionToken: String,
        productCategoryId: String = "ALL"
    ): ProductResponseDto
}