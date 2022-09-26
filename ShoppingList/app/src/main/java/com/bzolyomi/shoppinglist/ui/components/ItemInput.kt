package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R

@Composable
fun ItemInput(
    itemName: String,
    itemQuantity: String,
    itemUnit: String,
    onItemNameChange: (String) -> Unit,
    onItemQuantityChange: (String) -> Unit,
    onItemUnitChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ItemNameInput(
            itemName = itemName,
            onItemNameChange = { onItemNameChange(it) }
        )
        ItemQuantityInput(
            itemQuantity = itemQuantity,
            onItemQuantityChange = { onItemQuantityChange(it) }
        )
        ItemUnitInput(
            itemUnit = itemUnit,
            onItemUnitChange = { onItemUnitChange(it) }
        )
    }
}

@Composable
fun ItemNameInput(itemName: String, onItemNameChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemName,
        onValueChange = { onItemNameChange(it) },
        label = { Text(text = stringResource(R.string.input_label_item_name)) },
        singleLine = true
    )
}

@Composable
fun ItemQuantityInput(itemQuantity: String, onItemQuantityChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemQuantity,
        onValueChange = { onItemQuantityChange(it) },
        label = { Text(text = stringResource(R.string.input_label_item_quantity)) },
        singleLine = true
    )
}

@Composable
fun ItemUnitInput(itemUnit: String, onItemUnitChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemUnit,
        onValueChange = { onItemUnitChange(it) },
        label = { Text(text = stringResource(R.string.input_label_item_unit)) },
        singleLine = true
    )
}