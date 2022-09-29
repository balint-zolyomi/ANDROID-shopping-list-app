package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.ui.theme.Accent
import com.bzolyomi.shoppinglist.util.Constants.ELEVATION_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.ELEVATION_SMALL
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

@Composable
fun SubmitAddAllButton(onSubmitAddAllButtonClicked: () -> Unit) {
    Button(onClick = onSubmitAddAllButtonClicked) {
        Text(text = stringResource(R.string.submit_button_text))
    }
}

@Composable
fun SubmitAddItemButton(onSubmitAddItemButtonClicked: () -> Unit) {
    Button(onClick = onSubmitAddItemButtonClicked) {
        Text(text = stringResource(R.string.submit_button_text))
    }
}

@Composable
fun GroupAndItemsCard(
    titleGroupName: String,
    shoppingList: List<ShoppingItemEntity>,
    onOpenGroupIconClicked: () -> Unit,
    modifier: Modifier
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
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
            }
            CardContent(
                isExpanded = isExpanded,
                shoppingList = shoppingList,
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
                    imageVector = Icons.Filled.Search,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary
                )
            }
            Column {
                for (item in shoppingList) {
                    Text(
                        text = item.itemName + " -- "
                                + item.itemQuantity.toString()
                            .dropLastWhile { it == '0' }.dropLastWhile { it == '.' } + " "
                                + item.itemUnit,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = modifier.padding(vertical = PADDING_X_SMALL)
                    )
                }
            }
            Spacer(modifier = modifier.weight(1f))
        }
    }
}

@Composable
fun GroupCard(
    titleGroupName: String, onDeleteGroupClicked: () -> Unit, modifier: Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {

        val (card, button) = createRefs()

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
                modifier = modifier.padding(horizontal = PADDING_LARGE)
            )
        }
        IconButton(onClick = onDeleteGroupClicked, modifier = modifier.constrainAs(button) {
            start.linkTo(card.end)
            top.linkTo(card.top)
            bottom.linkTo(card.bottom)
        }) {
            Icon(Icons.Filled.Delete, tint = Accent, contentDescription = "")
        }
    }
}

@Composable
fun ItemCards(
    shoppingListItems: List<ShoppingItemEntity>,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    modifier: Modifier
) {
    // The composable function rememberLazyListState creates an initial state for the list
    // using rememberSaveable. When the Activity is recreated, the scroll state is
    // maintained without you having to code anything.
    LazyColumn(
        state = LazyListState()
    ) {
        itemsIndexed(shoppingListItems) { _, item ->

//                var offsetX by remember { mutableStateOf(0f) }
//                var offsetY by remember { mutableStateOf(0f) }
//                var alpha by remember { mutableStateOf(0f) }

            var isItemChecked by mutableStateOf(item.isItemChecked)

//            Row(
//                    .offset {
//                        IntOffset(
//                            offsetX.roundToInt(), offsetY.roundToInt()
//                        )
//                    }
//                    .background(
//                        brush = gradientBrush,
//                        shape = CutCornerShape(8.dp),
//                        alpha = alpha
//            ) {
            Card(
                elevation = ELEVATION_SMALL,
                shape = MaterialTheme.shapes.large,
                modifier = modifier.padding(PADDING_SMALL)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
//                    var itemsThatCrossed: List<ShoppingItemEntity> = emptyList()
//                    var globalY: Float = remember { 0f }
//                    isRearranging = sharedViewModel.isRearranging

//                    if (!isRearranging) {
//                        DragIcon()
//                    }
                    ItemCheckboxIconButton(
                        isItemChecked = isItemChecked,
                        onCheckboxClicked = {
                            onCheckboxClicked(item)
                            isItemChecked = !isItemChecked
                        },
                        modifier = modifier.padding(start = PADDING_X_SMALL)
                    )
                    Text(
                        text = item.itemName + " -- "
                                + item.itemQuantity.toString()
                            .dropLastWhile { it == '0' }.dropLastWhile { it == '.' } + " "
                                + item.itemUnit,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = modifier.padding(PADDING_X_SMALL)
                    )
                    DeleteItemIconButton(
                        item = item,
                        onDeleteItemClicked = onDeleteItemClicked,
                        modifier = modifier.padding(end = PADDING_X_SMALL)
                    )
                }
//                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DeleteItemIconButton(
    item: ShoppingItemEntity,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    modifier: Modifier
) {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        IconButton(
            onClick = { onDeleteItemClicked(item.itemId) },
            modifier = modifier.size(SIZE_ICONS_OFFICIAL)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.delete_item_icon)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ItemCheckboxIconButton(
    isItemChecked: Boolean, onCheckboxClicked: () -> Unit, modifier: Modifier
) {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        IconButton(
            onClick = onCheckboxClicked, modifier = modifier
                .size(SIZE_ICONS_OFFICIAL)
        ) {
            if (isItemChecked) {
                Icon(
                    imageVector = Icons.Filled.CheckBox,
                    contentDescription = ""
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.CheckBoxOutlineBlank,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun ExpandIcon(
    isExpanded: Boolean, onExpandIconClicked: () -> Unit, modifier: Modifier
) {
    Column {
        IconButton(
            onClick = onExpandIconClicked
        ) {
            Surface(shape = CircleShape, modifier = modifier.size(SIZE_MEDIUM)) {
                Icon(
                    imageVector = if (isExpanded) {
                        Icons.Filled.ExpandLess
                    } else Icons.Filled.ExpandMore,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary
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
            contentDescription = "",
            tint = MaterialTheme.colors.primary
        )
    }
}
