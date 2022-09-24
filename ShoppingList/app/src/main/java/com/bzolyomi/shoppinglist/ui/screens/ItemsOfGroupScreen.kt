package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.data.GroupWithLists
import com.bzolyomi.shoppinglist.data.ShoppingListEntity
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun ItemsOfGroupScreen(
    selectedGroupWithList: GroupWithLists,
    onDeleteItemClicked: (itemId: Long?) -> Unit
) {
//    val shoppingGroupsWithLists by sharedViewModel.shoppingGroupsWithLists.collectAsState()

    Column {
        Text(
            text = selectedGroupWithList.group.groupName.uppercase(),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(12.dp)
        )
        AllItems(
            shoppingListItems = selectedGroupWithList.shoppingList,
            onDeleteItemClicked = onDeleteItemClicked
        )
    }
}

@Composable
fun AllItems(
    shoppingListItems: List<ShoppingListEntity>,
    onDeleteItemClicked: (itemId: Long?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // The composable function rememberLazyListState creates an initial state for the list
        // using rememberSaveable. When the Activity is recreated, the scroll state is
        // maintained without you having to code anything.
        LazyColumn(state = LazyListState(), modifier = Modifier.padding(12.dp)) {
            items(items = shoppingListItems) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = false, onCheckedChange = { /*TODO*/ })
                    Text(
                        text = it.itemName,
//                      However sometimes you need to deviate slightly from the selection of colors
//                      and font styles. In those situations it's better to base your color or style
//                      on an existing one.
//                      For this, you can modify a predefined style by using the copy function.
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    IconButton(onClick = { onDeleteItemClicked(it.id) }) {
                        Icon(Icons.Filled.Close, contentDescription = "Delete item")
                    }
                }
            }
        }
    }
}