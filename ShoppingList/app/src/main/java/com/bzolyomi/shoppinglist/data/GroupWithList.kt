package com.bzolyomi.shoppinglist.data

import androidx.room.Embedded
import androidx.room.Relation
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_GROUP_TABLE_COLUMN_GROUP_ID
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_LIST_TABLE_COLUMN_GROUP_ID

data class GroupWithList(
    @Embedded val group: ShoppingGroupEntity,
    @Relation(
        parentColumn = SHOPPING_GROUP_TABLE_COLUMN_GROUP_ID,
        entityColumn = SHOPPING_LIST_TABLE_COLUMN_GROUP_ID
    )
    val shoppingList: List<ShoppingItemEntity>
)
