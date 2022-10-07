package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import java.time.format.TextStyle

@Composable
fun ItemInput(
    itemName: String,
    itemQuantity: String,
    itemUnit: String,
    isItemNameError: Boolean,
    isItemQuantityError: Boolean,
    onItemNameChange: (String) -> Unit,
    onItemQuantityChange: (String) -> Unit,
    onItemUnitChange: (String) -> Unit,
    onEraseItemNameInputButtonClicked: () -> Unit,
    onEraseItemQuantityInputButtonClicked: () -> Unit,
    onEraseItemUnitInputButtonClicked: () -> Unit,
    onDone: () -> Unit,
    onNextInItemNameInputClicked: () -> Unit,
    onNextInItemQuantityInputClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ItemNameInput(
            itemName = itemName,
            isError = isItemNameError,
            onItemNameChange = { onItemNameChange(it) },
            onEraseItemNameInputButtonClicked = onEraseItemNameInputButtonClicked,
            onNextInItemNameInputClicked = onNextInItemNameInputClicked
        )
        ItemQuantityInput(
            itemQuantity = itemQuantity,
            isError = isItemQuantityError,
            onItemQuantityChange = { onItemQuantityChange(it) },
            onEraseItemQuantityInputButtonClicked = onEraseItemQuantityInputButtonClicked,
            onNextInItemQuantityInputClicked = onNextInItemQuantityInputClicked
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
    val inputTextStyle = if (isSystemInDarkTheme()) {
        LocalTextStyle.current.copy(color = Color.White)
    } else {
        LocalTextStyle.current
    }

    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = itemName,
            onValueChange = { onItemNameChange(it) },
            label = { Text(text = stringResource(R.string.input_label_item_name)) },
            textStyle = inputTextStyle,
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
    isError: Boolean,
    onItemQuantityChange: (String) -> Unit,
    onEraseItemQuantityInputButtonClicked: () -> Unit,
    onNextInItemQuantityInputClicked: () -> Unit
) {
    val inputTextStyle = if (isSystemInDarkTheme()) {
        LocalTextStyle.current.copy(color = Color.White)
    } else {
        LocalTextStyle.current
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemQuantity,
        onValueChange = { onItemQuantityChange(it) },
        label = { Text(text = stringResource(R.string.input_label_item_quantity)) },
        textStyle = inputTextStyle,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onNextInItemQuantityInputClicked()
                if (!isError) defaultKeyboardAction(ImeAction.Next)
            }
        ),
        trailingIcon = {
            if (itemQuantity.isNotBlank()) {
                TrailingIconForErase(onEraseItemQuantityInputButtonClicked)
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
            text = stringResource(R.string.error_message_quantity_input_field),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.error,
            modifier = Modifier.padding(start = PADDING_MEDIUM)
        )
    }
}

@Composable
fun ItemUnitInput(
    itemUnit: String,
    onItemUnitChange: (String) -> Unit,
    onEraseItemUnitInputButtonClicked: () -> Unit,
    onDone: () -> Unit
) {
    val inputTextStyle = if (isSystemInDarkTheme()) {
        LocalTextStyle.current.copy(color = Color.White)
    } else {
        LocalTextStyle.current
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = itemUnit,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        onValueChange = { onItemUnitChange(it) },
        label = { Text(text = stringResource(R.string.input_label_item_unit)) },
        textStyle = inputTextStyle,
        trailingIcon = {
            if (itemUnit.isNotBlank()) TrailingIconForErase(
                onEraseItemUnitInputButtonClicked
            )
        },
        singleLine = true
    )
}