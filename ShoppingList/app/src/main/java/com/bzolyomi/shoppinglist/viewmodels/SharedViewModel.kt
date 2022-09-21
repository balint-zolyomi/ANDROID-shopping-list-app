package com.bzolyomi.shoppinglist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzolyomi.shoppinglist.data.ShoppingListDao
import com.bzolyomi.shoppinglist.data.ShoppingListEntity
import com.bzolyomi.shoppinglist.data.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: ShoppingListRepository
) : ViewModel() {

    private val _shoppingList = MutableStateFlow<List<ShoppingListEntity>>(emptyList())
    val shoppingList: StateFlow<List<ShoppingListEntity>> = _shoppingList

    init {
        getAll()
    }

    private fun getAll() {
        viewModelScope.launch {
            repo.allShoppingLists.collect {
                _shoppingList.value = it
            }
        }
    }
}