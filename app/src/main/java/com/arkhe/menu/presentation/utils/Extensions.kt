@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.presentation.utils

import com.arkhe.menu.data.local.entity.TripkeunEntity
import com.arkhe.menu.data.remote.dto.ReceiptDto
import com.arkhe.menu.data.remote.dto.ReceiptItemDto
import com.arkhe.menu.domain.model.PaymentMethod
import com.arkhe.menu.domain.model.PaymentStatus
import com.arkhe.menu.domain.model.Receipt
import com.arkhe.menu.domain.model.ReceiptItem
import com.arkhe.menu.domain.model.TripkeunData
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Date formatting
fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return formatter.format(date)
}

// Currency formatting
fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount)
}

// Receipt number generation
fun generateReceiptNumber(): String {
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HHmmss", Locale.getDefault())
    val date = Date()
    return "TRP-${dateFormat.format(date)}-${timeFormat.format(date)}"
}

// Domain to Entity mappers
fun TripkeunData.toEntity(): TripkeunEntity {
    return TripkeunEntity(
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

fun ReceiptDto.toDomain(): Receipt {
    return Receipt(
        id = id,
        receiptNumber = receiptNumber,
        customerName = customerName,
        customerEmail = customerEmail,
        customerPhone = customerPhone,
        items = items.map { it.toDomain() },
        subtotal = subtotal,
        tax = tax,
        discount = discount,
        total = total,
        paymentMethod = PaymentMethod.valueOf(paymentMethod.uppercase()),
        paymentStatus = PaymentStatus.valueOf(paymentStatus.uppercase()),
        notes = notes,
        createdBy = createdBy,
        createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(createdAt) ?: Date(),
        updatedAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(updatedAt) ?: Date()
    )
}

fun ReceiptItemDto.toDomain(): ReceiptItem {
    return ReceiptItem(
        id = id,
        name = name,
        description = description,
        quantity = quantity,
        unitPrice = unitPrice,
        totalPrice = totalPrice
    )
}