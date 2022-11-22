package com.bzolyomi.shoppinglist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingItemEntity::class, ShoppingGroupEntity::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun DAO(): DAO
}