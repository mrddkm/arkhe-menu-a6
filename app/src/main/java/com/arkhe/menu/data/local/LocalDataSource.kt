package com.arkhe.menu.data.local

import com.arkhe.menu.data.local.dao.CategoryDao
import com.arkhe.menu.data.local.dao.ProfileDao
import com.arkhe.menu.data.local.entity.CategoryEntity
import com.arkhe.menu.data.local.entity.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataSource(
    private val profileDao: ProfileDao,
    private val categoryDao: CategoryDao
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
}