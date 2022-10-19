package com.bzolyomi.shoppinglist.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzolyomi.shoppinglist.data.*
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
            emptyList(),
            emptyList()
        )
    )

    private val _selectedShoppingList = MutableStateFlow<List<ShoppingItemEntity>>(emptyList())
    val selectedShoppingList: StateFlow<List<ShoppingItemEntity>>
        get() = _selectedShoppingList

    private val _selectedListOrder = MutableStateFlow<List<ListOrderEntity>>(emptyList())
    val selectedListOrder: StateFlow<List<ListOrderEntity>>
        get() = _selectedListOrder

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
                    isItemChecked = false
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

    fun getSelectedListOrderCoroutine(groupId: String?) {
        val id = groupId?.toLong()
        viewModelScope.launch {
            repo.getListOrder(id).collect {
                _selectedListOrder.value = it
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
    fun createWithCoroutines() = runBlocking {
        var groupId = getGroupIdCoroutine()

        if (groupId == null) {
            createGroupCoroutine()
            groupId = getGroupIdCoroutine()
            createItems(groupId = groupId) // TODO
        } else {
            createItems(groupId = groupId) // TODO
        }

        flushGroupGUI()
    }

    private suspend fun createItems(groupId: Long?) = coroutineScope {
        addItemFromGUIToItemList()
        for (item in items) {
            item.itemParentId = groupId
            repo.createItem(item = item)
        }
        flushItemGUI()
        items.clear()

        updateListOrderCoroutine(groupId = groupId)
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

    fun deleteAllListOrders(groupId: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllListOrders(groupId = groupId)
        }
    }

    fun deleteListOrder(groupId: Long?, itemId: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteListOrder(groupId =  groupId, itemId =  itemId)
        }
    }

    private suspend fun deleteAllListOrdersAsync(groupId: Long?) {
        repo.deleteAllListOrders(groupId = groupId)
    }

//    fun deleteGroupAndItsItems() {
//        deleteItems(shoppingList = selectedGroupWithList.shoppingList)
//        deleteGroup(groupId = selectedGroupWithList.group.groupId)
//        deleteAllListOrders(groupId = selectedGroupWithList.group.groupId)
//    }

    // UPDATE
    fun updateItemChecked(shoppingListItem: ShoppingItemEntity) {
        shoppingListItem.isItemChecked = !shoppingListItem.isItemChecked

        viewModelScope.launch(Dispatchers.IO) {
            repo.updateItem(item = shoppingListItem)
        }
    }

    fun updateListOrder(orderOfItemIds: List<ListOrderEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateListOrder(
                listOrder = orderOfItemIds
            )
        }
    }

    private suspend fun updateListOrderCoroutine(groupId: Long?) = coroutineScope {
        selectedGroupWithList = getSelectedGroupWithListCoroutine(groupId = groupId.toString())
        val deleteDone = async {
            deleteAllListOrdersAsync(groupId = selectedGroupWithList.group.groupId)
        }
        deleteDone.await()
        val createListOrder = async { updateListOrder() }
        createListOrder.await()
    }

    private suspend fun updateListOrder() {
        for ((itemPosition, item) in selectedGroupWithList.shoppingList.withIndex()){
            repo.createListOrder(
                ListOrderEntity(
                    listOrderId = null,
                    groupId = selectedGroupWithList.group.groupId,
                    itemId = item.itemId,
                    itemPositionInList = itemPosition
                )
            )
        }
    }
}
