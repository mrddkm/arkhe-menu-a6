package com.arkhe.menu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arkhe.menu.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProduct(id: String): ProductEntity?

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE productCategoryId = :categoryId")
    fun getProductsByCategory(categoryId: String): Flow<List<ProductEntity>>

    @Query("SELECT DISTINCT productFullName FROM products")
    suspend fun getDistinctProductNames(): List<String>

    @Query("SELECT * FROM products WHERE productFullName LIKE :namePrefix || '%'")
    fun getProductsByNamePrefix(namePrefix: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("UPDATE products SET localImagePath = :path WHERE productCode = :productCode")
    suspend fun updateImagePath(productCode: String, path: String?)

    @Query("UPDATE products SET localImagePath = :path WHERE id = :id")
    suspend fun updateImagePathById(id: String, path: String?)

    @Query("UPDATE products SET localImagePath = :path WHERE productCode IN (:productCodes)")
    suspend fun updateImagePaths(productCodes: List<String>, path: String?)

    @Query("SELECT * FROM products WHERE localImagePath IS NULL OR localImagePath = '' AND logo IS NOT NULL AND logo != ''")
    suspend fun getProductsWithoutLocalImages(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE localImagePath IS NOT NULL AND localImagePath != ''")
    suspend fun getProductsWithLocalImages(): List<ProductEntity>

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProduct(id: String)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
}