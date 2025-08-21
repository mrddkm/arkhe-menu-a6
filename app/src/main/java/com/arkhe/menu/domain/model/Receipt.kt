package com.arkhe.menu.domain.model

import java.util.Date

data class Receipt(
    val id: Long = 0,
    val receiptNumber: String,
    val customerName: String,
    val customerEmail: String?,
    val customerPhone: String?,
    val items: List<ReceiptItem>,
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

data class ReceiptItem(
    val id: Long = 0,
    val name: String,
    val description: String?,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)

enum class PaymentMethod {
    CASH,
    BANK_TRANSFER,
    CREDIT_CARD,
    DIGITAL_WALLET,
    CHECK
}

enum class PaymentStatus {
    PENDING,
    PAID,
    PARTIALLY_PAID,
    OVERDUE,
    CANCELLED
}