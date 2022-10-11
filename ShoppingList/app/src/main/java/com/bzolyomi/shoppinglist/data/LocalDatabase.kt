package com.bzolyomi.shoppinglist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingItemEntity::class, ShoppingGroupEntity::class, ListOrderEntity::class],
    version = 1,
    exportSchema = true // TODO
)
//@AutoMigration(from = 1, to = 2)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun localDatabaseDao(): LocalDatabaseDao
}