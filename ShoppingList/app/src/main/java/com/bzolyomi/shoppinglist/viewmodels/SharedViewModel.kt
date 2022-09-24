package com.bzolyomi.shoppinglist.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzolyomi.shoppinglist.data.GroupWithLists
import com.bzolyomi.shoppinglist.data.ShoppingGroupEntity
import com.bzolyomi.shoppinglist.data.ShoppingListEntity
import com.bzolyomi.shoppinglist.data.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Warning: ViewModels are not part of the Composition. Therefore, you should not hold state
// created in composables (for example, a remembered value) because this could cause memory leaks.
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: ShoppingListRepository
) : ViewModel() {

//    private val _shoppingListItems = MutableStateFlow<List<ShoppingListEntity>>(emptyList())
//    val shoppingListItems: StateFlow<List<ShoppingListEntity>>
//        get() = _shoppingListItems

    private val _shoppingGroupsWithLists = MutableStateFlow<List<GroupWithLists>>(emptyList())
    val shoppingGroupsWithLists: StateFlow<List<GroupWithLists>>
        get() = _shoppingGroupsWithLists

    //    private var groupId: String? by mutableStateOf(null)
    private var retrievedGroupId: Long? by mutableStateOf(null)
    var groupName by mutableStateOf("")

    //    private var itemId: String? by mutableStateOf(null)
    var itemName by mutableStateOf("")
    var itemQuantity by mutableStateOf("")
    var itemUnit by mutableStateOf("")

    private var items: MutableList<ShoppingListEntity> = mutableListOf()

    private var _selectedGroupWithList = MutableStateFlow(
        GroupWithLists(
            ShoppingGroupEntity(null, ""),
            emptyList()
        )
    )
    val selectedGroupWithList: StateFlow<GroupWithLists>
        get() = _selectedGroupWithList

    init {
        getAll()
    }

    private fun getAll() {
        viewModelScope.launch {
            repo.allGroupWithLists.collect {
                _shoppingGroupsWithLists.value = it
            }
        }
    }

    private fun deleteItemCache() {
//        itemId = null
        itemName = ""
        itemQuantity = ""
        itemUnit = ""
    }

    fun addToItemList() {
        if (itemName.isNotBlank()) {
            items.add(
                ShoppingListEntity(
                    id = null,
                    groupId = null,
                    itemName = itemName,
                    itemQuantity = itemQuantity.toFloat(),
                    itemUnit = itemUnit
                )
            )
            deleteItemCache()
        }
    }

    private fun deleteGroupCache() {
//        groupId = null
        groupName = ""
    }

    fun createGroupAndItems() {
        val group = ShoppingGroupEntity(
            id = null,
            groupName = groupName
        )

        viewModelScope.launch(Dispatchers.IO) {
            repo.createGroup(group = group)
            repo.getGroupId(groupName = groupName).collect {
                retrievedGroupId = it
                withContext(Dispatchers.IO) {
                    createItems()
                    deleteGroupCache()
                }
            }
        }
    }

    private fun createItems() {
        if (itemName.isNotBlank()) addToItemList()
        for (item in items) {
            item.groupId = retrievedGroupId
            viewModelScope.launch(Dispatchers.IO) {
                repo.createItem(item = item)
            }
        }
        items.clear()
    }

    fun getSelectedGroupWithList(groupId: String?) {
        val id = groupId?.toLong()
        viewModelScope.launch(Dispatchers.IO) {
            repo.getGroupWithList(groupId = id).collect {
                _selectedGroupWithList.value = it
            }
        }
    }

    fun deleteItem(itemId: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteItem(itemId = itemId)
        }
    }

    fun deleteGroup(groupId: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteGroup(groupId = groupId)
        }
    }

    fun deleteItems(shoppingList: List<ShoppingListEntity>) {
        for (item in shoppingList) {
            viewModelScope.launch(Dispatchers.IO) {
                repo.deleteItem(itemId = item.id)
            }
        }
    }
}