package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.ui.components.GroupInput
import com.bzolyomi.shoppinglist.ui.components.ItemInput
import com.bzolyomi.shoppinglist.ui.components.SubmitAddAllButton
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM

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
    onEraseItemQuantityInputButtonClicked: () -> Unit,
    onEraseItemUnitInputButtonClicked: () -> Unit,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(PADDING_MEDIUM)
    ) {
        GroupInput(
            groupName = groupName,
            onGroupNameChange = { onGroupNameChange(it) },
            onEraseGroupNameInputButtonClicked = onEraseGroupNameInputButtonClicked
        )
        ItemInput(
            itemName = itemName,
            itemQuantity = itemQuantity,
            itemUnit = itemUnit,
            onItemNameChange = { onItemNameChange(it) },
            onItemQuantityChange = { onItemQuantityChange(it) },
            onItemUnitChange = { onItemUnitChange(it) },
            onEraseItemNameInputButtonClicked = onEraseItemNameInputButtonClicked,
            onEraseItemQuantityInputButtonClicked = onEraseItemQuantityInputButtonClicked,
            onEraseItemUnitInputButtonClicked = onEraseItemUnitInputButtonClicked
        )
        Row(modifier = Modifier.padding(PADDING_MEDIUM)) {
            AddItemButton(onAddItemButtonClicked = onAddItemButtonClicked)
            Spacer(Modifier.weight(1f))
            SubmitAddAllButton(onSubmitAddAllButtonClicked = onSubmitAddAllButtonClicked)
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
