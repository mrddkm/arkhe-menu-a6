package com.arkhe.menu.data.local

import com.arkhe.menu.data.local.dao.ProfileDao
import com.arkhe.menu.data.local.entity.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataSource(
    private val profileDao: ProfileDao
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
}