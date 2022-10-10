package com.bzolyomi.shoppinglist.data

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    private val dao: LocalDatabaseDao
) {

    // CREATE
    suspend fun createGroup(group: ShoppingGroupEntity) {
        dao.createGroup(group = group)
    }

    suspend fun createItem(item: ShoppingItemEntity) {
        dao.createItem(item = item)
    }

    // READ
    val allGroupsWithLists: Flow<List<GroupWithList>> = dao.getGroupsWithLists()

    suspend fun getGroupWithList(groupId: Long?): GroupWithList {
        return dao.getGroupWithList(groupId)
    }

    fun getShoppingList(groupId: Long?): Flow<List<ShoppingItemEntity>> {
        return dao.getShoppingList(groupId)
    }

        // Special
    suspend fun getGroupId(groupName: String): Long? {
        return dao.getGroupId(groupName = groupName)
    }

    // UPDATE
    suspend fun updateItem(item: ShoppingItemEntity) {
        dao.updateItem(item = item)
    }

    // DELETE
    suspend fun deleteGroup(groupId: Long?) {
        dao.deleteGroup(groupId = groupId)
    }

    suspend fun deleteItem(itemId: Long?) {
        dao.deleteItem(itemId = itemId)
    }
}