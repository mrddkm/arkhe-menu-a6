package com.arkhe.menu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arkhe.menu.domain.model.PaymentMethod
import com.arkhe.menu.domain.model.PaymentStatus
import java.util.Date

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val receiptNumber: String,
    val customerName: String,
    val customerEmail: String?,
    val customerPhone: String?,
    val subtotal: Double,
    val tax: Double,
    val discount: Double,
    val total: Double,
    val paymentMethod: PaymentMethod,
    val paymentStatus: PaymentStatus,
    val notes: String?,
    val createdBy: String,
    val createdAt: Date,
    val updatedAt: Date
)

@Entity(
    tableName = "receipt_items",
    foreignKeys = [androidx.room.ForeignKey(
        entity = ReceiptEntity::class,
        parentColumns = ["id"],
        childColumns = ["receiptId"],
        onDelete = androidx.room.ForeignKey.CASCADE
    )]
)
data class ReceiptItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val receiptId: Long,
    val name: String,
    val description: String?,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)