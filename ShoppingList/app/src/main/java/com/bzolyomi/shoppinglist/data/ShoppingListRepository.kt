package com.bzolyomi.shoppinglist.data

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ShoppingListRepository @Inject constructor(
    private val dao: ShoppingListDao) {

    val allShoppingLists: Flow<List<ShoppingListEntity>> = dao.getAll()
}