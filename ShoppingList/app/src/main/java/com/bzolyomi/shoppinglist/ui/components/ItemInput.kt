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
    isError: Boolean,
    onItemNameChange: (String) -> Unit,
    onItemQuantityChange: (String) -> Unit,
    onItemUnitChange: (String) -> Unit,
    onEraseItemNameInputButtonClicked: () -> Unit,
    onEraseItemQuantityInputButtonClicked: () -> Unit,
    onEraseItemUnitInputButtonClicked: () -> Unit,
    onDone: () -> Unit,
    onNextInItemNameInputClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ItemNameInput(
            itemName = itemName,
            isError = isError,
            onItemNameChange = { onItemNameChange(it) },
            onEraseItemNameInputButtonClicked = onEraseItemNameInputButtonClicked,
            onNextInItemNameInputClicked = onNextInItemNameInputClicked
        )
        ItemQuantityInput(
            itemQuantity = itemQuantity,
            onItemQuantityChange = { onItemQuantityChange(it) },
            onEraseItemQuantityInputButtonClicked = onEraseItemQuantityInputButtonClicked
        )
        ItemUnitInput(
            itemUnit = itemUnit,
            onItemUnitChange = { onItemUnitChange(it) },
            onEraseItemUnitInputButtonClicked = onEraseItemUnitInputButtonClicked,
            onDone = onDone
        )
    }
}

@Composable
fun ItemNameInput(
    itemName: String,
    isError: Boolean,
    onItemNameChange: (String) -> Unit,
    onEraseItemNameInputButtonClicked: () -> Unit,
    onNextInItemNameInputClicked: () -> Unit
) {
    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = itemName,
            onValueChange = { onItemNameChange(it) },
            label = { Text(text = stringResource(R.string.input_label_item_name)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNextInItemNameInputClicked()
                    if (!isError) defaultKeyboardAction(ImeAction.Next)
                }
            ),
            trailingIcon = {
                if (itemName.isNotBlank()) {
                    TrailingIconForErase(onEraseItemNameInputButtonClicked)
                } else if (isError) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = ""
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
    onEraseItemUnitInputButtonClicked: () -> Unit,
    onDone: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemUnit,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
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