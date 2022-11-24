package com.bzolyomi.shoppinglist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzolyomi.shoppinglist.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    private val _shoppingGroupsWithLists = MutableStateFlow<List<GroupWithList>>(emptyList())
    val shoppingGroupsWithLists: StateFlow<List<GroupWithList>>
        get() = _shoppingGroupsWithLists

    var selectedGroupWithList by mutableStateOf(
        GroupWithList(
            ShoppingGroupEntity(null, ""),
            emptyList()
        )
    )

    init {
        createDummyDataIfNotExistsCoroutine()
    }

    // READ
    suspend fun getSelectedGroupWithListCoroutine(groupId: String): GroupWithList =
        coroutineScope {
            val groupWithList = async { getSelectedGroupWithList(groupId = groupId) }
            groupWithList.await()
        }

    private suspend fun getSelectedGroupWithList(groupId: String): GroupWithList {
        val id = groupId.toLong()
        return repo.getGroupWithList(groupId = id)
    }

    // CREATE
    private fun createDummyDataIfNotExistsCoroutine() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.allGroupsWithLists.collect {
                _shoppingGroupsWithLists.value = it
                if (it.isEmpty()) {
                    createDummyData()
                }
            }
        }
    }

    private fun createGroupCoroutine(group: ShoppingGroupEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.createGroup(group)
        }
    }

    private fun createItemCoroutine(item: ShoppingItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.createItem(item)
        }
    }

    private fun createDummyData() {
        DummyData.groupsWithList.forEach { groupWithList ->
            createGroupCoroutine(groupWithList.group)
            for (item in groupWithList.shoppingList) {
                createItemCoroutine(item)
            }
        }
    }

    // DELETE
    fun deleteGroup(groupId: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteGroup(groupId = groupId)
        }
    }

    fun deleteItems(shoppingList: List<ShoppingItemEntity>) {
        for (item in shoppingList) {
            viewModelScope.launch(Dispatchers.IO) {
                repo.deleteItem(itemId = item.itemId)
            }
        }
    }
}