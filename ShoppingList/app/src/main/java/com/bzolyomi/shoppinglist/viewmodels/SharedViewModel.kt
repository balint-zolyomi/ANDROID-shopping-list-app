package com.bzolyomi.shoppinglist.viewmodels

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
import javax.inject.Inject

// Warning: ViewModels are not part of the Composition. Therefore, you should not hold state
// created in composables (for example, a remembered value) because this could cause memory leaks.
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: ShoppingListRepository
) : ViewModel() {

    private val _shoppingListItems = MutableStateFlow<List<ShoppingListEntity>>(emptyList())
    val shoppingListItems: StateFlow<List<ShoppingListEntity>>
        get() = _shoppingListItems

    private val _shoppingGroupsWithLists = MutableStateFlow<List<GroupWithLists>>(emptyList())
    val shoppingGroupsWithLists: StateFlow<List<GroupWithLists>> = _shoppingGroupsWithLists

    var groupId by mutableStateOf("")
    var groupName by mutableStateOf("")

    init {
        getAllShoppingListItems()
        getAll()
    }

    private fun getAllShoppingListItems() {
        viewModelScope.launch {
            repo.allShoppingListItems.collect {
                _shoppingListItems.value = it
            }
        }
    }

    private fun getAll() {
        viewModelScope.launch {
            repo.allGroupWithLists.collect {
                _shoppingGroupsWithLists.value = it
            }
        }
    }

    fun createGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            val _groupId = groupId.toLong()
            val group = ShoppingGroupEntity(id = _groupId, groupName = groupName)
            repo.addGroup(shoppingGroupEntity = group)
        }
    }
}