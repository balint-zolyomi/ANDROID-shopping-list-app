package com.bzolyomi.shoppinglist.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//@ViewModelScoped
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
        return dao.getGroupWithList(groupId)
    }

    // DELETE
    suspend fun deleteGroup(groupId: Long?) {
        dao.deleteGroup(groupId = groupId)
    }

    suspend fun deleteItem(itemId: Long?) {
        dao.deleteItem(itemId = itemId)
    }
}