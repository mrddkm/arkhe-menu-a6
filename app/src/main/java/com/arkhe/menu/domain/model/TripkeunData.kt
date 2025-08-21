@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.domain.model

import java.util.Date

data class TripkeunData(
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: TripCategory,
    val startDate: Date,
    val endDate: Date,
    val location: String,
    val maxParticipants: Int,
    val currentParticipants: Int = 0,
    val price: Double,
    val status: TripStatus,
    val createdAt: Date,
    val updatedAt: Date
)

enum class TripCategory {
    GENERAL_MONTHLY,
    PERSONAL,
    SPECIAL_EVENT,
    CORPORATE
}

enum class TripStatus {
    PLANNED,
    OPEN_REGISTRATION,
    FULL_BOOKED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}