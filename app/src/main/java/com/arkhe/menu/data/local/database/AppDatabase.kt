package com.arkhe.menu.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arkhe.menu.data.local.dao.ProfileDao
import com.arkhe.menu.data.local.entity.ProfileEntity

@Database(
    entities = [ProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object {
        const val DATABASE_NAME = "arkhe_menu_database"
    }
}