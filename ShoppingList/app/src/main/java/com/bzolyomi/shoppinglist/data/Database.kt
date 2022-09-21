package com.bzolyomi.shoppinglist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingListEntity::class, ShoppingGroupEntity::class],
    version = 1,
    exportSchema = true
)
//@AutoMigration(from = 1, to = 2)
abstract class Database : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
}