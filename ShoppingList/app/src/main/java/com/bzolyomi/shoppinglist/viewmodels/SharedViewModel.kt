package com.bzolyomi.shoppinglist.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzolyomi.shoppinglist.data.GroupWithList
import com.bzolyomi.shoppinglist.data.Repository
import com.bzolyomi.shoppinglist.data.ShoppingGroupEntity
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// Warning: ViewModels are not part of the Composition. Therefore, you should not hold state
// created in composables (for example, a remembered value) because this could cause memory leaks.
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    var groupName by mutableStateOf("")
    var itemName by mutableStateOf("")
    var itemQuantity by mutableStateOf("")
    var itemUnit by mutableStateOf("")

    private val _shoppingGroupsWithLists = MutableStateFlow<List<GroupWithList>>(emptyList())
    val shoppingGroupsWithLists: StateFlow<List<GroupWithList>>
        get() = _shoppingGroupsWithLists

    private var _selectedGroupWithList = MutableStateFlow(
        GroupWithList(
            ShoppingGroupEntity(null, ""),
            emptyList()
        )
    )
    val selectedGroupWithList: StateFlow<GroupWithList>
        get() = _selectedGroupWithList

    private var _groupId: Long? = null

    private var items: MutableList<ShoppingItemEntity> = mutableListOf()

    val yPositionOfItems: StateFlow<MutableMap<Int, Float>> = MutableStateFlow(mutableMapOf())
    var isRearranging by mutableStateOf(false)

    init {
        getAll()
    }

    private fun getAll() {
        viewModelScope.launch {
            repo.allGroupsWithLists.collect {
                _shoppingGroupsWithLists.value = it
            }
        }
    }

    fun getSelectedGroupWithList(groupId: String?) {
        val id = groupId?.toLong()
        viewModelScope.launch(Dispatchers.IO) {
            repo.getGroupWithList(groupId = id).collect {
                _selectedGroupWithList.value = it
            }
        }
    }

//    fun resetItemGUIInput() {
//        itemName = ""
//        itemQuantity = ""
//        itemUnit = ""
//        isItemChecked = false
//    }
//
//    fun addFromGUIToItemList() {
//        items.add(
//            ShoppingItemEntity(
//                itemId = null,
//                itemParentId = null,
//                itemName = itemName,
//                itemQuantity = if (itemQuantity == "") 0f else itemQuantity.toFloat(),
//                itemUnit = itemUnit,
//                isItemChecked = false
//            )
//        )
//    }

    fun flushItemGUI() {
        itemName = ""
        itemQuantity = ""
        itemUnit = ""
    }

    // COROUTINES and their functions
    fun createWithCoroutines() = runBlocking {
        var groupId = getGroupIdCoroutine()

        if (groupId == null) {
            createGroupCoroutine()
            groupId = getGroupIdCoroutine()
            createItems(groupId = groupId)
        } else {
            createItems(groupId = groupId)
        }

        groupName = ""
    }

    suspend fun createItems(groupId: Long?) = coroutineScope {
        addItemFromGUIToItemList()
        for (item in items) {
            item.itemParentId = groupId
            repo.createItem(item = item)
        }
        flushItemGUI()
        items.clear()
    }

    private suspend fun createGroupCoroutine() = coroutineScope {
        val createGroup = async { createGroup() }
        createGroup.await()
    }

    private suspend fun createGroup() {
        repo.createGroup(ShoppingGroupEntity(groupId = null, groupName = groupName.trim()))
    }

    private suspend fun getGroupIdCoroutine(): Long? = coroutineScope {
        val groupId = async { getGroupId() }
        groupId.await()
    }

    private suspend fun getGroupId(): Long? {
        return repo.getGroupId(groupName = groupName.trim())
    }

    fun addItemFromGUIToItemList() {
        if (itemName.isNotBlank()) {
            items.add(
                ShoppingItemEntity(
                    itemId = null,
                    itemParentId = null,
                    itemName = itemName.trim(),
                    itemQuantity = if (itemQuantity.isBlank()) {
                        null
                    } else {
                        itemQuantity = itemQuantity.replace(",", ".").trim()
                        itemQuantity.toFloat()
                    },
                    itemUnit = itemUnit.trim(),
                    isItemChecked = false
                )
            )
        }
        flushItemGUI()
    }

    // OTHER
    fun setCurrentGroupID(groupId: Long?) {
        _groupId = groupId
    }

    // DELETE
    fun deleteGroup(groupId: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteGroup(groupId = groupId)
        }
    }

    fun deleteItem(itemId: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteItem(itemId = itemId)
        }
    }

    fun deleteItems(shoppingList: List<ShoppingItemEntity>) {
        for (item in shoppingList) {
            viewModelScope.launch(Dispatchers.IO) {
                repo.deleteItem(itemId = item.itemId)
            }
        }
    }

    // UPDATE
    fun updateItemChecked(shoppingListItem: ShoppingItemEntity) {
        shoppingListItem.isItemChecked = !shoppingListItem.isItemChecked

        viewModelScope.launch(Dispatchers.IO) {
            repo.updateItem(item = shoppingListItem)
        }
    }

//    fun updateItem(shoppingListItem: ShoppingItemEntity) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repo.updateItem(item = shoppingListItem)
//        }
//    }
}
/* --- TODO: REARRANGE
    fun rearrangeItems(
        shoppingListItems: List<ShoppingItemEntity>,
        yPositionOfItems: MutableMap<Int, Float>
    ) {
        var rearrangedMutableList: MutableList<ShoppingItemEntity> = mutableListOf()
        for (item in shoppingListItems) {
            rearrangedMutableList.add(item)
        }

        for (itemIndex in 0..shoppingListItems.size - 2) {
            if (yPositionOfItems[itemIndex]!! > yPositionOfItems[itemIndex + 1]!!) {
                isRearranging = true
                *//*
                // Create new, rearranged list (switch items)
                rearrangedMutableList[itemIndex + 1] = shoppingListItems[itemIndex]
                rearrangedMutableList[itemIndex] = shoppingListItems[itemIndex + 1]

                // Convert back to list
                val rearrangedList: List<ShoppingListEntity> = rearrangedMutableList

                // Don't forget groupId
                retrievedGroupId = rearrangedList[0].groupId

                // Delete all previous items of list in database
                deleteItems(shoppingListItems)

                // Create all items of new list
                items.clear()
                items = rearrangedMutableList
                createItems()
                *//*

                // Save item
                val savedItem: ShoppingItemEntity = shoppingListItems[itemIndex]
                currentGroupId = savedItem.itemParentId

                // Delete item
                deleteItem(shoppingListItems[itemIndex].itemId)

                // Add item
                items.clear()
                itemName = savedItem.itemName
                itemQuantity = savedItem.itemQuantity.toString()
                itemUnit = savedItem.itemUnit
                createItemsAndClearCaches()
            }
        }

    }*/


