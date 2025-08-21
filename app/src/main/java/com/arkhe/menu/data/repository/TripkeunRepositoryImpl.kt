@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.arkhe.menu.data.local.dao.ReceiptDao
import com.arkhe.menu.data.local.dao.TripkeunDao
import com.arkhe.menu.data.remote.TripkeunApiService
import com.arkhe.menu.domain.model.Receipt
import com.arkhe.menu.domain.model.TripkeunData
import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.domain.repository.TripkeunRepository
import com.arkhe.menu.presentation.utils.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class TripkeunRepositoryImpl(
    private val tripkeunDao: TripkeunDao,
    private val receiptDao: ReceiptDao,
    private val apiService: TripkeunApiService,
    private val dataStore: DataStore<Preferences>
) : TripkeunRepository {

    private val userRoleKey = stringPreferencesKey("user_role")

    // User Management
    override suspend fun getUserRole(): Flow<UserRole> {
        return dataStore.data.map { preferences ->
            val roleCode = preferences[userRoleKey] ?: UserRole.FAGA.code
            UserRole.fromCode(roleCode)
        }
    }

    override suspend fun setUserRole(role: UserRole) {
        dataStore.edit { preferences ->
            preferences[userRoleKey] = role.code
        }
    }

    // Trip Data
    override suspend fun getTripData(): Flow<List<TripkeunData>> {
        return tripkeunDao.getAllTrips().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTripById(id: Long): TripkeunData? {
        return tripkeunDao.getTripById(id)?.toDomain()
    }

    override suspend fun insertTrip(trip: TripkeunData): Long {
        return tripkeunDao.insertTrip(trip.toEntity())
    }

    override suspend fun updateTrip(trip: TripkeunData) {
        tripkeunDao.updateTrip(trip.toEntity())
    }

    override suspend fun deleteTrip(id: Long) {
        tripkeunDao.deleteTripById(id)
    }

    // Receipt Management
    override suspend fun getReceipts(): Flow<List<Receipt>> {
        return receiptDao.getAllReceipts().map { entities ->
            entities.map { receiptEntity ->
                val items = receiptDao.getReceiptItems(receiptEntity.id)
                receiptEntity.toDomain(items.map { it.toDomain() })
            }
        }
    }

    override suspend fun getReceiptById(id: Long): Receipt? {
        val receiptEntity = receiptDao.getReceiptById(id) ?: return null
        val items = receiptDao.getReceiptItems(id)
        return receiptEntity.toDomain(items.map { it.toDomain() })
    }

    override suspend fun createReceipt(receipt: Receipt): Long {
        val receiptId = receiptDao.insertReceipt(receipt.toEntity())
        val itemEntities = receipt.items.map { it.toEntity(receiptId) }
        receiptDao.insertReceiptItems(itemEntities)
        return receiptId
    }

    override suspend fun updateReceipt(receipt: Receipt) {
        receiptDao.updateReceipt(receipt.toEntity())
        // Delete existing items and insert new ones
        receiptDao.deleteReceiptItems(receipt.id)
        val itemEntities = receipt.items.map { it.toEntity(receipt.id) }
        receiptDao.insertReceiptItems(itemEntities)
    }

    override suspend fun deleteReceipt(id: Long) {
        receiptDao.deleteReceiptWithItems(id)
    }

    // Remote API calls
    override suspend fun syncTripData(): Result<List<TripkeunData>> {
        return try {
            val result = apiService.getTrips()
            if (result.isSuccess) {
                val dtoList = result.getOrNull() ?: emptyList()
                val domainList = dtoList.map { it.toDomain() }
                val entities = domainList.map { it.toEntity() }
                tripkeunDao.insertTrips(entities)
                Result.success(domainList)
            } else {
                result.map { emptyList() }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncReceipts(): Result<List<Receipt>> {
        return try {
            val result = apiService.getReceipts()
            if (result.isSuccess) {
                val dtoList = result.getOrNull() ?: emptyList()
                val domainList = dtoList.map { it.toDomain() }
                // Sync with local database
                domainList.forEach { receipt ->
                    val receiptId = receiptDao.insertReceipt(receipt.toEntity())
                    val itemEntities = receipt.items.map { it.toEntity(receiptId) }
                    receiptDao.insertReceiptItems(itemEntities)
                }
                Result.success(domainList)
            } else {
                result.map { emptyList() }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}