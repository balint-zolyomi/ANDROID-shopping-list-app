package com.bzolyomi.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_GROUP_TABLE
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_GROUP_TABLE_COLUMN_GROUP_ID
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_GROUP_TABLE_COLUMN_GROUP_NAME

@Entity(tableName = SHOPPING_GROUP_TABLE)
data class ShoppingGroupEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SHOPPING_GROUP_TABLE_COLUMN_GROUP_ID) val groupId: Long?,
    @ColumnInfo(name = SHOPPING_GROUP_TABLE_COLUMN_GROUP_NAME) val groupName: String
)
