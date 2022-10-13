package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.bzolyomi.shoppinglist.R

@Composable
fun ItemNameInput(
    itemName: String,
    isError: Boolean,
    inputTextStyle: TextStyle,
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
                        contentDescription = stringResource(
                            id = R.string.content_description_error_icon_input_field
                        )
                    )
                }
            },
            singleLine = true,
            isError = isError
        )
        if (isError) {
            ErrorText(text = stringResource(R.string.error_message_input_field))
        }
    }
}

@Composable
fun ItemQuantityInput(
    itemQuantity: String,
    isError: Boolean,
    inputTextStyle: TextStyle,
    onItemQuantityChange: (String) -> Unit,
    onEraseItemQuantityInputButtonClicked: () -> Unit,
    onNextInItemQuantityInputClicked: () -> Unit
) {
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
                    contentDescription = stringResource(
                        id = R.string.content_description_error_icon_input_field
                    )
                )
            }
        },
        singleLine = true,
        isError = isError
    )
    if (isError) {
        ErrorText(
            text = stringResource(R.string.error_message_quantity_input_field)
        )
    }
}

@Composable
fun ItemUnitInput(
    itemUnit: String,
    inputTextStyle: TextStyle,
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
        textStyle = inputTextStyle,
        trailingIcon = {
            if (itemUnit.isNotBlank()) TrailingIconForErase(
                onEraseItemUnitInputButtonClicked
            )
        },
        singleLine = true
    )
}