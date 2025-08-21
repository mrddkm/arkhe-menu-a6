package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReceiptItemDto(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val quantity: Int,
    val unitPrice: Double,
    val totalPrice: Double
)