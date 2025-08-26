package com.arkhe.menu.data.local.dao

import androidx.room.*
import com.arkhe.menu.data.local.entity.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles WHERE nameShort = :nameShort LIMIT 1")
    suspend fun getProfile(nameShort: String): ProfileEntity?

    @Query("SELECT * FROM profiles")
    fun getAllProfiles(): Flow<List<ProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(profiles: List<ProfileEntity>)

    @Query("DELETE FROM profiles WHERE nameShort = :nameShort")
    suspend fun deleteProfile(nameShort: String)

    @Query("DELETE FROM profiles")
    suspend fun deleteAllProfiles()

    @Query("SELECT COUNT(*) FROM profiles")
    suspend fun getProfileCount(): Int
}