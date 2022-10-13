package com.bzolyomi.shoppinglist.ui.components.cards

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.data.ListOrderEntity
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.ui.components.DragIcon
import com.bzolyomi.shoppinglist.util.Constants
import com.bzolyomi.shoppinglist.util.Constants.ELEVATION_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.PADDING_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_SMALL
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun ItemCardsReorder(
    shoppingList: List<ShoppingItemEntity>,
    listOrderById: List<ListOrderEntity>,
    onItemsOrderChange: (List<ListOrderEntity>) -> Unit,
    modifier: Modifier
) {
    var listOrderByPosition = listOrderById.sortedBy {
        it.itemPositionInList
    }

    var itemIdsSortedByPosition by remember {
        mutableStateOf(
            List(listOrderByPosition.size) {
                listOrderByPosition[it].itemId.toString()
            }
        )
    }

    val state = rememberReorderableLazyListState(onMove = { from, to ->
        itemIdsSortedByPosition = itemIdsSortedByPosition.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }

        listOrderByPosition = listOrderByPosition.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        for ((i, item) in listOrderByPosition.withIndex()) {
            item.itemPositionInList = i
        }

        onItemsOrderChange(listOrderByPosition)
    })

    LazyColumn(
        state = state.listState,
        modifier = modifier
            .reorderable(state = state)
            .detectReorderAfterLongPress(state = state)
    ) {
        items(itemIdsSortedByPosition, { it }) { listOrderId ->

            val shoppingListItem = shoppingList.find { shoppingItem ->
                shoppingItem.itemId == listOrderId.toLong()
            }

            if (shoppingListItem != null) {
                val isItemChecked by mutableStateOf(shoppingListItem.isItemChecked)

                ReorderableItem(reorderableState = state, key = listOrderId) { isDragging ->
                    val elevation = animateDpAsState(
                        targetValue =
                        if (isDragging) 16.dp else ELEVATION_SMALL
                    )
                    val padding = animateDpAsState(
                        targetValue =
                        if (isDragging) PADDING_SMALL else 0.dp
                    )
                    val border = animateDpAsState(
                        targetValue =
                        if (isDragging) 4.dp else 0.dp
                    )
                    val borderColor = animateColorAsState(
                        targetValue =
                        if (isDragging) {
                            MaterialTheme.colors.primary
                        } else {
                            MaterialTheme.colors.surface
                        }
                    )

                    Card(
                        elevation = elevation.value,
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier.padding(PADDING_SMALL),
                        border = BorderStroke(border.value, borderColor.value)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(padding.value)
                        ) {

                            val textDecoration = if (isItemChecked) TextDecoration.LineThrough else
                                TextDecoration.None

                            DragIcon(modifier = Modifier.padding(start = PADDING_X_SMALL))
                            Item(
                                item = shoppingListItem,
                                textDecoration = textDecoration,
                                modifier = Modifier
                            )
                            DragIcon(modifier = Modifier.padding(end = PADDING_X_SMALL))
                        }
                    }
                }
            }
        }
    }
}
