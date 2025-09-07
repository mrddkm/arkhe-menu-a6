package com.arkhe.menu.data.repository

import android.util.Log
import com.arkhe.menu.data.local.LocalDataSource
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.mapper.toDomainList
import com.arkhe.menu.data.mapper.toEntityList
import com.arkhe.menu.data.remote.RemoteDataSource
import com.arkhe.menu.data.remote.api.NetworkErrorHandler
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.NetworkException
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductGroup
import com.arkhe.menu.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class ProductRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ProductRepository {

    companion object {
        private const val TAG = "ProductRepository"
    }

    override suspend fun getProducts(
        sessionToken: String,
        productCategoryId: String,
        forceRefresh: Boolean
    ): Flow<SafeApiResult<List<Product>>> = flow {
        Log.d(TAG, "getProducts called - categoryId: $productCategoryId, forceRefresh: $forceRefresh")
        emit(SafeApiResult.Loading)

        // Check if we have local data first
        val hasLocalData = try {
            localDataSource.hasProducts()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to check local products data: ${e.message}")
            false
        }

        Log.d(TAG, "Has local products data: $hasLocalData")

        // Emit cached data first if available and not forcing refresh
        if (!forceRefresh && hasLocalData) {
            try {
                Log.d(TAG, "Loading cached products...")
                localDataSource.getAllProducts()
                    .take(1)
                    .collect { entities ->
                        Log.d(TAG, "Found ${entities.size} cached products")
                        if (entities.isNotEmpty()) {
                            emit(SafeApiResult.Success(entities.toDomainList()))
                        }
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to load cached products: ${e.message}")
            }
        }

        // Always fetch fresh data from remote if:
        // 1. Force refresh is requested, OR
        // 2. No local data exists (first install)
        if (forceRefresh || !hasLocalData) {
            Log.d(TAG, "Fetching products from remote API... (forceRefresh: $forceRefresh, hasLocalData: $hasLocalData)")
            when (val remoteResult = remoteDataSource.getProducts(sessionToken, productCategoryId)) {
                is SafeApiResult.Success -> {
                    Log.d(TAG, "Remote API success - status: ${remoteResult.data.status}")
                    Log.d(TAG, "Products count: ${remoteResult.data.data.size}")

                    when {
                        remoteResult.data.status == "success" && remoteResult.data.data.isNotEmpty() -> {
                            Log.d(TAG, "Processing successful response with products data")
                            try {
                                // Save to local database
                                val entities = remoteResult.data.toEntityList()
                                Log.d(TAG, "Saving ${entities.size} product entities to local database")
                                if (productCategoryId == "ALL") {
                                    localDataSource.deleteAllProducts()
                                }
                                localDataSource.insertProducts(entities)

                                // Emit fresh data
                                emit(SafeApiResult.Success(remoteResult.data.toDomainList()))
                            } catch (e: Exception) {
                                Log.e(TAG, "Failed to save products to local database: ${e.message}", e)
                                // Even if local save fails, still return remote data
                                emit(SafeApiResult.Success(remoteResult.data.toDomainList()))
                            }
                        }

                        remoteResult.data.status == "debug" -> {
                            Log.w(TAG, "API returned debug response: ${remoteResult.data.message}")
                            emit(SafeApiResult.Error(Exception("Debug: ${remoteResult.data.message}")))
                        }

                        remoteResult.data.data.isEmpty() -> {
                            Log.w(TAG, "API returned empty products array")
                            emit(SafeApiResult.Error(Exception("API returned empty products array")))
                        }

                        else -> {
                            Log.w(TAG, "API returned unsuccessful status: ${remoteResult.data.status}")
                            emit(SafeApiResult.Error(Exception("API error - Status: ${remoteResult.data.status}, Message: ${remoteResult.data.message}")))
                        }
                    }
                }

                is SafeApiResult.Error -> {
                    Log.e(TAG, "Remote API error: ${remoteResult.exception.message}")

                    // Always emit error if it's the first install or force refresh
                    val errorMessage = if (remoteResult.exception is NetworkException) {
                        NetworkErrorHandler.getErrorMessage(remoteResult.exception)
                    } else {
                        remoteResult.exception.message ?: "Unknown error occurred"
                    }
                    emit(SafeApiResult.Error(Exception(errorMessage)))
                    // If we have local data and it's not a forced refresh, we already emitted the cached data above
                }

                SafeApiResult.Loading -> {
                    Log.w(TAG, "Remote API returned Loading state - this shouldn't happen")
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProduct(id: String): Product? {
        return try {
            Log.d(TAG, "Getting product for id: $id")
            localDataSource.getProduct(id)?.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get product: ${e.message}", e)
            null
        }
    }

    override suspend fun getProductsByCategory(categoryId: String): Flow<List<Product>> {
        return localDataSource.getProductsByCategory(categoryId).map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun getProductsByNamePrefix(namePrefix: String): Flow<List<Product>> {
        return localDataSource.getProductsByNamePrefix(namePrefix).map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun getProductGroups(): List<ProductGroup> {
        return try {
            val allProducts = localDataSource.getAllProducts().take(1)
                .map { it.toDomainList() }

            // Group products by series prefix
            val grouped = mutableMapOf<String, MutableList<Product>>()

            allProducts.collect { products ->
                products.forEach { product ->
                    val prefix = product.seriesPrefix
                    grouped.getOrPut(prefix) { mutableListOf() }.add(product)
                }
            }

            grouped.map { (seriesName, products) ->
                ProductGroup(seriesName, products.sortedBy { it.productCode })
            }.sortedBy { it.seriesName }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get product groups: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun refreshProducts(sessionToken: String, productCategoryId: String): SafeApiResult<List<Product>> {
        Log.d(TAG, "refreshProducts called with token: $sessionToken, categoryId: $productCategoryId")
        return when (val remoteResult = remoteDataSource.getProducts(sessionToken, productCategoryId)) {
            is SafeApiResult.Success -> {
                Log.d(TAG, "Refresh success - status: ${remoteResult.data.status}, data count: ${remoteResult.data.data.size}")

                when {
                    remoteResult.data.status == "success" && remoteResult.data.data.isNotEmpty() -> {
                        try {
                            val entities = remoteResult.data.toEntityList()
                            if (productCategoryId == "ALL") {
                                localDataSource.deleteAllProducts()
                            }
                            localDataSource.insertProducts(entities)

                            SafeApiResult.Success(remoteResult.data.toDomainList())
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to save refreshed products data: ${e.message}", e)
                            SafeApiResult.Success(remoteResult.data.toDomainList())
                        }
                    }

                    remoteResult.data.status == "debug" -> {
                        SafeApiResult.Error(Exception("Debug response: ${remoteResult.data.message}"))
                    }

                    else -> {
                        SafeApiResult.Error(Exception("Refresh failed - Status: ${remoteResult.data.status}, Message: ${remoteResult.data.message}"))
                    }
                }
            }

            is SafeApiResult.Error -> {
                Log.e(TAG, "Refresh error: ${remoteResult.exception.message}")
                val errorMessage = if (remoteResult.exception is NetworkException) {
                    NetworkErrorHandler.getErrorMessage(remoteResult.exception)
                } else {
                    remoteResult.exception.message ?: "Unknown error occurred"
                }
                SafeApiResult.Error(Exception(errorMessage))
            }

            SafeApiResult.Loading -> {
                Log.w(TAG, "Refresh returned Loading state - this shouldn't happen")
                SafeApiResult.Error(Exception("Unexpected loading state"))
            }
        }
    }
}