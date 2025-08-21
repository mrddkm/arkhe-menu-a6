@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arkhe.menu.domain.model.TripCategory
import com.arkhe.menu.domain.model.TripStatus
import java.util.Date

@Entity(tableName = "tripkeun")
data class TripkeunEntity(
    @PrimaryKey(autoGenerate = true)
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