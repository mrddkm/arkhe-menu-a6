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
        emit(SafeApiResult.Loading)
        val hasLocalData = try {
            localDataSource.hasProducts()
        } catch (e: Exception) {
            false
        }

        if (!forceRefresh && hasLocalData) {
            try {
                localDataSource.getAllProducts()
                    .take(1)
                    .collect { entities ->
                        if (entities.isNotEmpty()) {
                            emit(SafeApiResult.Success(entities.toDomainList()))
                            /*Download images in background for products that don't have local images*/
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
            when (val remoteResult =
                remoteDataSource.getProducts(sessionToken, productCategoryId)) {
                is SafeApiResult.Success -> {
                    when {
                        remoteResult.data.status == "success" && remoteResult.data.data.isNotEmpty() -> {
                            try {
                                val entities = remoteResult.data.toEntityList()
                                if (productCategoryId == "ALL") {
                                    localDataSource.deleteAllProducts()
                                }
                                localDataSource.insertProducts(entities)

                                emit(SafeApiResult.Success(remoteResult.data.toDomainList()))

                                /*Download images in background*/
                                supervisorScope {
                                    launch {
                                        downloadAndUpdateImages(entities)
                                    }
                                }
                            } catch (e: Exception) {
                                emit(SafeApiResult.Success(remoteResult.data.toDomainList()))
                            }
                        }

                        remoteResult.data.status == "debug" -> {
                            emit(SafeApiResult.Failure(Exception("Debug: ${remoteResult.data.message}")))
                        }

                        remoteResult.data.data.isEmpty() -> {
                            emit(SafeApiResult.Failure(Exception("API returned empty products array")))
                        }

                        else -> {
                            emit(SafeApiResult.Failure(Exception("API error - Status: ${remoteResult.data.status}, Message: ${remoteResult.data.message}")))
                        }
                    }
                }

                is SafeApiResult.Failure -> {
                    val errorMessage = if (remoteResult.exception is NetworkException) {
                        NetworkErrorHandler.getErrorMessage(remoteResult.exception)
                    } else {
                        remoteResult.exception.message ?: "Unknown error occurred"
                    }
                    emit(SafeApiResult.Failure(Exception(errorMessage)))
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
            entities.forEach { entity ->
                if (entity.localImagePath.isNullOrEmpty() && entity.logo.isNotEmpty()) {
                    val fileName = "product_${entity.productCode}_logo"
                    val existingPath = imageStorageManager.getLocalImagePath(fileName)

                    if (existingPath == null) {
                        val localPath =
                            imageStorageManager.downloadAndSaveImage(entity.logo, fileName)
                        if (localPath != null) {
                            localDataSource.updateImagePath(entity.productCode, localPath)
                        }
                    } else {
                        /*Update database with existing path*/
                        localDataSource.updateImagePath(entity.productCode, existingPath)
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
            entities.forEach { entity ->
                if (entity.logo.isNotEmpty()) {
                    val fileName = "product_${entity.productCode}_logo"
                    val localPath = imageStorageManager.downloadAndSaveImage(entity.logo, fileName)
                    if (localPath != null) {
                        localDataSource.updateImagePath(entity.productCode, localPath)
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
            emptyList()
        }
    }

    override suspend fun refreshProducts(
        sessionToken: String,
        productCategoryId: String
    ): SafeApiResult<List<Product>> {
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
                        SafeApiResult.Failure(e)
                    }
                } else {
                    SafeApiResult.Failure(Exception("No data returned from API"))
                }
            }

            is SafeApiResult.Failure -> SafeApiResult.Failure(remoteResult.exception)
            SafeApiResult.Loading -> SafeApiResult.Failure(Exception("Unexpected loading state"))
        }
    }
}