package com.arkhe.menu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arkhe.menu.utils.Constants

@Entity(tableName = Constants.Database.PROFILE_TABLE)
data class ProfileEntity(
    @PrimaryKey
    val action: String,
    val actionInformationId: String,
    val actionInformationEn: String,
    val nameShort: String,
    val nameLong: String,
    val birthDate: String,
    val logo: String,
    val googleMaps: String,
    val whatsApp: String,
    val instagram: String,
    val tiktok: String,
    val youtube: String,
    val tagline: String,
    val quotes: String,
    val informationId: String,
    val informationEn: String,
    val createdAt: String = "",
    val updatedAt: String = "",
    val createdByUserId: String = "",
    val updatedByUserId: String = "",
    val lastSyncTime: Long = System.currentTimeMillis()
)