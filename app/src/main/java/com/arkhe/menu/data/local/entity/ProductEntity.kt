package com.arkhe.menu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val productCategoryId: String,
    val categoryName: String,
    val categoryType: String,
    val productCode: String,
    val productFullName: String,
    val productDestination: String,
    val status: String,
    val informationId: String,
    val informationEn: String,
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String,
    val lastSyncTime: Long = System.currentTimeMillis()
)