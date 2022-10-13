package com.bzolyomi.shoppinglist.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.data.ListOrderEntity
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.util.Constants.ELEVATION_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.ELEVATION_SMALL
import com.bzolyomi.shoppinglist.util.Constants.EXPAND_ICON_ROTATION_ANIMATION_END_DEGREES
import com.bzolyomi.shoppinglist.util.Constants.EXPAND_ICON_ROTATION_ANIMATION_START_DEGREES
import com.bzolyomi.shoppinglist.util.Constants.GROUP_CARD_FADE_IN_DURATION
import com.bzolyomi.shoppinglist.util.Constants.GROUP_CARD_FADE_IN_INITIAL_ALPHA
import com.bzolyomi.shoppinglist.util.Constants.GROUP_CARD_FADE_OUT_DURATION
import com.bzolyomi.shoppinglist.util.Constants.PADDING_LARGE
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.PADDING_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_LARGE
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_ZERO
import com.bzolyomi.shoppinglist.util.Constants.SIZE_ICONS_OFFICIAL
import com.bzolyomi.shoppinglist.util.Constants.SIZE_MEDIUM
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

fun showItemAddedToast(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.toast_message_item_added),
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun ErrorText(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.error,
        modifier = Modifier.padding(start = PADDING_MEDIUM)
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
private fun DoneRatio(
    shoppingList: List<ShoppingItemEntity>, modifier: Modifier
) {
    var itemsTotal: Int = 0
    var itemsDone: Int = 0

    for (item in shoppingList) {
        itemsTotal++
        if (item.isItemChecked) itemsDone++
    }

    Text(
        text = "$itemsDone/$itemsTotal", modifier = modifier, style = MaterialTheme.typography.body1
    )
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
        visible = isExpanded, enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(
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
            IconButton(onClick = onOpenGroupIconClicked) {
                Icon(
                    imageVector = Icons.Filled.Search, contentDescription = stringResource(
                        R.string.content_description_go_to_items_of_group_screen_icon
                    ), tint = MaterialTheme.colors.primary
                )
            }
            Column {
                val order = listOrder.sortedBy {
                    it.itemPositionInList
                }

                for (position in order) {
                    for (item in shoppingList) {
                        if (position.itemId == item.itemId) {

                            val itemFontStyle = if (item.isItemChecked) {
                                TextStyle(
                                    textDecoration = TextDecoration.LineThrough,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            } else {
                                MaterialTheme.typography.body1
                            }

                            val itemQuantityToDisplay = if (item.itemQuantity == null) {
                                ""
                            } else {
                                " -- " + item.itemQuantity.toString().dropLastWhile { it == '0' }
                                    .dropLastWhile { it == '.' } + " "
                            }

                            Text(
                                text = item.itemName + itemQuantityToDisplay + item.itemUnit,
                                style = itemFontStyle,
                                modifier = modifier.padding(vertical = PADDING_X_SMALL)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = modifier.weight(1f))
        }
    }
}

@Composable
fun GroupCard(
    titleGroupName: String,
    onDeleteGroupClicked: () -> Unit,
//    onReorderButtonClicked: () -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {

        val (card, buttonDelete) = createRefs()

        Card(
            elevation = ELEVATION_MEDIUM,
            modifier = modifier
                .padding(vertical = PADDING_LARGE)
                .constrainAs(card) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            shape = MaterialTheme.shapes.large
        ) {
            CardTitle(
                titleGroupName = titleGroupName,
                modifier = modifier.padding(horizontal = PADDING_LARGE, vertical = PADDING_SMALL)
            )
        }

        IconButton(onClick = onDeleteGroupClicked, modifier = modifier.constrainAs(buttonDelete) {
            start.linkTo(card.end)
            top.linkTo(card.top)
            bottom.linkTo(card.bottom)
        }) {
            Icon(
                Icons.Filled.Delete,
                tint = MaterialTheme.colors.primary,
                contentDescription = stringResource(R.string.content_description_delete_group_icon)
            )
        }
    }
}

@Composable
fun ItemCardsRearrange(
    shoppingListItems: List<ShoppingItemEntity>,
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
            var shoppingListItem by mutableStateOf(
                ShoppingItemEntity(
                    itemId = null,
                    itemParentId = null,
                    itemName = "",
                    itemQuantity = null,
                    itemUnit = "",
                    isItemChecked = true
                )
            )
            for (i in shoppingListItems) {
                if (i.itemId == listOrderId.toLong()) shoppingListItem = i
            }

            val isItemChecked by mutableStateOf(shoppingListItem.isItemChecked)

            ReorderableItem(reorderableState = state, key = listOrderId) { isDragging ->
                val elevation = animateDpAsState(
                    targetValue =
                    if (isDragging) 16.dp else ELEVATION_SMALL
                )
                val padding = animateDpAsState(
                    targetValue =
                    if (isDragging) PADDING_MEDIUM else PADDING_SMALL
                )
                val border = animateDpAsState(
                    targetValue =
                    if (isDragging) 4.dp else 0.dp
                )
                val borderColor = animateColorAsState(
                    targetValue =
                    if (isDragging) MaterialTheme.colors.primary else MaterialTheme.colors.surface
                )

                Card(
                    elevation = elevation.value,
                    shape = MaterialTheme.shapes.large,
                    modifier = modifier
                        .padding(PADDING_SMALL),
                    border = BorderStroke(border.value, borderColor.value)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.padding(padding.value)
                    ) {

                        val itemFontStyle = if (isItemChecked) {
                            TextStyle(
                                textDecoration = TextDecoration.LineThrough,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        } else {
                            TextStyle(
                                textDecoration = TextDecoration.None,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        }

                        val itemQuantityToDisplay = if (shoppingListItem.itemQuantity == null) {
                            ""
                        } else {
                            " -- " + shoppingListItem.itemQuantity.toString()
                                .dropLastWhile { it == '0' }.dropLastWhile { it == '.' } + " "
                        }
                        DragIcon()
                        Text(
                            text = shoppingListItem.itemName + itemQuantityToDisplay + shoppingListItem.itemUnit,
                            style = itemFontStyle,
                            modifier = modifier.padding(PADDING_X_SMALL)
                        )
                        DragIcon()
                    }
                }
            }
        }
    }
}

@Composable
fun DragIcon() {
    Icon(
        imageVector = Icons.Filled.DragIndicator,
        contentDescription = stringResource(R.string.drag_icon_content_description)
    )
}

@Composable
fun ItemCards(
    shoppingListItems: List<ShoppingItemEntity>,
    itemPositions: List<ListOrderEntity>,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    onDeleteItemClicked: (itemId: Long?, groupId: Long?) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        state = rememberLazyListState()
    ) {
        val order = mutableStateOf(itemPositions.sortedBy {
            it.itemPositionInList
        })

        items(order.value) { listOrder ->

            val shoppingListItem = shoppingListItems.find { shoppingItem ->
                shoppingItem.itemId == listOrder.itemId
            }

            if (shoppingListItem != null) {
                var isItemChecked by mutableStateOf(shoppingListItem.isItemChecked)

                Card(
                    elevation = ELEVATION_SMALL,
                    shape = MaterialTheme.shapes.large,
                    modifier = modifier.padding(PADDING_SMALL)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        ItemCheckboxIconButton(
                            isItemChecked = isItemChecked, onCheckboxClicked = {
                                onCheckboxClicked(shoppingListItem)
                                isItemChecked = !isItemChecked
                            }, modifier = modifier.padding(start = PADDING_X_SMALL)
                        )
                        Item(
                            item = shoppingListItem,
                            modifier = modifier
                        )
                        DeleteItemIconButton(
                            onDeleteItemClicked = {
                                onDeleteItemClicked(
                                    shoppingListItem.itemId,
                                    shoppingListItem.itemParentId
                                )
                            },
                            modifier = modifier.padding(end = PADDING_X_SMALL)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Item(
    item: ShoppingItemEntity,
    modifier: Modifier
) {
    val itemQuantityToDisplay = if (item.itemQuantity == null) {
        ""
    } else {
        " -- " + item.itemQuantity.toString().dropLastWhile { it == '0' }
            .dropLastWhile { it == '.' } + " "
    }
    Text(
        text = item.itemName + itemQuantityToDisplay + item.itemUnit,
        modifier = modifier.padding(PADDING_X_SMALL)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ItemCheckboxIconButton(
    isItemChecked: Boolean,
    onCheckboxClicked: () -> Unit,
    modifier: Modifier
) {
//    TODO
//    val (isChecked, setChecked) = remember { mutableStateOf(false) }
//    MaterialTheme {
//        Surface {
//            FavoriteButton(
//                isChecked = isChecked,
//                onClick = { setChecked(!isChecked) }
//            )
//        }
//    }
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        IconButton(
            onClick = onCheckboxClicked, modifier = modifier.size(SIZE_ICONS_OFFICIAL)
        ) {
            if (isItemChecked) {
                Icon(
                    imageVector = Icons.Filled.CheckBox, contentDescription = stringResource(
                        R.string.content_description_checkbox_done_item
                    )
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.CheckBoxOutlineBlank,
                    contentDescription = stringResource(
                        R.string.content_description_checkbox_item_not_done
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DeleteItemIconButton(
    onDeleteItemClicked: () -> Unit,
    modifier: Modifier
) {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        IconButton(
            onClick = onDeleteItemClicked,
            modifier = modifier.size(SIZE_ICONS_OFFICIAL)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.delete_item_icon)
            )
        }
    }
}

@Composable
private fun ExpandIcon(
    isExpanded: Boolean, onExpandIconClicked: () -> Unit, modifier: Modifier
) {
    val expandIconAngle: Float by animateFloatAsState(
        targetValue = if (isExpanded) EXPAND_ICON_ROTATION_ANIMATION_END_DEGREES
        else EXPAND_ICON_ROTATION_ANIMATION_START_DEGREES
    )

    Column {
        IconButton(
            onClick = onExpandIconClicked
        ) {
            Surface(shape = CircleShape, modifier = modifier.size(SIZE_MEDIUM)) {
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = stringResource(
                        R.string.content_description_expand_group_icon
                    ),
                    tint = MaterialTheme.colors.primary,
                    modifier = modifier.rotate(expandIconAngle)
                )
            }
        }
    }
}

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
fun TrailingIconForErase(callback: () -> Unit) {
    IconButton(onClick = callback) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.content_description_erase_input_field),
            tint = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun ShowAlertDialog(
    title: String,
    message: String,
    isOpen: Boolean,
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            title = { Text(text = title) },
            text = { Text(text = message) },
            confirmButton = {
                Button(
                    onClick = onConfirmClicked,
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text(text = stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismissClicked, colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(text = stringResource(R.string.cancel_button))
                }
            },
            onDismissRequest = onDismissClicked
        )
    }
}
