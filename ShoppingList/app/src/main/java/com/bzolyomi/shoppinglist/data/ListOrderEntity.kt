package com.bzolyomi.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bzolyomi.shoppinglist.util.Constants.LIST_ORDER_TABLE
import com.bzolyomi.shoppinglist.util.Constants.LIST_ORDER_TABLE_COLUMN_GROUP_ID
import com.bzolyomi.shoppinglist.util.Constants.LIST_ORDER_TABLE_COLUMN_ITEM_ID
import com.bzolyomi.shoppinglist.util.Constants.LIST_ORDER_TABLE_COLUMN_ITEM_POSITION
import com.bzolyomi.shoppinglist.util.Constants.LIST_ORDER_TABLE_COLUMN_LIST_ORDER_ID

@Entity(tableName = LIST_ORDER_TABLE)
data class ListOrderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = LIST_ORDER_TABLE_COLUMN_LIST_ORDER_ID) val listOrderId: Long?,
    @ColumnInfo(name = LIST_ORDER_TABLE_COLUMN_GROUP_ID) val groupId: Long?,
    @ColumnInfo(name = LIST_ORDER_TABLE_COLUMN_ITEM_ID) val itemId: Long?,
    @ColumnInfo(name = LIST_ORDER_TABLE_COLUMN_ITEM_POSITION) var itemPositionInList: Int
)
