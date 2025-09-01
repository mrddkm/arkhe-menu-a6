package com.arkhe.menu.data.local

import com.arkhe.menu.data.local.dao.CategoryDao
import com.arkhe.menu.data.local.dao.ProductDao
import com.arkhe.menu.data.local.dao.ProfileDao
import com.arkhe.menu.data.local.entity.CategoryEntity
import com.arkhe.menu.data.local.entity.ProductEntity
import com.arkhe.menu.data.local.entity.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataSource(
    private val profileDao: ProfileDao,
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao
) {
    fun getAllProfiles(): Flow<List<ProfileEntity>> {
        return profileDao.getAllProfiles()
    }

    suspend fun getProfile(nameShort: String): ProfileEntity? {
        return withContext(Dispatchers.IO) {
            profileDao.getProfile(nameShort)
        }
    }

    suspend fun insertProfiles(profiles: List<ProfileEntity>) {
        withContext(Dispatchers.IO) {
            profileDao.insertProfiles(profiles)
        }
    }

    suspend fun insertProfile(profile: ProfileEntity) {
        withContext(Dispatchers.IO) {
            profileDao.insertProfile(profile)
        }
    }

    suspend fun deleteProfile(nameShort: String) {
        withContext(Dispatchers.IO) {
            profileDao.deleteProfile(nameShort)
        }
    }

    suspend fun deleteAllProfiles() {
        withContext(Dispatchers.IO) {
            profileDao.deleteAllProfiles()
        }
    }

    suspend fun getProfileCount(): Int {
        return withContext(Dispatchers.IO) {
            profileDao.getProfileCount()
        }
    }

    suspend fun hasProfiles(): Boolean {
        return getProfileCount() > 0
    }

    /*Categories*/
    fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

    suspend fun getCategory(id: String): CategoryEntity? {
        return withContext(Dispatchers.IO) {
            categoryDao.getCategory(id)
        }
    }

    suspend fun insertCategories(categories: List<CategoryEntity>) {
        withContext(Dispatchers.IO) {
            categoryDao.insertCategories(categories)
        }
    }

    suspend fun insertCategory(category: CategoryEntity) {
        withContext(Dispatchers.IO) {
            categoryDao.insertCategory(category)
        }
    }

    suspend fun deleteCategory(id: String) {
        withContext(Dispatchers.IO) {
            categoryDao.deleteCategory(id)
        }
    }

    suspend fun deleteAllCategories() {
        withContext(Dispatchers.IO) {
            categoryDao.deleteAllCategories()
        }
    }

    suspend fun getCategoryCount(): Int {
        return withContext(Dispatchers.IO) {
            categoryDao.getCategoryCount()
        }
    }

    suspend fun hasCategories(): Boolean {
        return getCategoryCount() > 0
    }

    /*Products*/
    fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    suspend fun getProduct(id: String): ProductEntity? {
        return withContext(Dispatchers.IO) {
            productDao.getProduct(id)
        }
    }

    suspend fun getProductsByCategory(categoryId: String): Flow<List<ProductEntity>> {
        return productDao.getProductsByCategory(categoryId)
    }

    suspend fun getProductsByNamePrefix(namePrefix: String): Flow<List<ProductEntity>> {
        return productDao.getProductsByNamePrefix(namePrefix)
    }

    suspend fun insertProducts(products: List<ProductEntity>) {
        withContext(Dispatchers.IO) {
            productDao.insertProducts(products)
        }
    }

    suspend fun insertProduct(product: ProductEntity) {
        withContext(Dispatchers.IO) {
            productDao.insertProduct(product)
        }
    }

    suspend fun deleteProduct(id: String) {
        withContext(Dispatchers.IO) {
            productDao.deleteProduct(id)
        }
    }

    suspend fun deleteAllProducts() {
        withContext(Dispatchers.IO) {
            productDao.deleteAllProducts()
        }
    }

    suspend fun getProductCount(): Int {
        return withContext(Dispatchers.IO) {
            productDao.getProductCount()
        }
    }

    suspend fun hasProducts(): Boolean {
        return getProductCount() > 0
    }
}