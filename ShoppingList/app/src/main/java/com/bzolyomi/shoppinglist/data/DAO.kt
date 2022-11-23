package com.bzolyomi.shoppinglist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createGroup(group: ShoppingGroupEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createItem(item: ShoppingItemEntity)

    @Transaction
    @Query("SELECT * FROM shopping_group")
    fun getAll(): Flow<List<GroupWithList>>
}