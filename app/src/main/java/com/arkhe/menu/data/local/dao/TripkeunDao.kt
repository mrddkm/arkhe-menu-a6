@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.data.local.dao

import androidx.room.*
import com.arkhe.menu.data.local.entity.TripkeunEntity
import com.arkhe.menu.domain.model.TripCategory
import com.arkhe.menu.domain.model.TripStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TripkeunDao {
    @Query("SELECT * FROM trips ORDER BY createdAt DESC")
    fun getAllTrips(): Flow<List<TripkeunEntity>>

    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun getTripById(id: Long): TripkeunEntity?

    @Query("SELECT * FROM trips WHERE category = :category ORDER BY startDate ASC")
    fun getTripsByCategory(category: TripCategory): Flow<List<TripkeunEntity>>

    @Query("SELECT * FROM trips WHERE status = :status ORDER BY startDate ASC")
    fun getTripsByStatus(status: TripStatus): Flow<List<TripkeunEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripkeunEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrips(trips: List<TripkeunEntity>)

    @Update
    suspend fun updateTrip(trip: TripkeunEntity)

    @Delete
    suspend fun deleteTrip(trip: TripkeunEntity)

    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteTripById(id: Long)

    @Query("DELETE FROM trips")
    suspend fun deleteAllTrips()
}