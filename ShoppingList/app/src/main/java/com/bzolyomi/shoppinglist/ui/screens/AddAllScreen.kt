package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.ui.components.AddItemButton
import com.bzolyomi.shoppinglist.ui.components.GroupInput
import com.bzolyomi.shoppinglist.ui.components.ItemInput
import com.bzolyomi.shoppinglist.ui.components.SubmitButton
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
    onSubmitButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PADDING_MEDIUM)
    ) {
        GroupInput(
            groupName = groupName,
            onGroupNameChange = onGroupNameChange
        )
        ItemInput(
            itemName = itemName,
            itemQuantity = itemQuantity,
            itemUnit = itemUnit,
            onItemNameChange = onItemNameChange,
            onItemQuantityChange = onItemQuantityChange,
            onItemUnitChange = onItemUnitChange
        )
        Row(modifier = Modifier.padding(PADDING_MEDIUM)) {
            AddItemButton(onAddItemButtonClicked = onAddItemButtonClicked)
            Spacer(Modifier.weight(1f))
            SubmitButton(onSubmitButtonClicked = onSubmitButtonClicked)
        }
    }
}
