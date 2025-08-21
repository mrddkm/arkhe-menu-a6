// TripkeunRepositoryImpl.kt
@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.arkhe.menu.data.local.dao.ReceiptDao
import com.arkhe.menu.data.local.dao.TripkeunDao
import com.arkhe.menu.data.local.entity.ReceiptEntity
import com.arkhe.menu.data.local.entity.ReceiptItemEntity
import com.arkhe.menu.data.remote.TripkeunApiService
import com.arkhe.menu.domain.model.Receipt
import com.arkhe.menu.domain.model.ReceiptItem
import com.arkhe.menu.domain.model.TripkeunData
import com.arkhe.menu.domain.model.UserRole
import com.arkhe.menu.domain.repository.TripkeunRepository
import com.arkhe.menu.presentation.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripkeunRepositoryImpl(
    private val tripkeunDao: TripkeunDao,
    private val receiptDao: ReceiptDao,
    private val apiService: TripkeunApiService,
    private val dataStore: DataStore<Preferences>
) : TripkeunRepository {

    private val userRoleKey = stringPreferencesKey(Constants.SharedPrefsKeys.USER_ROLE)

    override suspend fun getUserRole(): Flow<UserRole> {
        return dataStore.data.map { preferences ->
            val roleCode = preferences[userRoleKey] ?: Constants.UserRoles.FAGA
            UserRole.fromCode(roleCode)
        }
    }

    override suspend fun setUserRole(role: UserRole) {
        dataStore.edit { preferences ->
            preferences[userRoleKey] = role.code
        }
    }

    override suspend fun getTripData(): Flow<List<TripkeunData>> {
        return tripkeunDao.getAllTrips().map { entities ->
            entities.map { entity ->
                TripkeunData(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    category = entity.category,
                    startDate = entity.startDate,
                    endDate = entity.endDate,
                    location = entity.location,
                    maxParticipants = entity.maxParticipants,
                    currentParticipants = entity.currentParticipants,
                    price = entity.price,
                    status = entity.status,
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt
                )
            }
        }
    }

    override suspend fun getTripById(id: Long): TripkeunData? {
        return tripkeunDao.getTripById(id)?.let { entity ->
            TripkeunData(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                category = entity.category,
                startDate = entity.startDate,
                endDate = entity.endDate,
                location = entity.location,
                maxParticipants = entity.maxParticipants,
                currentParticipants = entity.currentParticipants,
                price = entity.price,
                status = entity.status,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
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

    override suspend fun getReceipts(): Flow<List<Receipt>> {
        return receiptDao.getAllReceiptsWithItems().map { receiptWithItems ->
            receiptWithItems.map { receiptEntity ->
                Receipt(
                    id = receiptEntity.receipt.id,
                    receiptNumber = receiptEntity.receipt.receiptNumber,
                    customerName = receiptEntity.receipt.customerName,
                    customerEmail = receiptEntity.receipt.customerEmail,
                    customerPhone = receiptEntity.receipt.customerPhone,
                    items = receiptEntity.items.map { itemEntity ->
                        ReceiptItem(
                            id = itemEntity.id,
                            name = itemEntity.name,
                            description = itemEntity.description,
                            quantity = itemEntity.quantity,
                            unitPrice = itemEntity.unitPrice,
                            totalPrice = itemEntity.totalPrice
                        )
                    },
                    subtotal = receiptEntity.receipt.subtotal,
                    tax = receiptEntity.receipt.tax,
                    discount = receiptEntity.receipt.discount,
                    total = receiptEntity.receipt.total,
                    paymentMethod = receiptEntity.receipt.paymentMethod,
                    paymentStatus = receiptEntity.receipt.paymentStatus,
                    notes = receiptEntity.receipt.notes,
                    createdBy = receiptEntity.receipt.createdBy,
                    createdAt = receiptEntity.receipt.createdAt,
                    updatedAt = receiptEntity.receipt.updatedAt
                )
            }
        }
    }

    override suspend fun getReceiptById(id: Long): Receipt? {
        return receiptDao.getReceiptWithItems(id)?.let { receiptWithItems ->
            Receipt(
                id = receiptWithItems.receipt.id,
                receiptNumber = receiptWithItems.receipt.receiptNumber,
                customerName = receiptWithItems.receipt.customerName,
                customerEmail = receiptWithItems.receipt.customerEmail,
                customerPhone = receiptWithItems.receipt.customerPhone,
                items = receiptWithItems.items.map { itemEntity ->
                    ReceiptItem(
                        id = itemEntity.id,
                        name = itemEntity.name,
                        description = itemEntity.description,
                        quantity = itemEntity.quantity,
                        unitPrice = itemEntity.unitPrice,
                        totalPrice = itemEntity.totalPrice
                    )
                },
                subtotal = receiptWithItems.receipt.subtotal,
                tax = receiptWithItems.receipt.tax,
                discount = receiptWithItems.receipt.discount,
                total = receiptWithItems.receipt.total,
                paymentMethod = receiptWithItems.receipt.paymentMethod,
                paymentStatus = receiptWithItems.receipt.paymentStatus,
                notes = receiptWithItems.receipt.notes,
                createdBy = receiptWithItems.receipt.createdBy,
                createdAt = receiptWithItems.receipt.createdAt,
                updatedAt = receiptWithItems.receipt.updatedAt
            )
        }
    }

    override suspend fun createReceipt(receipt: Receipt): Long {
        val receiptEntity = ReceiptEntity(
            receiptNumber = receipt.receiptNumber,
            customerName = receipt.customerName,
            customerEmail = receipt.customerEmail,
            customerPhone = receipt.customerPhone,
            subtotal = receipt.subtotal,
            tax = receipt.tax,
            discount = receipt.discount,
            total = receipt.total,
            paymentMethod = receipt.paymentMethod,
            paymentStatus = receipt.paymentStatus,
            notes = receipt.notes,
            createdBy = receipt.createdBy,
            createdAt = receipt.createdAt,
            updatedAt = receipt.updatedAt
        )

        val receiptItems = receipt.items.map { item ->
            ReceiptItemEntity(
                receiptId = 0,
                name = item.name,
                description = item.description,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
                totalPrice = item.totalPrice
            )
        }

        receiptDao.insertReceiptWithItems(receiptEntity, receiptItems)
        return receiptEntity.id
    }

    override suspend fun updateReceipt(receipt: Receipt) {
        val receiptEntity = ReceiptEntity(
            id = receipt.id,
            receiptNumber = receipt.receiptNumber,
            customerName = receipt.customerName,
            customerEmail = receipt.customerEmail,
            customerPhone = receipt.customerPhone,
            subtotal = receipt.subtotal,
            tax = receipt.tax,
            discount = receipt.discount,
            total = receipt.total,
            paymentMethod = receipt.paymentMethod,
            paymentStatus = receipt.paymentStatus,
            notes = receipt.notes,
            createdBy = receipt.createdBy,
            createdAt = receipt.createdAt,
            updatedAt = receipt.updatedAt
        )
        receiptDao.updateReceipt(receiptEntity)
    }

    override suspend fun deleteReceipt(id: Long) {
        receiptDao.deleteReceiptById(id)
    }

    override suspend fun syncTripData(): Result<List<TripkeunData>> {
        return try {
            val trips = apiService.getTripData()
            // Convert and save to local database
            Result.success(emptyList()) // Placeholder
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncReceipts(): Result<List<Receipt>> {
        return try {
            val receipts = apiService.getReceipts()
            // Convert and save to local database
            Result.success(emptyList()) // Placeholder
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun TripkeunData.toEntity(): com.arkhe.menu.data.local.entity.TripkeunEntity {
        return com.arkhe.menu.data.local.entity.TripkeunEntity(
            id = id,
            title = title,
            description = description,
            category = category,
            startDate = startDate,
            endDate = endDate,
            location = location,
            maxParticipants = maxParticipants,
            currentParticipants = currentParticipants,
            price = price,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}