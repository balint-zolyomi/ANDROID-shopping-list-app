package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.data.GroupWithList
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.ui.components.*
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.PADDING_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_LARGE
import com.bzolyomi.shoppinglist.util.Constants.PADDING_ZERO

@Composable
fun ItemsOfGroupScreen(
    selectedGroupWithList: GroupWithList?,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    onDeleteGroupClicked: (groupId: Long?, shoppingList: List<ShoppingItemEntity>) -> Unit,
    itemName: String,
    itemQuantity: String,
    itemUnit: String,
    onItemNameChange: (String) -> Unit,
    onItemQuantityChange: (String) -> Unit,
    onItemUnitChange: (String) -> Unit,
    onSubmitAddItemButtonClicked: (Long?) -> Unit,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
//    sharedViewModel: SharedViewModel,
//    onItemsRearrangedOnGUI: (MutableMap<Int, Float>) -> Unit
) {
    if (selectedGroupWithList != null) {
        var addItem by remember { mutableStateOf(false) }

        Column {
            ContentWithoutInput(
                selectedGroupWithList,
                onDeleteGroupClicked,
                onDeleteItemClicked,
                onCheckboxClicked,
                modifier = Modifier
//                sharedViewModel,
//                onItemsRearrangedOnGUI
            )
            if (addItem) {
                ItemInputFields(
                    itemName = itemName,
                    itemQuantity = itemQuantity,
                    itemUnit = itemUnit,
                    onItemNameChange = { onItemNameChange(it) },
                    onItemQuantityChange = { onItemQuantityChange(it) },
                    onItemUnitChange = { onItemUnitChange(it) },
                    onSubmitAddItemButtonClicked = {
                        onSubmitAddItemButtonClicked(selectedGroupWithList.group.groupId)
                        addItem = false
                    },
                    onCancelButtonClicked = {
                        addItem = false
                    }
                )
            } else {
                Button(
                    onClick = { addItem = true },
                    modifier = Modifier.padding(
                        start = PADDING_X_LARGE,
                        top = PADDING_MEDIUM,
                        end = PADDING_MEDIUM,
                        bottom = PADDING_MEDIUM
                    )
                ) {
                    Text(text = stringResource(R.string.add_item_button))
                }
            }
        }
    }
}

@Composable
fun ItemInputFields(
    itemName: String,
    itemQuantity: String,
    itemUnit: String,
    onItemNameChange: (String) -> Unit,
    onItemQuantityChange: (String) -> Unit,
    onItemUnitChange: (String) -> Unit,
    onSubmitAddItemButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
) {
    Column(modifier = Modifier.padding(PADDING_MEDIUM)) {
        ItemNameInput(
            itemName = itemName,
            onItemNameChange = { onItemNameChange(it) })
        ItemQuantityInput(
            itemQuantity,
            onItemQuantityChange = { onItemQuantityChange(it) })
        ItemUnitInput(
            itemUnit,
            onItemUnitChange = { onItemUnitChange(it) }
        )
        Row(modifier = Modifier.padding(horizontal = PADDING_MEDIUM)) {
            SubmitAddItemButton(onSubmitAddItemButtonClicked = onSubmitAddItemButtonClicked)
            Spacer(modifier = Modifier.weight(1f))
            CancelButton(onCancelButtonClicked = onCancelButtonClicked)
        }
    }
}

@Composable
fun CancelButton(onCancelButtonClicked: () -> Unit) {
    Button(onClick = onCancelButtonClicked) {
        Text(text = stringResource(R.string.cancel_button))
    }
}

@Composable
fun ContentWithoutInput(
    selectedGroupWithList: GroupWithList,
    onDeleteGroupClicked: (groupId: Long?, shoppingList: List<ShoppingItemEntity>) -> Unit,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    modifier: Modifier
//    sharedViewModel: SharedViewModel,
//    onItemsRearrangedOnGUI: (MutableMap<Int, Float>) -> Unit
) {
    Column {
        GroupCard(
            titleGroupName = selectedGroupWithList.group.groupName,
            onDeleteGroupClicked = {
                onDeleteGroupClicked(
                    selectedGroupWithList.group.groupId,
                    selectedGroupWithList.shoppingList
                )
            },
            modifier = modifier
        )
        ItemsList(
            shoppingListItems = selectedGroupWithList.shoppingList,
            onDeleteItemClicked = onDeleteItemClicked,
            onCheckboxClicked = onCheckboxClicked,
            modifier = modifier
//        sharedViewModel = sharedViewModel,
//        onItemsRearrangedOnGUI = onItemsRearrangedOnGUI
        )
    }
}

@Composable
fun ItemsList(
    shoppingListItems: List<ShoppingItemEntity>,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    modifier: Modifier
//    sharedViewModel: SharedViewModel,
//    onItemsRearrangedOnGUI: (MutableMap<Int, Float>) -> Unit
) {
//    var isRearranging by mutableStateOf(false)
//    isRearranging = sharedViewModel.isRearranging
//    val yPositionOfItems by sharedViewModel.yPositionOfItems.collectAsState()
//
//    val gradientBrush = Brush.horizontalGradient(
//        colors = listOf(Color.Red, Color.Blue, Color.Green),
//        startX = 0.0f,
//        endX = 500.0f
//    )

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        ItemCards(
            shoppingListItems = shoppingListItems,
            onCheckboxClicked = onCheckboxClicked,
            onDeleteItemClicked = onDeleteItemClicked,
            modifier = modifier
        )
    }
}


//@Composable
//fun DragIcon() {
//    Icon(
//        imageVector = Icons.Filled.DragIndicator,
//        contentDescription = "",
//        modifier = Modifier
//            .onGloballyPositioned {
//                globalY = it.positionInRoot().y
//                yPositionOfItems[index] = globalY
//            }
//            .pointerInput(Unit) {
//                detectDragGestures { change, dragAmount ->
//                    change.consume()
//
//                    var canDragUp = offsetY > -12f
//                    var canDragDown = offsetY < 1200f
//
//                    if (canDragDown && dragAmount.y > 0) {
//                        if (offsetY + dragAmount.y > 1200f) {
//                            offsetY = 1200f
//                        } else {
//                            offsetY += dragAmount.y
//                        }
//                        alpha = 0.75f
//                    } else if (canDragUp && dragAmount.y < 0) {
//                        if (offsetY + dragAmount.y < 12f) {
//                            offsetY =
//                                -12f
//                        } else {
//                            offsetY += dragAmount.y
//                        }
//                        alpha = 0.75f
////                                        itemsThatCrossed = listOf(
////                                            shoppingListItems.elementAt(index = index),
////                                            shoppingListItems.elementAt(index = index + 1)
////                                        )
//                    }
//
////                                    if (
////                                        shoppingListItems.lastIndex != index
////                                        && globalY > shoppingListItems[index + 1].
////                                    )
//
//                    onItemsRearrangedOnGUI(yPositionOfItems)
////                                    LaunchedEffect(key1 = true) {
////                                        Log.d("balint-debug", yPositionOfItems.toString())
////                                        sharedViewModel.rearrangeItems(
////                                            shoppingListItems = shoppingListItems,
////                                            yPositionOfItems = yPositionOfItems
////                                        )
////                                    }
//                }
//            })
//}