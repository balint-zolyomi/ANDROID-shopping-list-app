package com.bzolyomi.shoppinglist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingListEntity::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
}