package com.bzolyomi.shoppinglist.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzolyomi.shoppinglist.data.GroupWithList
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

    private val _shoppingGroupsWithLists = MutableStateFlow<List<GroupWithList>>(emptyList())
    val shoppingGroupsWithLists: StateFlow<List<GroupWithList>>
        get() = _shoppingGroupsWithLists

    //    private var groupId: String? by mutableStateOf(null)
    private var retrievedGroupId: Long? by mutableStateOf(null)
    var groupName by mutableStateOf("")

    //    private var itemId: String? by mutableStateOf(null)
    var itemName by mutableStateOf("")
    var itemQuantity by mutableStateOf("")
    var itemUnit by mutableStateOf("")
    var isItemChecked by mutableStateOf(false)

    private var items: MutableList<ShoppingListEntity> = mutableListOf()

    private var _selectedGroupWithList = MutableStateFlow(
        GroupWithList(
            ShoppingGroupEntity(null, ""),
            emptyList()
        )
    )
    val selectedGroupWithList: StateFlow<GroupWithList>
        get() = _selectedGroupWithList

    val yPositionOfItems: StateFlow<MutableMap<Int, Float>> = MutableStateFlow(mutableMapOf())
    var isRearranging by mutableStateOf(false)

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
                    itemUnit = itemUnit,
                    isChecked = false
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

    fun createItems(groupId: Long? = retrievedGroupId) {
        if (itemName.isNotBlank()) addToItemList()
        for (item in items) {
            item.groupId = groupId
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

    fun updateItem(shoppingListItem: ShoppingListEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateItem(shoppingListItem = shoppingListItem)
        }
    }

    fun updateItemChecked(shoppingListItem: ShoppingListEntity) {
        shoppingListItem.isChecked = !shoppingListItem.isChecked
        isItemChecked = shoppingListItem.isChecked

        viewModelScope.launch(Dispatchers.IO) {
            repo.updateItem(shoppingListItem = shoppingListItem)
        }
    }

    fun rearrangeItems(
        shoppingListItems: List<ShoppingListEntity>,
        yPositionOfItems: MutableMap<Int, Float>
    ) {
        var rearrangedMutableList: MutableList<ShoppingListEntity> = mutableListOf()
        for (item in shoppingListItems) {
            rearrangedMutableList.add(item)
        }

        for (itemIndex in 0..shoppingListItems.size - 2) {
            if (yPositionOfItems[itemIndex]!! > yPositionOfItems[itemIndex + 1]!!) {
                isRearranging = true
                /*
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
                */

                // Save item
                val savedItem: ShoppingListEntity = shoppingListItems[itemIndex]
                retrievedGroupId = savedItem.groupId

                // Delete item
                deleteItem(shoppingListItems[itemIndex].id)

                // Add item
                items.clear()
                itemName = savedItem.itemName
                itemQuantity = savedItem.itemQuantity.toString()
                itemUnit = savedItem.itemUnit
                createItems()
            }
        }

    }
}