package com.bzolyomi.shoppinglist.data

import android.util.Log
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    private val dao: LocalDatabaseDao) {

    // CREATE
    suspend fun createGroup(group: ShoppingGroupEntity) {
        dao.createGroup(group = group)
    }

    suspend fun createItem(item: ShoppingItemEntity) {
        dao.createItem(item = item)
    }
    // READ
    val allGroupsWithLists: Flow<List<GroupWithList>> = dao.getGroupsWithLists()

    fun getGroupWithList(groupId: Long?): Flow<GroupWithList> {
        return dao.getGroupWithList(groupId)
    }

        // Special
    suspend fun getGroupId(groupName: String): Long? {
            Log.d( // TODO DONE
                "balint-debug", "__________\n" +
                        "\ngroupName REPO: $groupName"
            )
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