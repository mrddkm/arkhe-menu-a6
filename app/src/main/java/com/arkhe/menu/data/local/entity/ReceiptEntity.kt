@file:Suppress("SpellCheckingInspection")

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
    val discount: Double = 0.0,
    val total: Double,
    val paymentMethod: PaymentMethod,
    val paymentStatus: PaymentStatus,
    val notes: String?,
    val createdBy: String,
    val createdAt: Date,
    val updatedAt: Date
)