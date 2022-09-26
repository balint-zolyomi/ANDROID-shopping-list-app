package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R

@Composable
fun AddItemButton(onAddItemButtonClicked: () -> Unit) {
    Button(onClick = onAddItemButtonClicked) {
        Text(text = stringResource(R.string.add_item_button_text))
    }
}