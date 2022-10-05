package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM

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
    var isError by rememberSaveable { mutableStateOf(false) }

    fun validate(input: String) {
        isError = input.isBlank()
    }

    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = itemName,
            onValueChange = {
                isError = if (itemName == "" && it == " ") {
                    true
                } else {
                    onItemNameChange(it)
                    false
                }
            },
            label = { Text(text = stringResource(R.string.input_label_item_name)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                validate(itemName)
                if (!isError) defaultKeyboardAction(ImeAction.Next)
            }),
            trailingIcon = {
                if (itemName.isNotBlank()) {
                    TrailingIconForErase(onEraseItemNameInputButtonClicked)
                } else if (isError) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = "",
                        tint = MaterialTheme.colors.error
                    )
                }
            },
            singleLine = true,
            isError = isError
        )
        if (isError) {
            Text(
                text = stringResource(R.string.error_message_input_field),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(start = PADDING_MEDIUM)
            )
        }
    }
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
        trailingIcon = {
            if (itemQuantity.isNotBlank()) TrailingIconForErase(
                onEraseItemQuantityInputButtonClicked
            )
        },
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
        trailingIcon = {
            if (itemUnit.isNotBlank()) TrailingIconForErase(
                onEraseItemUnitInputButtonClicked
            )
        },
        singleLine = true
    )
}