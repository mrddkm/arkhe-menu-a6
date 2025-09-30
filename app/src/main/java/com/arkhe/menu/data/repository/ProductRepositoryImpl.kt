package com.arkhe.menu.data.repository

import android.util.Log
import com.arkhe.menu.data.local.LocalDataSource
import com.arkhe.menu.data.local.storage.ImageStorageManager
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.mapper.toDomainList
import com.arkhe.menu.data.mapper.toEntityList
import com.arkhe.menu.data.remote.RemoteDataSource
import com.arkhe.menu.data.remote.api.NetworkErrorHandler
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.domain.model.NetworkException
import com.arkhe.menu.domain.model.Product
import com.arkhe.menu.domain.model.ProductByGroup
import com.arkhe.menu.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ProductRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val imageStorageManager: ImageStorageManager
) : ProductRepository {

    companion object {
        private const val TAG = "ProductRepository"
    }

    override suspend fun getProducts(
        sessionToken: String,
        productCategoryId: String,
        forceRefresh: Boolean
    ): Flow<SafeApiResult<List<Product>>> = flow {
        Log.d(
            TAG,
            "getProducts called - categoryId: $productCategoryId, forceRefresh: $forceRefresh"
        )
        emit(SafeApiResult.Loading)

        val hasLocalData = try {
            localDataSource.hasProducts()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to check local products data: ${e.message}")
            false
        }

        Log.d(TAG, "Has local products data: $hasLocalData")

        if (!forceRefresh && hasLocalData) {
            try {
                Log.d(TAG, "Loading cached products...")
                localDataSource.getAllProducts()
                    .take(1)
                    .collect { entities ->
                        Log.d(TAG, "Found ${entities.size} cached products")
                        if (entities.isNotEmpty()) {
                            emit(SafeApiResult.Success(entities.toDomainList()))
                            // Download images in background for products that don't have local images
                            supervisorScope {
                                launch {
                                    downloadMissingImages(entities)
                                }
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to load cached products: ${e.message}")
            }
        }

        if (forceRefresh || !hasLocalData) {
            Log.d(
                TAG,
                "Fetching products from remote API... (forceRefresh: $forceRefresh, hasLocalData: $hasLocalData)"
            )
            when (val remoteResult =
                remoteDataSource.getProducts(sessionToken, productCategoryId)) {
                is SafeApiResult.Success -> {
                    Log.d(TAG, "Remote API success - status: ${remoteResult.data.status}")
                    Log.d(TAG, "Products count: ${remoteResult.data.data.size}")

                    when {
                        remoteResult.data.status == "success" && remoteResult.data.data.isNotEmpty() -> {
                            Log.d(TAG, "Processing successful response with products data")
                            try {
                                val entities = remoteResult.data.toEntityList()
                                Log.d(
                                    TAG,
                                    "Saving ${entities.size} product entities to local database"
                                )
                                if (productCategoryId == "ALL") {
                                    localDataSource.deleteAllProducts()
                                }
                                localDataSource.insertProducts(entities)

                                emit(SafeApiResult.Success(remoteResult.data.toDomainList()))

                                // Download images in background
                                supervisorScope {
                                    launch {
                                        downloadAndUpdateImages(entities)
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    TAG,
                                    "Failed to save products to local database: ${e.message}",
                                    e
                                )
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
                            Log.w(
                                TAG,
                                "API returned unsuccessful status: ${remoteResult.data.status}"
                            )
                            emit(SafeApiResult.Error(Exception("API error - Status: ${remoteResult.data.status}, Message: ${remoteResult.data.message}")))
                        }
                    }
                }

                is SafeApiResult.Error -> {
                    Log.e(TAG, "Remote API error: ${remoteResult.exception.message}")

                    val errorMessage = if (remoteResult.exception is NetworkException) {
                        NetworkErrorHandler.getErrorMessage(remoteResult.exception)
                    } else {
                        remoteResult.exception.message ?: "Unknown error occurred"
                    }
                    emit(SafeApiResult.Error(Exception(errorMessage)))
                }

                SafeApiResult.Loading -> {
                    Log.w(TAG, "Remote API returned Loading state - this shouldn't happen")
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Download images for products that don't have local images yet
     */
    private suspend fun downloadMissingImages(entities: List<com.arkhe.menu.data.local.entity.ProductEntity>) {
        try {
            Log.d(TAG, "Checking for missing images...")
            entities.forEach { entity ->
                if (entity.localImagePath.isNullOrEmpty() && entity.logo.isNotEmpty()) {
                    val fileName = "product_${entity.productCode}_logo"
                    val existingPath = imageStorageManager.getLocalImagePath(fileName)

                    if (existingPath == null) {
                        Log.d(TAG, "Downloading missing image for product: ${entity.productCode}")
                        val localPath =
                            imageStorageManager.downloadAndSaveImage(entity.logo, fileName)
                        if (localPath != null) {
                            localDataSource.updateImagePath(entity.productCode, localPath)
                            Log.d(TAG, "Updated image path for ${entity.productCode}: $localPath")
                        }
                    } else {
                        // Update database with existing path
                        localDataSource.updateImagePath(entity.productCode, existingPath)
                        Log.d(
                            TAG,
                            "Updated database with existing image path for ${entity.productCode}: $existingPath"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading missing images: ${e.message}", e)
        }
    }

    /**
     * Download and update images for new products
     */
    private suspend fun downloadAndUpdateImages(entities: List<com.arkhe.menu.data.local.entity.ProductEntity>) {
        try {
            Log.d(TAG, "Downloading images for new products...")
            entities.forEach { entity ->
                if (entity.logo.isNotEmpty()) {
                    val fileName = "product_${entity.productCode}_logo"
                    val localPath = imageStorageManager.downloadAndSaveImage(entity.logo, fileName)
                    if (localPath != null) {
                        localDataSource.updateImagePath(entity.productCode, localPath)
                        Log.d(
                            TAG,
                            "Downloaded and saved image for ${entity.productCode}: $localPath"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading and updating images: ${e.message}", e)
        }
    }

    override suspend fun getProduct(id: String): Product? {
        return try {
            localDataSource.getProduct(id)?.toDomain()
        } catch (_: Exception) {
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

    override suspend fun getProductGroups(): List<ProductByGroup> {
        return try {
            val allProducts = localDataSource.getAllProducts().take(1)
                .map { it.toDomainList() }

            val grouped = mutableMapOf<String, MutableList<Product>>()

            allProducts.collect { products ->
                products.forEach { product ->
                    val prefix = product.seriesPrefix
                    grouped.getOrPut(prefix) { mutableListOf() }.add(product)
                }
            }

            grouped.map { (seriesName, products) ->
                ProductByGroup(seriesName, products.sortedBy { it.productCode })
            }.sortedBy { it.seriesName }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get product groups: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun refreshProducts(
        sessionToken: String,
        productCategoryId: String
    ): SafeApiResult<List<Product>> {
        Log.d(
            TAG,
            "refreshProducts called with token: $sessionToken, categoryId: $productCategoryId"
        )
        return syncProducts(sessionToken, productCategoryId)
    }

    override suspend fun syncProducts(
        sessionToken: String,
        productCategoryId: String
    ): SafeApiResult<List<Product>> {
        return when (
            val remoteResult = remoteDataSource.getProducts(sessionToken, productCategoryId)
        ) {
            is SafeApiResult.Success -> {
                if (remoteResult.data.status == "success" && remoteResult.data.data.isNotEmpty()) {
                    try {
                        val entities = remoteResult.data.toEntityList().map { entity ->
                            val fileName = "product_${entity.productCode}_logo"
                            val localPath = if (entity.logo.isNotEmpty()) {
                                imageStorageManager.downloadAndSaveImage(entity.logo, fileName)
                            } else null
                            entity.copy(localImagePath = localPath)
                        }
                        if (productCategoryId == "ALL") {
                            localDataSource.deleteAllProducts()
                        }
                        localDataSource.insertProducts(entities)

                        supervisorScope {
                            launch {
                                downloadAndUpdateImages(entities)
                            }
                        }

                        SafeApiResult.Success(entities.toDomainList())
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to save refreshed products data: ${e.message}", e)
                        SafeApiResult.Error(e)
                    }
                } else {
                    SafeApiResult.Error(Exception("No data returned from API"))
                }
            }

            is SafeApiResult.Error -> SafeApiResult.Error(remoteResult.exception)
            SafeApiResult.Loading -> SafeApiResult.Error(Exception("Unexpected loading state"))
        }
    }
}