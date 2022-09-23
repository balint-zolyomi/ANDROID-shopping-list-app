package com.bzolyomi.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_GROUP_TABLE_NAME

@Entity(tableName = SHOPPING_GROUP_TABLE_NAME)
data class ShoppingGroupEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "group_id") val id: Long?,
    @ColumnInfo(name = "group_name") val groupName: String
)
