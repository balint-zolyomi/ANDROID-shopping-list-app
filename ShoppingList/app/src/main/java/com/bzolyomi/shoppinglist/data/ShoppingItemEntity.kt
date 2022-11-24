package com.bzolyomi.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_LIST_TABLE
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_LIST_TABLE_COLUMN_GROUP_ID
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_LIST_TABLE_COLUMN_ITEM_CHECKED
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_LIST_TABLE_COLUMN_ITEM_ID
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_LIST_TABLE_COLUMN_ITEM_NAME
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_LIST_TABLE_COLUMN_ITEM_POSITION_IN_LIST
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_LIST_TABLE_COLUMN_ITEM_QUANTITY
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_LIST_TABLE_COLUMN_ITEM_UNIT

@Entity(tableName = SHOPPING_LIST_TABLE)
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SHOPPING_LIST_TABLE_COLUMN_ITEM_ID)
    var itemId: Long?,
    @ColumnInfo(name = SHOPPING_LIST_TABLE_COLUMN_GROUP_ID) var itemParentId: Long?,
    @ColumnInfo(name = SHOPPING_LIST_TABLE_COLUMN_ITEM_NAME) val itemName: String,
    @ColumnInfo(name = SHOPPING_LIST_TABLE_COLUMN_ITEM_QUANTITY) var itemQuantity: Float?,
    @ColumnInfo(name = SHOPPING_LIST_TABLE_COLUMN_ITEM_UNIT) val itemUnit: String,
    @ColumnInfo(name = SHOPPING_LIST_TABLE_COLUMN_ITEM_CHECKED) var isItemChecked: Boolean,
    @ColumnInfo(name = SHOPPING_LIST_TABLE_COLUMN_ITEM_POSITION_IN_LIST) var itemPositionInList: Int?
)
