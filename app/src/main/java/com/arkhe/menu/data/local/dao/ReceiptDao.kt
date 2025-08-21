package com.arkhe.menu.data.local.dao

import androidx.room.*
import com.arkhe.menu.data.local.entity.ReceiptEntity
import com.arkhe.menu.data.local.entity.ReceiptItemEntity
import com.arkhe.menu.domain.model.PaymentStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {
    @Query("SELECT * FROM receipts ORDER BY createdAt DESC")
    fun getAllReceipts(): Flow<List<ReceiptEntity>>

    @Query("SELECT * FROM receipts WHERE id = :id")
    suspend fun getReceiptById(id: Long): ReceiptEntity?

    @Query("SELECT * FROM receipt_items WHERE receiptId = :receiptId")
    suspend fun getReceiptItems(receiptId: Long): List<ReceiptItemEntity>

    @Query("SELECT * FROM receipts WHERE paymentStatus = :status ORDER BY createdAt DESC")
    fun getReceiptsByStatus(status: PaymentStatus): Flow<List<ReceiptEntity>>

    @Query("SELECT * FROM receipts WHERE createdBy = :userId ORDER BY createdAt DESC")
    fun getReceiptsByUser(userId: String): Flow<List<ReceiptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(receipt: ReceiptEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceiptItems(items: List<ReceiptItemEntity>)

    @Update
    suspend fun updateReceipt(receipt: ReceiptEntity)

    @Delete
    suspend fun deleteReceipt(receipt: ReceiptEntity)

    @Query("DELETE FROM receipts WHERE id = :id")
    suspend fun deleteReceiptById(id: Long)

    @Query("DELETE FROM receipt_items WHERE receiptId = :receiptId")
    suspend fun deleteReceiptItems(receiptId: Long)

    @Transaction
    suspend fun deleteReceiptWithItems(receiptId: Long) {
        deleteReceiptItems(receiptId)
        deleteReceiptById(receiptId)
    }
}