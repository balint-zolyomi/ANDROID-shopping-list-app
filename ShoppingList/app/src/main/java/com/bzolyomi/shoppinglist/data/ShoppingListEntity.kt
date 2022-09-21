package com.bzolyomi.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bzolyomi.shoppinglist.util.Constants.DATABASE_TABLE_NAME

@Entity (tableName = DATABASE_TABLE_NAME)
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "item_name") val itemName: String,
    @ColumnInfo(name = "item_quantity") val itemQuantity: Float,
    @ColumnInfo(name = "item_unit") val itemUnit: String
)
