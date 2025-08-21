@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.data.local.dao

import androidx.room.*
import com.arkhe.menu.data.local.entity.TripkeunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripkeunDao {

    @Query("SELECT * FROM tripkeun ORDER BY startDate ASC")
    fun getAllTrips(): Flow<List<TripkeunEntity>>

    @Query("SELECT * FROM tripkeun WHERE id = :id")
    suspend fun getTripById(id: Long): TripkeunEntity?

    @Query("SELECT * FROM tripkeun WHERE category = :category ORDER BY startDate ASC")
    fun getTripsByCategory(category: String): Flow<List<TripkeunEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripkeunEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(trips: List<TripkeunEntity>)

    @Update
    suspend fun updateTrip(trip: TripkeunEntity)

    @Delete
    suspend fun deleteTrip(trip: TripkeunEntity)

    @Query("DELETE FROM tripkeun WHERE id = :id")
    suspend fun deleteTripById(id: Long)

    @Query("DELETE FROM tripkeun")
    suspend fun deleteAllTrips()
}