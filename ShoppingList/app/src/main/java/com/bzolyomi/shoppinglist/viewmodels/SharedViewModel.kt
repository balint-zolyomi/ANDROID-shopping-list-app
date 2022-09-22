package com.bzolyomi.shoppinglist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzolyomi.shoppinglist.data.GroupWithLists
import com.bzolyomi.shoppinglist.data.ShoppingListEntity
import com.bzolyomi.shoppinglist.data.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: ShoppingListRepository
) : ViewModel() {

    private val _shoppingListItems = MutableStateFlow<List<ShoppingListEntity>>(emptyList())
    val shoppingListItems: StateFlow<List<ShoppingListEntity>> = _shoppingListItems

    private val _shoppingGroupsWithLists = MutableStateFlow<List<GroupWithLists>>(emptyList())
    val shoppingGroupsWithLists: StateFlow<List<GroupWithLists>> = _shoppingGroupsWithLists



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
}