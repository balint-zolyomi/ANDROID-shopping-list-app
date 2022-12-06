package com.bzolyomi.shoppinglist.ui.components.cards

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextDecoration
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.ui.components.CheckboxIcon
import com.bzolyomi.shoppinglist.ui.components.DeleteItemIcon
import com.bzolyomi.shoppinglist.util.Constants.ELEVATION_SMALL
import com.bzolyomi.shoppinglist.util.Constants.ITEM_CARD_ON_DELETE_FADE_OUT_DURATION
import com.bzolyomi.shoppinglist.util.Constants.PADDING_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_XX_LARGE
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_SMALL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ItemCards(
    shoppingList: List<ShoppingItemEntity>,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
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
        val shoppingListByItemPosition = shoppingList.sortedBy {
            it.itemPositionInList
        }

        items(shoppingListByItemPosition) { item ->

            var isItemChecked by mutableStateOf(item.isItemChecked)

            var startFadeOutAnimation by remember { mutableStateOf(false) }
            val cardAlpha by animateFloatAsState(
                targetValue = if (startFadeOutAnimation)
                    0.0F else 1F,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessHigh
                )
            )
            val scope = rememberCoroutineScope()

            Card(
                elevation = ELEVATION_SMALL,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .padding(PADDING_SMALL)
                    .alpha(cardAlpha)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CheckboxIcon(
                        isItemChecked = isItemChecked,
                        onCheckboxClicked = {
                            onCheckboxClicked(item)
                            isItemChecked = !isItemChecked
                        },
                        itemName = item.itemName,
                        modifier = Modifier.padding(start = PADDING_X_SMALL)
                    )
                    Item(
                        item = item,
                        textDecoration = TextDecoration.None,
                        modifier = Modifier
                    )
                    DeleteItemIcon(
                        onDeleteItemClicked = {
                            startFadeOutAnimation = true
                            scope.launch(Dispatchers.IO) {
                                delay(ITEM_CARD_ON_DELETE_FADE_OUT_DURATION)
                                startFadeOutAnimation = false
                                onDeleteItemClicked(item.itemId)
                            }
                        },
                        itemName = item.itemName,
                        modifier = Modifier.padding(end = PADDING_X_SMALL)
                    )
                }
            }
        }
    }
}
