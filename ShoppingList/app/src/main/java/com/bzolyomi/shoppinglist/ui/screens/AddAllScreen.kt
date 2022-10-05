package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.ui.components.GroupInput
import com.bzolyomi.shoppinglist.ui.components.ItemInput
import com.bzolyomi.shoppinglist.ui.components.SubmitAddAllButton
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun AddAllScreen(
    groupName: String,
    itemName: String,
    itemQuantity: String,
    itemUnit: String,
    onGroupNameChange: (String) -> Unit,
    onItemNameChange: (String) -> Unit,
    onItemQuantityChange: (String) -> Unit,
    onItemUnitChange: (String) -> Unit,
    onAddItemButtonClicked: () -> Unit,
    onSubmitAddAllButtonClicked: () -> Unit,
    onEraseGroupNameInputButtonClicked: () -> Unit,
    onEraseItemNameInputButtonClicked: () -> Unit,
//    onEraseItemQuantityInputButtonClicked: () -> Unit,
    onEraseItemUnitInputButtonClicked: () -> Unit,
    sharedViewModel: SharedViewModel,
    modifier: Modifier
) {
    var isGroupNameError by rememberSaveable { mutableStateOf(false) }
    var isItemNameError by rememberSaveable { mutableStateOf(false) }
    var isItemQuantityError by rememberSaveable { mutableStateOf(false) }

    fun validateGroupNameInput(groupNameInput: String) {
        isGroupNameError = groupNameInput.isBlank()
    }

    fun validateItemNameInput(itemNameInput: String) {
        isItemNameError = itemNameInput.isBlank()
    }

    fun validateItemQuantityInput(itemQuantityInput: String) {
        isItemQuantityError = try {
            val tempQuantity = itemQuantityInput.replace(",",".")
            tempQuantity.toFloat()
            false
        } catch (e: Exception) {
            itemQuantityInput != ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(PADDING_MEDIUM)
    ) {
        GroupInput(
            groupName = groupName,
            isError = isGroupNameError,
            onGroupNameChange = {
                validateGroupNameInput(it)
                if (!isGroupNameError) onGroupNameChange(it)
            },
            onEraseGroupNameInputButtonClicked = onEraseGroupNameInputButtonClicked,
            onNextInGroupNameInputClicked = { validateGroupNameInput(groupName) }
        )
        ItemInput(
            itemName = itemName,
            itemQuantity = itemQuantity,
            itemUnit = itemUnit,
            isItemNameError = isItemNameError,
            isItemQuantityError = isItemQuantityError,
            onItemNameChange = {
                validateItemNameInput(it)
                if (!isItemNameError) onItemNameChange(it)
            },
            onItemQuantityChange = {
                onItemQuantityChange(it)
            },
            onItemUnitChange = { onItemUnitChange(it) },
            onEraseItemNameInputButtonClicked = onEraseItemNameInputButtonClicked,
            onEraseItemQuantityInputButtonClicked = {
                sharedViewModel.itemQuantity = ""
                validateItemQuantityInput(sharedViewModel.itemQuantity)
            },
            onEraseItemUnitInputButtonClicked = onEraseItemUnitInputButtonClicked,
            onDone = {
                validateGroupNameInput(groupName)
                validateItemNameInput(itemName)
                validateItemQuantityInput(itemQuantity)
                if (!isItemNameError && !isGroupNameError && !isItemQuantityError) {
                    onSubmitAddAllButtonClicked()
                }
            },
            onNextInItemNameInputClicked = { validateItemNameInput(itemName) },
            onNextInItemQuantityInputClicked = { validateItemQuantityInput(itemQuantity) }
        )
        Row(modifier = Modifier.padding(PADDING_MEDIUM)) {
            AddItemButton(onAddItemButtonClicked = {
                validateGroupNameInput(groupName)
                validateItemNameInput(itemName)
                validateItemQuantityInput(itemQuantity)
                if (!isItemNameError && !isGroupNameError && !isItemQuantityError) {
                    onAddItemButtonClicked()
                }
            })
            Spacer(Modifier.weight(1f))
            SubmitAddAllButton(onSubmitAddAllButtonClicked = {
                validateGroupNameInput(groupName)
                validateItemNameInput(itemName)
                validateItemQuantityInput(itemQuantity)
                if (!isItemNameError && !isGroupNameError && !isItemQuantityError) {
                    onSubmitAddAllButtonClicked()
                }
            })
        }
    }
}

@Composable
fun AddItemButton(onAddItemButtonClicked: () -> Unit) {
    Button(
        onClick = onAddItemButtonClicked,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
    ) {
        Text(text = stringResource(R.string.add_item_button_text))
    }
}
