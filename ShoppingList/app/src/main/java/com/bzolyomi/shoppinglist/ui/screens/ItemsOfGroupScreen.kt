package com.bzolyomi.shoppinglist.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.data.GroupWithList
import com.bzolyomi.shoppinglist.data.ShoppingListEntity
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel
import kotlin.math.roundToInt

@Composable
fun ItemsOfGroupScreen(
    selectedGroupWithList: GroupWithList?,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    onDeleteGroupClicked: (groupId: Long?, shoppingList: List<ShoppingListEntity>) -> Unit,
    itemName: String,
    itemQuantity: String,
    itemUnit: String,
    isItemChecked: Boolean,
    onItemNameChange: (String) -> Unit,
    onItemQuantityChange: (String) -> Unit,
    onItemUnitChange: (String) -> Unit,
    onSubmitButtonClicked: (Long?) -> Unit,
    onCheckboxClicked: (ShoppingListEntity) -> Unit,
    sharedViewModel: SharedViewModel,
    onItemsRearrangedOnGUI: (MutableMap<Int, Float>) -> Unit
) {
    if (selectedGroupWithList != null) {
        var addItem by remember { mutableStateOf(false) }

        Column {
            ContentWithoutInput(
                selectedGroupWithList,
                isItemChecked,
                onDeleteGroupClicked,
                onDeleteItemClicked,
                onCheckboxClicked,
                sharedViewModel,
                onItemsRearrangedOnGUI
            )
            if (addItem) {
                Column(modifier = Modifier.padding(12.dp)) {
                    ItemNameInput(
                        itemName = itemName,
                        onItemNameChange = { onItemNameChange(it) })
                    ItemQuantityInput(
                        itemQuantity,
                        onItemQuantityChange = { onItemQuantityChange(it) })
                    ItemUnitInput(itemUnit, onItemUnitChange = { onItemUnitChange(it) })
                    Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                        SubmitButton(onSubmitButtonClicked = {
                            onSubmitButtonClicked(
                                selectedGroupWithList.group.id
                            )
                            addItem = false
                        })
                        Spacer(modifier = Modifier.weight(1f))
                        CancelButton(onCancelButtonClicked = { addItem = false })
                    }
                }
            } else {
                Button(onClick = { addItem = true }, modifier = Modifier.padding(12.dp)) {
                    Text(text = "ADD ITEM")
                }
            }
        }
    }
}

@Composable
fun ContentWithoutInput(
    selectedGroupWithList: GroupWithList,
    isItemChecked: Boolean,
    onDeleteGroupClicked: (groupId: Long?, shoppingList: List<ShoppingListEntity>) -> Unit,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    onCheckboxClicked: (ShoppingListEntity) -> Unit,
    sharedViewModel: SharedViewModel,
    onItemsRearrangedOnGUI: (MutableMap<Int, Float>) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = selectedGroupWithList.group.groupName.uppercase(),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp)
        )
        IconButton(onClick = {
            onDeleteGroupClicked(
                selectedGroupWithList.group.id,
                selectedGroupWithList.shoppingList
            )
        }) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete group")
        }
    }
    AllItems(
        shoppingListItems = selectedGroupWithList.shoppingList,
        isItemChecked = isItemChecked,
        onDeleteItemClicked = onDeleteItemClicked,
        onCheckboxClicked = onCheckboxClicked,
        sharedViewModel = sharedViewModel,
        onItemsRearrangedOnGUI = onItemsRearrangedOnGUI
    )
}

@Composable
fun CancelButton(onCancelButtonClicked: () -> Unit) {
    Button(onClick = onCancelButtonClicked) {
        Text(text = "CANCEL")
    }
}

@Composable
fun AllItems(
    shoppingListItems: List<ShoppingListEntity>,
    isItemChecked: Boolean,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    onCheckboxClicked: (ShoppingListEntity) -> Unit,
    sharedViewModel: SharedViewModel,
    onItemsRearrangedOnGUI: (MutableMap<Int, Float>) -> Unit
) {
    var isRearranging by mutableStateOf(false)
    isRearranging = sharedViewModel.isRearranging
    val yPositionOfItems by sharedViewModel.yPositionOfItems.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // The composable function rememberLazyListState creates an initial state for the list
        // using rememberSaveable. When the Activity is recreated, the scroll state is
        // maintained without you having to code anything.
        LazyColumn(
            state = LazyListState(), modifier = Modifier
                .padding(12.dp)
        ) {
            itemsIndexed(
                shoppingListItems
//                key = { item: ShoppingListEntity -> item.id!! } maybe switch IDs and refresh?
            ) { index, item ->

                var offsetX by remember { mutableStateOf(0f) }
                var offsetY by remember { mutableStateOf(0f) }
                var alpha by remember { mutableStateOf(0f) }

                val gradientBrush = Brush.horizontalGradient(
                    colors = listOf(Color.Red, Color.Blue, Color.Green),
                    startX = 0.0f,
                    endX = 500.0f
                )

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .offset {
                        IntOffset(
                            offsetX.roundToInt(), offsetY.roundToInt()
                        )
                    }
                    .background(
                        brush = gradientBrush,
                        shape = CutCornerShape(8.dp),
                        alpha = alpha
                    )
                ) {

                    var itemsThatCrossed: List<ShoppingListEntity> = emptyList()
                    var globalY: Float = remember { 0f }
//                    isRearranging = sharedViewModel.isRearranging

                    if (!isRearranging) {
                        Icon(
                            imageVector = Icons.Filled.DragIndicator,
                            contentDescription = "",
                            modifier = Modifier
                                .onGloballyPositioned {
                                    globalY = it.positionInRoot().y
                                    yPositionOfItems[index] = globalY
                                }
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()

                                        var canDragUp = offsetY > -12f
                                        var canDragDown = offsetY < 1200f

                                        if (canDragDown && dragAmount.y > 0) {
                                            if (offsetY + dragAmount.y > 1200f) {
                                                offsetY = 1200f
                                            } else {
                                                offsetY += dragAmount.y
                                            }
                                            alpha = 0.75f
                                        } else if (canDragUp && dragAmount.y < 0) {
                                            if (offsetY + dragAmount.y < 12f) {
                                                offsetY =
                                                    -12f
                                            } else {
                                                offsetY += dragAmount.y
                                            }
                                            alpha = 0.75f
//                                        itemsThatCrossed = listOf(
//                                            shoppingListItems.elementAt(index = index),
//                                            shoppingListItems.elementAt(index = index + 1)
//                                        )
                                        }

//                                    if (
//                                        shoppingListItems.lastIndex != index
//                                        && globalY > shoppingListItems[index + 1].
//                                    )

                                        onItemsRearrangedOnGUI(yPositionOfItems)
//                                    LaunchedEffect(key1 = true) {
//                                        Log.d("balint-debug", yPositionOfItems.toString())
//                                        sharedViewModel.rearrangeItems(
//                                            shoppingListItems = shoppingListItems,
//                                            yPositionOfItems = yPositionOfItems
//                                        )
//                                    }
                                    }
                                }
                        )
                    }
                    IconButton(onClick = { onCheckboxClicked(item) }) {
                        if (isItemChecked) {
                            Icon(imageVector = Icons.Filled.CheckBox, contentDescription = "")
                        } else {
                            Icon(
                                imageVector = Icons.Filled.CheckBoxOutlineBlank,
                                contentDescription = ""
                            )
                        }
                    }
                    Text(
                        text = "$index " + item.itemName + " -- " + item.itemQuantity + " "
                                + item.itemUnit,
//                      However sometimes you need to deviate slightly from the selection of colors
//                      and font styles. In those situations it's better to base your color or style
//                      on an existing one.
//                      For this, you can modify a predefined style by using the copy function.
                        style = MaterialTheme
                            .typography.subtitle1.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    IconButton(onClick = { onDeleteItemClicked(item.id) }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Delete item")
                    }
                }
            }
        }
//        Box(modifier = Modifier.fillMaxSize()) {
//            var offsetX by remember { mutableStateOf(0f) }
//            var offsetY by remember { mutableStateOf(0f) }
//
//            Box(
//                Modifier
//                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
//                    .background(Color.Blue)
//                    .size(50.dp)
//                    .pointerInput(Unit) {
//                        detectDragGestures { change, dragAmount ->
//                            change.consumeAllChanges()
//                            offsetX += dragAmount.x
//                            offsetY += dragAmount.y
//                        }
//                    }
//            )
//        }
    }
}