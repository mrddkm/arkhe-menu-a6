@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TripkeunDto(
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val startDate: String,
    val endDate: String,
    val location: String,
    val maxParticipants: Int,
    val currentParticipants: Int = 0,
    val price: Double,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)