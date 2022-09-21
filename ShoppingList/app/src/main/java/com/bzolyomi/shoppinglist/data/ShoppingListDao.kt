package com.bzolyomi.shoppinglist.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_list")
    fun getAll(): Flow<List<ShoppingListEntity>>

    @Transaction
    @Query("SELECT * FROM shopping_group")
    fun getShoppingGroupsWithShoppingLists(): Flow<List<GroupWithLists>>
}