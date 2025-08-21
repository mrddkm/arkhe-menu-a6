package com.arkhe.menu.data.local.dao

import androidx.room.*
import com.arkhe.menu.data.local.entity.ReceiptEntity
import com.arkhe.menu.data.local.entity.ReceiptItemEntity
import com.arkhe.menu.data.local.entity.ReceiptWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {

    @Transaction
    @Query("SELECT * FROM receipts ORDER BY createdAt DESC")
    fun getAllReceiptsWithItems(): Flow<List<ReceiptWithItems>>

    @Transaction
    @Query("SELECT * FROM receipts WHERE id = :id")
    suspend fun getReceiptWithItems(id: Long): ReceiptWithItems?

    @Query("SELECT * FROM receipts WHERE id = :id")
    suspend fun getReceiptById(id: Long): ReceiptEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(receipt: ReceiptEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceiptItems(items: List<ReceiptItemEntity>)

    @Transaction
    suspend fun insertReceiptWithItems(receipt: ReceiptEntity, items: List<ReceiptItemEntity>) {
        val receiptId = insertReceipt(receipt)
        val itemsWithReceiptId = items.map { it.copy(receiptId = receiptId) }
        insertReceiptItems(itemsWithReceiptId)
    }

    @Update
    suspend fun updateReceipt(receipt: ReceiptEntity)

    @Delete
    suspend fun deleteReceipt(receipt: ReceiptEntity)

    @Query("DELETE FROM receipts WHERE id = :id")
    suspend fun deleteReceiptById(id: Long)

    @Query("DELETE FROM receipt_items WHERE receiptId = :receiptId")
    suspend fun deleteReceiptItems(receiptId: Long)
}
