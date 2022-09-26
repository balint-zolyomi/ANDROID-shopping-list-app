package com.bzolyomi.shoppinglist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingItemEntity::class, ShoppingGroupEntity::class],
    version = 1,
    exportSchema = true
)
//@AutoMigration(from = 1, to = 2)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun localDatabaseDao(): LocalDatabaseDao
}