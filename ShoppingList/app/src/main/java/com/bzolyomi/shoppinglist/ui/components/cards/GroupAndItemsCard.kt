package com.bzolyomi.shoppinglist.ui.components.cards

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.bzolyomi.shoppinglist.data.ListOrderEntity
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.ui.components.ExpandIcon
import com.bzolyomi.shoppinglist.ui.components.OpenInNewIcon
import com.bzolyomi.shoppinglist.util.Constants.ELEVATION_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.ELEVATION_SMALL
import com.bzolyomi.shoppinglist.util.Constants.GROUP_CARD_FADE_IN_DURATION
import com.bzolyomi.shoppinglist.util.Constants.GROUP_CARD_FADE_IN_INITIAL_ALPHA
import com.bzolyomi.shoppinglist.util.Constants.GROUP_CARD_FADE_OUT_DURATION
import com.bzolyomi.shoppinglist.util.Constants.PADDING_LARGE
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.PADDING_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_LARGE
import com.bzolyomi.shoppinglist.util.Constants.PADDING_ZERO

@Composable
private fun CardTitle(
    titleGroupName: String, modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            // However sometimes you need to deviate slightly from the selection of
            // colors and font styles. In those situations it's better to base your
            // color or style on an existing one.
            // For this, you can modify a predefined style by using the copy function.
            text = titleGroupName, style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun DoneRatio(
    shoppingList: List<ShoppingItemEntity>, modifier: Modifier
) {
    var itemsTotal = 0
    var itemsDone = 0
    for (item in shoppingList) {
        itemsTotal++
        if (item.isItemChecked) itemsDone++
    }
    Text(
        text = "$itemsDone/$itemsTotal", modifier = modifier, style = MaterialTheme.typography.body1
    )
}

@Composable
fun GroupAndItemsCard(
    titleGroupName: String,
    shoppingList: List<ShoppingItemEntity>,
    listOrder: List<ListOrderEntity>,
    onOpenGroupIconClicked: () -> Unit,
    modifier: Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val titleBottomPaddingDp by animateDpAsState(if (isExpanded) PADDING_ZERO else PADDING_MEDIUM)
    val cardElevation = if (isExpanded) ELEVATION_MEDIUM else ELEVATION_SMALL

    Card(
        elevation = cardElevation,
        modifier = modifier.padding(PADDING_SMALL),
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            Row(
                modifier = modifier.padding(
                    start = PADDING_SMALL,
                    top = PADDING_MEDIUM,
                    end = PADDING_MEDIUM,
                    bottom = titleBottomPaddingDp
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                ExpandIcon(
                    isExpanded = isExpanded,
                    onExpandIconClicked = { isExpanded = !isExpanded },
                    modifier = modifier.weight(1f)
                )
                CardTitle(
                    titleGroupName = titleGroupName, modifier = modifier.weight(1f)
                )
                DoneRatio(
                    shoppingList = shoppingList, modifier = modifier.padding(end = PADDING_SMALL)
                )
            }
            CardContent(
                isExpanded = isExpanded,
                shoppingList = shoppingList,
                listOrder = listOrder,
                onOpenGroupIconClicked = onOpenGroupIconClicked,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ColumnScope.CardContent(
    isExpanded: Boolean,
    shoppingList: List<ShoppingItemEntity>,
    listOrder: List<ListOrderEntity>,
    onOpenGroupIconClicked: () -> Unit,
    modifier: Modifier
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(
            initialAlpha = GROUP_CARD_FADE_IN_INITIAL_ALPHA,
            animationSpec = tween(durationMillis = GROUP_CARD_FADE_IN_DURATION)
        ), exit = shrinkVertically() + fadeOut(
            animationSpec = tween(durationMillis = GROUP_CARD_FADE_OUT_DURATION)
        )
    ) {
        Row(
            modifier = modifier.padding(
                start = PADDING_X_LARGE,
                top = PADDING_ZERO,
                end = PADDING_LARGE,
                bottom = PADDING_LARGE
            ), verticalAlignment = Alignment.CenterVertically
        ) {
            OpenInNewIcon(onOpenGroupIconClicked = onOpenGroupIconClicked)

            Column {
                val order = listOrder.sortedBy {
                    it.itemPositionInList
                }

                for (position in order) {
                    val item = shoppingList.find { shoppingItem ->
                        shoppingItem.itemId == position.itemId
                    }

                    if (item != null) {
                        val textDecoration = if (item.isItemChecked) TextDecoration.LineThrough else
                            TextDecoration.None

                        Item(
                            item = item,
                            textDecoration = textDecoration,
                            modifier = modifier
                        )
                    }
                }
            }
            Spacer(modifier = modifier.weight(1f))
        }
    }
}
