package com.bzolyomi.shoppinglist.ui.components.cards

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_SMALL

@Composable
fun Item(
    item: ShoppingItemEntity,
    textDecoration: TextDecoration,
    modifier: Modifier
) {
    val itemQuantityToDisplay = if (item.itemQuantity == null) {
        if (item.itemUnit.isEmpty()) "" else " "
    } else {
        " -- " + item.itemQuantity.toString().dropLastWhile { it == '0' }
            .dropLastWhile { it == '.' } + " "
    }
    Text(
        text = item.itemName + itemQuantityToDisplay + item.itemUnit,
        textDecoration = textDecoration,
        modifier = modifier.padding(PADDING_X_SMALL)
    )
}