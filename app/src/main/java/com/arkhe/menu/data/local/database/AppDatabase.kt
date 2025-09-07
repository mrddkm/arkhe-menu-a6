package com.arkhe.menu.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arkhe.menu.data.local.dao.CategoryDao
import com.arkhe.menu.data.local.dao.ProductDao
import com.arkhe.menu.data.local.dao.ProfileDao
import com.arkhe.menu.data.local.entity.CategoryEntity
import com.arkhe.menu.data.local.entity.ProductEntity
import com.arkhe.menu.data.local.entity.ProfileEntity
import com.arkhe.menu.utils.Constants

@Database(
    entities = [ProfileEntity::class, CategoryEntity::class, ProductEntity::class],
    version = Constants.Database.DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao

    companion object {
        const val DATABASE_NAME = Constants.Database.DATABASE_NAME
    }
}