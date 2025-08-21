package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReceiptDto(
    val id: Long = 0,
    val receiptNumber: String,
    val customerName: String,
    val customerEmail: String? = null,
    val customerPhone: String? = null,
    val items: List<ReceiptItemDto>,
    val subtotal: Double,
    val tax: Double,
    val discount: Double = 0.0,
    val total: Double,
    val paymentMethod: String,
    val paymentStatus: String,
    val notes: String? = null,
    val createdBy: String,
    val createdAt: String,
    val updatedAt: String
)