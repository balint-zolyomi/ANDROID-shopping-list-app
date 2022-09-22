package com.bzolyomi.shoppinglist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_list")
    fun getAllShoppingListItems(): Flow<List<ShoppingListEntity>>

    @Transaction
    @Query("SELECT * FROM shopping_group")
    fun getShoppingGroupsWithShoppingLists(): Flow<List<GroupWithLists>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGroup(shoppingGroupEntity: ShoppingGroupEntity)
}