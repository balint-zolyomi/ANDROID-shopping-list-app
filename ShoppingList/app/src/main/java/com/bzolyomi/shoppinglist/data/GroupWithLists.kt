package com.bzolyomi.shoppinglist.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class GroupWithLists(
    @Embedded val group: ShoppingGroupEntity,
    @Relation(
        parentColumn = "group_id",
        entityColumn = "parent_id"
    )
    val shoppingList: List<ShoppingListEntity>
)
