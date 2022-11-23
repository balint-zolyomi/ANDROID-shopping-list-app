package com.bzolyomi.shoppinglist

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

    init {
        createDummyDataIfNotExistCoroutine()
    }

    private fun createDummyDataIfNotExistCoroutine() {
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
}