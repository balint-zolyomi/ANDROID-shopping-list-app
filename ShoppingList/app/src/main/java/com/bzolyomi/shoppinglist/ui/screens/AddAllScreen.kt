package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun AddAllScreen(
    sharedViewModel: SharedViewModel,
    onNavigateToItemGroupScreen: () -> Unit
) {
    val groupName: String = sharedViewModel.groupName
    val itemName: String = sharedViewModel.itemName
    val itemQuantity: String = sharedViewModel.itemQuantity
    val itemUnit: String = sharedViewModel.itemUnit

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        GroupInput(
            groupName = groupName,
            onGroupNameChange = { sharedViewModel.groupName = it }
        )
        ItemsInput(
            itemName = itemName,
            itemQuantity = itemQuantity,
            itemUnit = itemUnit,
            onItemNameChange = { sharedViewModel.itemName = it },
            onItemQuantityChange = { sharedViewModel.itemQuantity = it },
            onItemUnitChange = { sharedViewModel.itemUnit = it }
        )
        Row(modifier = Modifier.padding(12.dp)) {
            PlusItemButton(onPlusItemButtonClicked = {
                sharedViewModel.addToItemList()
            })
            Spacer(Modifier.weight(1f))
            SubmitButton(onSubmitButtonClicked = onNavigateToItemGroupScreen)
        }
    }
}

@Composable
fun GroupInput(
    groupName: String,
    onGroupNameChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        GroupNameInput(groupName, onGroupNameChange = onGroupNameChange)
    }
}

@Composable
fun GroupNameInput(groupName: String, onGroupNameChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = groupName,
        onValueChange = { onGroupNameChange(it) },
        label = { Text(text = "Group name") },
        singleLine = true
    )
}

@Composable
fun ItemsInput(
    itemName: String,
    itemQuantity: String,
    itemUnit: String,
    onItemNameChange: (String) -> Unit,
    onItemQuantityChange: (String) -> Unit,
    onItemUnitChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ItemNameInput(itemName, onItemNameChange = { onItemNameChange(it) })
        ItemQuantityInput(itemQuantity, onItemQuantityChange = { onItemQuantityChange(it) })
        ItemUnitInput(itemUnit, onItemUnitChange = { onItemUnitChange(it) })
    }
}

@Composable
fun ItemNameInput(itemName: String, onItemNameChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemName,
        onValueChange = { onItemNameChange(it) },
        label = { Text(text = "Item name") },
        singleLine = true
    )
}

@Composable
fun ItemQuantityInput(itemQuantity: String, onItemQuantityChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemQuantity,
        onValueChange = { onItemQuantityChange(it) },
        label = { Text(text = "Item quantity") },
        singleLine = true
    )
}

@Composable
fun ItemUnitInput(itemUnit: String, onItemUnitChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemUnit,
        onValueChange = { onItemUnitChange(it) },
        label = { Text(text = "Item Unit") },
        singleLine = true
    )
}

@Composable
fun SubmitButton(onSubmitButtonClicked: () -> Unit) {
    Button(onClick = onSubmitButtonClicked) {
        Text(text = "DONE")
    }
}

@Composable
fun PlusItemButton(onPlusItemButtonClicked: () -> Unit) {
    Button(onClick = onPlusItemButtonClicked) {
        Text(text = "+ ITEM")
    }
}
