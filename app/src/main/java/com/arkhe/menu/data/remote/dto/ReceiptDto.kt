package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReceiptDto(
    val id: Long,
    val receiptNumber: String,
    val customerName: String,
    val customerEmail: String? = null,
    val customerPhone: String? = null,
    val items: List<ReceiptItemDto>,
    val subtotal: Double,
    val tax: Double,
    val discount: Double,
    val total: Double,
    val paymentMethod: String,
    val paymentStatus: String,
    val notes: String? = null,
    val createdBy: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class ReceiptItemDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)