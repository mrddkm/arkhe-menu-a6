@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.domain.repository

import com.arkhe.menu.domain.model.Receipt
import com.arkhe.menu.domain.model.TripkeunData
import com.arkhe.menu.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface TripkeunRepository {
    // User Management
    suspend fun getUserRole(): Flow<UserRole>
    suspend fun setUserRole(role: UserRole)

    // Trip Data
    suspend fun getTripData(): Flow<List<TripkeunData>>
    suspend fun getTripById(id: Long): TripkeunData?
    suspend fun insertTrip(trip: TripkeunData): Long
    suspend fun updateTrip(trip: TripkeunData)
    suspend fun deleteTrip(id: Long)

    // Receipt Management
    suspend fun getReceipts(): Flow<List<Receipt>>
    suspend fun getReceiptById(id: Long): Receipt?
    suspend fun createReceipt(receipt: Receipt): Long
    suspend fun updateReceipt(receipt: Receipt)
    suspend fun deleteReceipt(id: Long)

    // Remote API calls
    suspend fun syncTripData(): Result<List<TripkeunData>>
    suspend fun syncReceipts(): Result<List<Receipt>>
}