package com.bzolyomi.shoppinglist.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val dao: DAO) {

    // CREATE
    suspend fun createGroup(group: ShoppingGroupEntity) {
        dao.createGroup(group = group)
    }

    suspend fun createItem(item: ShoppingItemEntity) {
        dao.createItem(item = item)
    }

    // READ
    val allGroupsWithLists: Flow<List<GroupWithList>> = dao.getAll()

    suspend fun getGroupWithList(groupId: Long?): GroupWithList {
        return dao.getGroupWithList(groupId =  groupId)
    }

    fun getShoppingList(groupId: Long?): Flow<List<ShoppingItemEntity>> {
        return dao.getShoppingList(groupId = groupId)
    }

        // Special
    suspend fun getGroupId(groupName: String): Long? {
        return dao.getGroupId(groupName = groupName)
    }

    // UPDATE
    suspend fun updateItem(item: ShoppingItemEntity) {
        dao.updateItem(item = item)
    }

    suspend fun updateShoppingListOrder(shoppingList: List<ShoppingItemEntity>) {
        dao.updateShoppingListOrder(shoppingList = shoppingList)
    }

    // DELETE
    suspend fun deleteGroup(groupId: Long?) {
        dao.deleteGroup(groupId = groupId)
    }

    suspend fun deleteItem(itemId: Long?) {
        dao.deleteItem(itemId = itemId)
    }
}