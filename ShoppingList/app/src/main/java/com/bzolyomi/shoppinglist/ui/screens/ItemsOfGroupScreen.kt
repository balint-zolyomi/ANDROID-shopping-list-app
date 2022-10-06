package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.data.GroupWithList
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.ui.components.*
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_LARGE
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun ItemsOfGroupScreen(
    selectedGroupWithList: GroupWithList?,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    onDeleteGroupConfirmed: (groupId: Long?, shoppingList: List<ShoppingItemEntity>) -> Unit,
    itemName: String,
    itemQuantity: String,
    itemUnit: String,
    onItemNameChange: (String) -> Unit,
    onItemQuantityChange: (String) -> Unit,
    onItemUnitChange: (String) -> Unit,
    onSubmitAddItemButtonClicked: (Long?) -> Unit,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    onCancelAddItemButtonClicked: () -> Unit,
    modifier: Modifier,
    sharedViewModel: SharedViewModel,
//    onItemsRearrangedOnGUI: (MutableMap<Int, Float>) -> Unit
) {

    if (selectedGroupWithList != null) {
        var addItem by remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            ContentWithoutInput(
                selectedGroupWithList = selectedGroupWithList,
                onDeleteGroupConfirmed = {
                    onDeleteGroupConfirmed(
                        selectedGroupWithList.group.groupId,
                        selectedGroupWithList.shoppingList
                    )
                },
                onDeleteItemClicked = onDeleteItemClicked,
                onCheckboxClicked = onCheckboxClicked,
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
                        onCancelAddItemButtonClicked()
                        addItem = false
                    },
                    sharedViewModel = sharedViewModel
                )
            } else {
                Button(
                    onClick = { addItem = true },
                    modifier = Modifier.padding(
                        start = PADDING_X_LARGE,
                        top = PADDING_MEDIUM,
                        end = PADDING_MEDIUM,
                        bottom = PADDING_MEDIUM
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
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
    onCancelButtonClicked: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    var isItemNameError by rememberSaveable { mutableStateOf(false) }
    var isItemQuantityError by rememberSaveable { mutableStateOf(false) }

    fun validateItemNameInput(itemNameInput: String) {
        isItemNameError = itemNameInput.isBlank()
    }

    fun validateItemQuantityInput(itemQuantityInput: String) {
        isItemQuantityError = try {
            val tempQuantity = itemQuantityInput.replace(",", ".")
            tempQuantity.toFloat()
            false
        } catch (e: Exception) {
            itemQuantityInput != ""
        }
    }

    Column(
        modifier = Modifier.padding(PADDING_MEDIUM)
    ) {
        ItemNameInput(
            itemName = itemName,
            isError = isItemNameError,
            onItemNameChange = {
                validateItemNameInput(it)
                if (!isItemNameError || it == "") onItemNameChange(it)
            },
            onEraseItemNameInputButtonClicked = {
                sharedViewModel.itemName = ""
                validateItemNameInput(sharedViewModel.itemName)
            },
            onNextInItemNameInputClicked = { validateItemNameInput(itemName) }
        )
        ItemQuantityInput(
            itemQuantity = itemQuantity,
            isError = isItemQuantityError,
            onItemQuantityChange = {
                onItemQuantityChange(it)
            },
            onEraseItemQuantityInputButtonClicked = {
                sharedViewModel.itemQuantity = ""
                validateItemQuantityInput(sharedViewModel.itemQuantity)
            },
            onNextInItemQuantityInputClicked = { validateItemQuantityInput(itemQuantity) }
        )
        ItemUnitInput(
            itemUnit,
            onItemUnitChange = { onItemUnitChange(it) },
            onEraseItemUnitInputButtonClicked = {
                sharedViewModel.itemUnit = ""
            },
            onDone = {
                validateItemNameInput(itemName)
                validateItemQuantityInput(itemQuantity)
                if (!isItemNameError && !isItemQuantityError) {
                    onSubmitAddItemButtonClicked()
                }
            }
        )
        Row(modifier = Modifier.padding(horizontal = PADDING_MEDIUM)) {
            SubmitAddItemButton(onSubmitAddItemButtonClicked = {
                validateItemNameInput(itemName)
                validateItemQuantityInput(itemQuantity)
                if (!isItemNameError && !isItemQuantityError) {
                    onSubmitAddItemButtonClicked()
                }
            })
            Spacer(modifier = Modifier.weight(1f))
            CancelButton(onCancelButtonClicked = onCancelButtonClicked)
        }
    }
}

@Composable
fun CancelButton(onCancelButtonClicked: () -> Unit) {
    Button(
        onClick = onCancelButtonClicked,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
    ) {
        Text(text = stringResource(R.string.cancel_button))
    }
}

@Composable
fun ContentWithoutInput(
    selectedGroupWithList: GroupWithList,
    onDeleteGroupConfirmed: () -> Unit,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    modifier: Modifier
//    sharedViewModel: SharedViewModel,
//    onItemsRearrangedOnGUI: (MutableMap<Int, Float>) -> Unit
) {
    var isAlertDialogOpen by remember { mutableStateOf(false) }

    Column {
        GroupCard(
            titleGroupName = selectedGroupWithList.group.groupName,
            onDeleteGroupClicked = { isAlertDialogOpen = true },
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

    ShowAlertDialog(
        title = stringResource(R.string.delete_all_alert_dialog_title),
        message = stringResource(R.string.delete_shopping_group_and_its_items_alert_dialog_message),
        isOpen = isAlertDialogOpen,
        onConfirmClicked = {
            onDeleteGroupConfirmed()
            isAlertDialogOpen = false
        },
        onDismissClicked = { isAlertDialogOpen = false }
    )
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