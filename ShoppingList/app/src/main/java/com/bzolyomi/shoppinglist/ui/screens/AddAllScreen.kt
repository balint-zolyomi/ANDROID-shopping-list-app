package com.bzolyomi.shoppinglist.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bzolyomi.shoppinglist.ui.components.*
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
    onSubmitAddAllButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PADDING_MEDIUM)
    ) {
        GroupInput(
            groupName = groupName,
            onGroupNameChange = { onGroupNameChange(it) }
        )
        ItemInput(
            itemName = itemName,
            itemQuantity = itemQuantity,
            itemUnit = itemUnit,
            onItemNameChange = { onItemNameChange(it) },
            onItemQuantityChange = { onItemQuantityChange(it) },
            onItemUnitChange = { onItemUnitChange(it) }
        )
        Row(modifier = Modifier.padding(PADDING_MEDIUM)) {
            AddItemButton(onAddItemButtonClicked = onAddItemButtonClicked)
            Spacer(Modifier.weight(1f))
            SubmitAddAllButton(onSubmitAddAllButtonClicked = onSubmitAddAllButtonClicked)
        }
    }
}
