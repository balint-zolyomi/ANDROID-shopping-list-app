package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.bzolyomi.shoppinglist.R

@Composable
fun ItemInput(
    itemName: String,
    itemQuantity: String,
    itemUnit: String,
    onItemNameChange: (String) -> Unit,
    onItemQuantityChange: (String) -> Unit,
    onItemUnitChange: (String) -> Unit,
    onEraseItemNameInputButtonClicked: () -> Unit,
    onEraseItemQuantityInputButtonClicked: () -> Unit,
    onEraseItemUnitInputButtonClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ItemNameInput(
            itemName = itemName,
            onItemNameChange = { onItemNameChange(it) },
            onEraseItemNameInputButtonClicked = onEraseItemNameInputButtonClicked
        )
        ItemQuantityInput(
            itemQuantity = itemQuantity,
            onItemQuantityChange = { onItemQuantityChange(it) },
            onEraseItemQuantityInputButtonClicked = onEraseItemQuantityInputButtonClicked
        )
        ItemUnitInput(
            itemUnit = itemUnit,
            onItemUnitChange = { onItemUnitChange(it) },
            onEraseItemUnitInputButtonClicked = onEraseItemUnitInputButtonClicked
        )
    }
}

@Composable
fun ItemNameInput(
    itemName: String,
    onItemNameChange: (String) -> Unit,
    onEraseItemNameInputButtonClicked: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemName,
        onValueChange = { onItemNameChange(it) },
        label = { Text(text = stringResource(R.string.input_label_item_name)) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        trailingIcon = { TrailingIconForErase(onEraseItemNameInputButtonClicked) },
//        {
//            IconButton(onClick = onEraseNameInputButtonClicked) {
//                Icon(
//                    imageVector = Icons.Filled.Close,
//                    contentDescription = "",
//                    tint = MaterialTheme.colors.primary
//                )
//            }
//        },
        singleLine = true
    )
}

@Composable
fun ItemQuantityInput(
    itemQuantity: String,
    onItemQuantityChange: (String) -> Unit,
    onEraseItemQuantityInputButtonClicked: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemQuantity,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
        onValueChange = { onItemQuantityChange(it) },
        label = { Text(text = stringResource(R.string.input_label_item_quantity)) },
        trailingIcon = { TrailingIconForErase(onEraseItemQuantityInputButtonClicked) },
        singleLine = true
    )
}

@Composable
fun ItemUnitInput(
    itemUnit: String,
    onItemUnitChange: (String) -> Unit,
    onEraseItemUnitInputButtonClicked: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemUnit,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        onValueChange = { onItemUnitChange(it) },
        label = { Text(text = stringResource(R.string.input_label_item_unit)) },
        trailingIcon = { TrailingIconForErase(onEraseItemUnitInputButtonClicked)},
        singleLine = true
    )
}