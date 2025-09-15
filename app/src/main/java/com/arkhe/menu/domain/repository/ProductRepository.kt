package com.arkhe.menu.domain.repository

import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductGroup
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(
        sessionToken: String,
        productCategoryId: String = "ALL",
        forceRefresh: Boolean = false
    ): Flow<SafeApiResult<List<Product>>>

    suspend fun getProduct(id: String): Product?
    suspend fun getProductsByCategory(categoryId: String): Flow<List<Product>>
    suspend fun getProductsByNamePrefix(namePrefix: String): Flow<List<Product>>
    suspend fun getProductGroups(): List<ProductGroup>
    suspend fun refreshProducts(
        sessionToken: String,
        productCategoryId: String = "ALL"
    ): SafeApiResult<List<Product>>
}