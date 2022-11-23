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
        val group1 = ShoppingGroupEntity(
            groupId = 1,
            groupName = "Spar"
        )
        val group2 = ShoppingGroupEntity(
            groupId = 2,
            groupName = "Lidl"
        )
        val item1 =
            ShoppingItemEntity(
                1,
                1,
                "cheese",
                2F,
                "kg",
                false,
                1
            )
        val item2 =
            ShoppingItemEntity(
                2,
                1,
                "beer",
                5F,
                "l",
                false,
                2
            )
        val item3 =
            ShoppingItemEntity(
                3,
                2,
                "pasta",
                2F,
                "kg",
                false,
                1
            )

        createItemCoroutine(item1)
        createItemCoroutine(item2)
        createItemCoroutine(item3)
        createGroupCoroutine(group1)
        createGroupCoroutine(group2)
    }
}