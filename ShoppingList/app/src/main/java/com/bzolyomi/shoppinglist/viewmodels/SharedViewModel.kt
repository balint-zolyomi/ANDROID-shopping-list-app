package com.bzolyomi.shoppinglist.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzolyomi.shoppinglist.data.*
import com.bzolyomi.shoppinglist.util.Constants.SHOPPING_LIST_TABLE_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel
@Inject constructor(
    private val repo: Repository
) : ViewModel() {

    private var _groupName by mutableStateOf("")
    val groupName: State<String>
        get() = mutableStateOf(_groupName)

    private var _itemName by mutableStateOf("")
    val itemName: State<String>
        get() = mutableStateOf(_itemName)

    private var _itemQuantity by mutableStateOf("")
    val itemQuantity: State<String>
        get() = mutableStateOf(_itemQuantity)

    private var _itemUnit by mutableStateOf("")
    val itemUnit: State<String>
        get() = mutableStateOf(_itemUnit)

    private val _shoppingGroupsWithLists = MutableStateFlow<List<GroupWithList>>(emptyList())
    val shoppingGroupsWithLists: StateFlow<List<GroupWithList>>
        get() = _shoppingGroupsWithLists

    var selectedGroupWithList by mutableStateOf(
        GroupWithList(
            ShoppingGroupEntity(null, ""),
            emptyList()
        )
    )

    private val _selectedShoppingList = MutableStateFlow<List<ShoppingItemEntity>>(emptyList())
    val selectedShoppingList: StateFlow<List<ShoppingItemEntity>>
        get() = _selectedShoppingList

    private var items: MutableList<ShoppingItemEntity> = mutableListOf() // TODO

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

    // GUI
    fun flushItemGUI() {
        _itemName = ""
        _itemQuantity = ""
        _itemUnit = ""
    }

    fun flushGroupGUI() {
        setGroupName("")
    }

    fun clearItemsList() {
        items.clear()
    }

    fun setGroupName(groupName: String) {
        _groupName = groupName
    }

    fun setItemName(itemName: String) {
        _itemName = itemName
    }

    fun setItemQuantity(itemQuantity: String) {
        _itemQuantity = itemQuantity
    }

    fun setItemUnit(itemUnit: String) {
        _itemUnit = itemUnit
    }

    private fun addItemFromGUIToItemList() {
        if (_itemName.isNotBlank()) {
            items.add(
                ShoppingItemEntity(
                    itemId = null,
                    itemParentId = null,
                    itemName = _itemName.trim(),
                    itemQuantity = sanitizeItemQuantityInput(),
                    itemUnit = _itemUnit.trim(),
                    isItemChecked = false,
                    itemPositionInList = null
                )
            )
        }
        flushItemGUI()
    }

    private fun sanitizeItemQuantityInput(): Float? {
        var sanitizedInput: Float? = null
        _itemQuantity = _itemQuantity
            .replace(oldValue = ",", newValue = ".")
            .trim()
        if (_itemQuantity.isNotEmpty() && _itemQuantity != "0") {
            sanitizedInput = _itemQuantity.toFloat()
            sanitizedInput = if (sanitizedInput == 0F) null else sanitizedInput
        }
        return sanitizedInput
    }

    // COROUTINES and their functions
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

    fun getSelectedShoppingListCoroutine(groupId: String?) {
        val id = groupId?.toLong()
        viewModelScope.launch {
            repo.getShoppingList(id).collect {
                _selectedShoppingList.value = it
            }
        }
    }

    private suspend fun getGroupIdCoroutine(): Long? = coroutineScope {
        val groupId = async { getGroupId() }
        groupId.await()
    }

    private suspend fun getGroupId(): Long? {
        return repo.getGroupId(groupName = _groupName.trim())
    }

    // CREATE
    fun createWithCoroutines() = runBlocking { // TODO
        var groupId = getGroupIdCoroutine()

        if (groupId == null) {
            createGroupCoroutine()
            groupId = getGroupIdCoroutine()
            createItems(groupId = groupId, startingPosition = SHOPPING_LIST_TABLE_EMPTY)
        } else {
            val groupWithList = getSelectedGroupWithListCoroutine(groupId = groupId.toString())
            val nextPosition = getNextStartingPosition(groupWithList = groupWithList)
            createItems(groupId = groupId, startingPosition = nextPosition)
        }

        flushGroupGUI()
    }

    private fun getNextStartingPosition(groupWithList: GroupWithList) : Int {
        var maxItemPosition = 0
        for (item in groupWithList.shoppingList) {
            if (item.itemPositionInList!! > maxItemPosition) {
                maxItemPosition = item.itemPositionInList!!
            }
        }
        return maxItemPosition
    }

    private suspend fun createItems(groupId: Long?, startingPosition: Int) = coroutineScope {
        addItemFromGUIToItemList()

        var itemPositionInList = startingPosition

        for (item in items) {
            item.itemParentId = groupId
            item.itemPositionInList = itemPositionInList
            repo.createItem(item = item)
            itemPositionInList++
        }
        flushItemGUI()
        items.clear()
    }

    private suspend fun createGroupCoroutine() = coroutineScope {
        val createGroup = async { createGroup() }
        createGroup.await()
    }

    private suspend fun createGroup() {
        repo.createGroup(
            ShoppingGroupEntity(
                groupId = null,
                groupName = _groupName.trim()
            )
        )
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

    fun deleteItem(itemId: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteItem(itemId = itemId)
        }
    }

    // UPDATE
    fun updateItemChecked(shoppingListItem: ShoppingItemEntity) {
        shoppingListItem.isItemChecked = !shoppingListItem.isItemChecked

        viewModelScope.launch(Dispatchers.IO) {
            repo.updateItem(item = shoppingListItem)
        }
    }

    fun updateShoppingListOrder(shoppingList: List<ShoppingItemEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateShoppingListOrder(
                shoppingList = shoppingList
            )
        }
    }
}
