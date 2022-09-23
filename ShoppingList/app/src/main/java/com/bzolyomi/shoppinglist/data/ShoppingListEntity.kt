package com.bzolyomi.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_LIST_TABLE_NAME

@Entity(tableName = SHOPPING_LIST_TABLE_NAME)
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "item_id") val id: Long?,
    @ColumnInfo(name = "parent_id") var groupId: Long?,
    @ColumnInfo(name = "item_name") val itemName: String,
    @ColumnInfo(name = "item_quantity") val itemQuantity: Float,
    @ColumnInfo(name = "item_unit") val itemUnit: String
)
