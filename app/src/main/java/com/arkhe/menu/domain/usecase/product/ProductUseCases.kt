package com.arkhe.menu.domain.usecase.product

import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductGroup
import com.arkhe.menu.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        sessionToken: String,
        productCategoryId: String = "ALL",
        forceRefresh: Boolean = false
    ): Flow<SafeApiResult<List<Product>>> {
        return repository.getProducts(sessionToken, productCategoryId, forceRefresh)
    }
}

class GetProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: String): Product? {
        return repository.getProduct(id)
    }
}

class GetProductsByCategoryUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(categoryId: String): Flow<List<Product>> {
        return repository.getProductsByCategory(categoryId)
    }
}

class GetProductsByNamePrefixUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(namePrefix: String): Flow<List<Product>> {
        return repository.getProductsByNamePrefix(namePrefix)
    }
}

class GetProductGroupsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): List<ProductGroup> {
        return repository.getProductGroups()
    }
}

class RefreshProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        sessionToken: String,
        productCategoryId: String = "ALL"
    ): SafeApiResult<List<Product>> {
        return repository.refreshProducts(sessionToken, productCategoryId)
    }
}

data class ProductUseCases(
    val getProducts: GetProductsUseCase,
    val getProduct: GetProductUseCase,
    val getProductsByCategory: GetProductsByCategoryUseCase,
    val getProductsByNamePrefix: GetProductsByNamePrefixUseCase,
    val getProductGroups: GetProductGroupsUseCase,
    val refreshProducts: RefreshProductsUseCase
)