package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TripDataDto(
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val startDate: String, // ISO date format
    val endDate: String,
    val location: String,
    val maxParticipants: Int,
    val currentParticipants: Int,
    val price: Double,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)