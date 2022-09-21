package com.bzolyomi.shoppinglist.data

import androidx.room.Dao
import androidx.room.Query
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_list")
    fun getAll(): Flow<List<ShoppingListEntity>>
}