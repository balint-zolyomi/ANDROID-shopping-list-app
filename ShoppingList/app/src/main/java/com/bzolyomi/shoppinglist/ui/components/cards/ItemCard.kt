package com.bzolyomi.shoppinglist.ui.components.cards

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import com.bzolyomi.shoppinglist.data.ListOrderEntity
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.ui.components.CheckboxIcon
import com.bzolyomi.shoppinglist.ui.components.DeleteItemIcon
import com.bzolyomi.shoppinglist.util.Constants.ELEVATION_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_XX_LARGE
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_SMALL

@Composable
fun ItemCards(
    shoppingList: List<ShoppingItemEntity>,
    listOrderById: List<ListOrderEntity>,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    onDeleteItemClicked: (itemId: Long?, groupId: Long?) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier,
        contentPadding = PaddingValues(
            start = PADDING_SMALL,
            end = PADDING_SMALL,
            top = PADDING_SMALL,
            bottom = PADDING_XX_LARGE
        )
    ) {
        val order = mutableStateOf(listOrderById.sortedBy {
            it.itemPositionInList
        })

        items(order.value) { listOrder ->

            val shoppingListItem = shoppingList.find { shoppingItem ->
                shoppingItem.itemId == listOrder.itemId
            }

            if (shoppingListItem != null) {
                var isItemChecked by mutableStateOf(shoppingListItem.isItemChecked)

                Card(
                    elevation = ELEVATION_SMALL,
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.padding(PADDING_SMALL)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CheckboxIcon(
                            isItemChecked = isItemChecked,
                            onCheckboxClicked = {
                                onCheckboxClicked(shoppingListItem)
                                isItemChecked = !isItemChecked
                            },
                            modifier = Modifier.padding(start = PADDING_X_SMALL)
                        )
                        Item(
                            item = shoppingListItem,
                            textDecoration = TextDecoration.None,
                            modifier = Modifier
                        )
                        DeleteItemIcon(
                            onDeleteItemClicked = {
                                onDeleteItemClicked(
                                    shoppingListItem.itemId,
                                    shoppingListItem.itemParentId
                                )
                            },
                            modifier = Modifier.padding(end = PADDING_X_SMALL)
                        )
                    }
                }
            }
        }
    }
}
