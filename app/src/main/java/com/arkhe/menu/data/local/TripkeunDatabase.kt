@file:Suppress("SpellCheckingInspection")

package com.arkhe.menu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arkhe.menu.data.local.converter.Converters
import com.arkhe.menu.data.local.dao.ReceiptDao
import com.arkhe.menu.data.local.dao.TripkeunDao
import com.arkhe.menu.data.local.entity.ReceiptEntity
import com.arkhe.menu.data.local.entity.ReceiptItemEntity
import com.arkhe.menu.data.local.entity.TripkeunEntity

@Database(
    entities = [
        TripkeunEntity::class,
        ReceiptEntity::class,
        ReceiptItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TripkeunDatabase : RoomDatabase() {

    abstract fun tripkeunDao(): TripkeunDao
    abstract fun receiptDao(): ReceiptDao

    companion object {
        const val DATABASE_NAME = "tripkeun_database"
    }
}