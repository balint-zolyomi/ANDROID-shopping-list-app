package com.bzolyomi.shoppinglist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Transaction
    @Query("SELECT * FROM shopping_group")
    fun getShoppingGroupsWithShoppingLists(): Flow<List<GroupWithLists>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createGroup(group: ShoppingGroupEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createItem(item: ShoppingListEntity)

    @Query("SELECT SG.group_id FROM shopping_group SG WHERE SG.group_name=:groupName")
    fun getGroupId(groupName: String): Flow<Long?>

    @Transaction
    @Query("SELECT * FROM shopping_group SG WHERE SG.group_id=:groupId")
    fun getGroupWithList(groupId: Long?): Flow<GroupWithLists>

    @Query("DELETE FROM shopping_list WHERE item_id=:itemId")
    suspend fun deleteItem(itemId: Long?)

    @Query("DELETE FROM shopping_group WHERE group_id=:groupId")
    suspend fun deleteGroup(groupId: Long?)

    @Update
    suspend fun updateItem(shoppingListItem: ShoppingListEntity)
}