package com.arkhe.menu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arkhe.menu.utils.Constants

@Entity(tableName = Constants.Database.CATEGORIES_TABLE)
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val productCount: String,
    val initiation: String,
    val research: String,
    val ready: String,
    val informationId: String,
    val informationEn: String,
    val colorBackground: String,
    val colorIcon: String,
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String,
    val lastSyncTime: Long = System.currentTimeMillis()
)