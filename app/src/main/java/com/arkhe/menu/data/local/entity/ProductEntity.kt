package com.arkhe.menu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arkhe.menu.utils.Constants

@Entity(tableName = Constants.Database.PRODUCT_TABLE)
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val productCategoryId: String,
    val categoryName: String,
    val categoryType: String,
    val productCode: String,
    val productFullName: String,
    val productTagLine: String,
    val productDestination: String,
    val logo: String,
    val status: String,
    val hikeDistance: String,
    val hikeDuration: String,
    val hikeElevationGain: String,
    val hikeAltitude: String,
    val hikeLevelId: String,
    val hikeLevelName: String,
    val hikeLevelInformationId: String,
    val hikeLevelInformationEn: String,
    val informationId: String,
    val informationEn: String,
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String,
    val lastSyncTime: Long = System.currentTimeMillis(),
    val localImagePath: String? = null
)