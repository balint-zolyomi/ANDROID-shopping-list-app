package com.bzolyomi.shoppinglist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createGroup(group: ShoppingGroupEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createItem(item: ShoppingItemEntity)

    // READ
    @Transaction
    @Query("SELECT * FROM shopping_group")
    fun getAll(): Flow<List<GroupWithList>>

    @Transaction
    @Query("SELECT * FROM shopping_group SG WHERE SG.group_id=:groupId")
    suspend fun getGroupWithList(groupId: Long?): GroupWithList

    @Transaction
    @Query("SELECT * FROM shopping_list SL WHERE SL.parent_id=:groupId")
    fun getShoppingList(groupId: Long?): Flow<List<ShoppingItemEntity>>

        // Special
    @Query("SELECT SG.group_id FROM shopping_group SG WHERE SG.group_name=:groupName")
    suspend fun getGroupId(groupName: String): Long?

    // UPDATE
    @Update
    suspend fun updateItem(item: ShoppingItemEntity)

    @Update
    suspend fun updateShoppingListOrder(shoppingList: List<ShoppingItemEntity>)

    // DELETE
    @Query("DELETE FROM shopping_group WHERE group_id=:groupId")
    suspend fun deleteGroup(groupId: Long?)

    @Query("DELETE FROM shopping_list WHERE item_id=:itemId")
    suspend fun deleteItem(itemId: Long?)
}