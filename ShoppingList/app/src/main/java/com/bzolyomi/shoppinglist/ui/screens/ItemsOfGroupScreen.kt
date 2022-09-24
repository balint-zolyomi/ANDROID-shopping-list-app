package com.bzolyomi.shoppinglist.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.data.GroupWithLists
import com.bzolyomi.shoppinglist.data.ShoppingListEntity

@Composable
fun ItemsOfGroupScreen(
    selectedGroupWithList: GroupWithLists?,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    onDeleteGroupClicked: (groupId: Long?, shoppingList: List<ShoppingListEntity>) -> Unit,
    itemName: String,
    itemQuantity: String,
    itemUnit: String,
    onItemNameChange: (String) -> Unit,
    onItemQuantityChange: (String) -> Unit,
    onItemUnitChange: (String) -> Unit,
    onSubmitButtonClicked: (Long?) -> Unit
) {
    if (selectedGroupWithList != null) {
        var addItem by remember { mutableStateOf(false) }

        Column {
            ContentWithoutInput(
                selectedGroupWithList,
                onDeleteGroupClicked,
                onDeleteItemClicked
            )
            if (addItem) {
                Column(modifier = Modifier.padding(12.dp)){
                    ItemNameInput(
                        itemName = itemName,
                        onItemNameChange = { onItemNameChange(it) })
                    ItemQuantityInput(
                        itemQuantity,
                        onItemQuantityChange = { onItemQuantityChange(it) })
                    ItemUnitInput(itemUnit, onItemUnitChange = { onItemUnitChange(it) })
                    Row (modifier = Modifier.padding(horizontal = 12.dp)) {
                        SubmitButton(onSubmitButtonClicked = {
                            onSubmitButtonClicked(
                                selectedGroupWithList.group.id
                            )
                            addItem = false
                        })
                        Spacer(modifier = Modifier.weight(1f))
                        CancelButton(onCancelButtonClicked = { addItem = false })
                    }
                }
            } else {
                Button(onClick = { addItem = true }, modifier = Modifier.padding(12.dp)) {
                    Text(text = "ADD ITEM")
                }
            }
        }
    }
}

@Composable
fun ContentWithoutInput(
    selectedGroupWithList: GroupWithLists,
    onDeleteGroupClicked: (groupId: Long?, shoppingList: List<ShoppingListEntity>) -> Unit,
    onDeleteItemClicked: (itemId: Long?) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = selectedGroupWithList.group.groupName.uppercase(),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp)
        )
        IconButton(onClick = {
            onDeleteGroupClicked(
                selectedGroupWithList.group.id,
                selectedGroupWithList.shoppingList
            )
        }) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete group")
        }
    }
    AllItems(
        shoppingListItems = selectedGroupWithList.shoppingList,
        onDeleteItemClicked = onDeleteItemClicked
    )
}

@Composable
fun CancelButton(onCancelButtonClicked: () -> Unit) {
    Button(onClick = onCancelButtonClicked) {
        Text(text = "CANCEL")
    }
}

@Composable
fun AllItems(
    shoppingListItems: List<ShoppingListEntity>,
    onDeleteItemClicked: (itemId: Long?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
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