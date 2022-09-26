package com.bzolyomi.shoppinglist.data

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ShoppingListRepository @Inject constructor(
    private val dao: ShoppingListDao) {

    val allGroupWithLists: Flow<List<GroupWithList>> = dao.getShoppingGroupsWithShoppingLists()

    suspend fun createGroup(group: ShoppingGroupEntity) {
        dao.createGroup(group = group)
    }

    suspend fun createItem(item: ShoppingListEntity) {
        dao.createItem(item = item)
    }

    fun getGroupId(groupName: String): Flow<Long?> {
        return dao.getGroupId(groupName = groupName)
    }

    fun getGroupWithList(groupId: Long?): Flow<GroupWithList> {
        return dao.getGroupWithList(groupId)
    }

    suspend fun deleteItem(itemId: Long?) {
        dao.deleteItem(itemId = itemId)
    }

    suspend fun deleteGroup(groupId: Long?) {
        dao.deleteGroup(groupId = groupId)
    }

    suspend fun updateItem(shoppingListItem: ShoppingListEntity) {
        dao.updateItem(shoppingListItem = shoppingListItem)
    }
}