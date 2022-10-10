package com.bzolyomi.shoppinglist.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.data.ShoppingGroupEntity
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.ui.components.*
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.PADDING_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_LARGE
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun ItemsOfGroupScreen(
    onDeleteGroupConfirmed: () -> Unit,
    onNavigationBarBackButtonClicked: () -> Unit,
    modifier: Modifier,
    sharedViewModel: SharedViewModel,
//    onItemsRearrangedOnGUI: (MutableMap<Int, Float>) -> Unit
) {
    BackHandler {
        onNavigationBarBackButtonClicked()
        sharedViewModel.flushItemGUI()
    }

    val shoppingGroup by mutableStateOf(sharedViewModel.selectedGroupWithList.group)
    val shoppingList by sharedViewModel.selectedShoppingList.collectAsState()

    val positionOfItems: MutableList<Long?> = mutableListOf()
    for (item in shoppingList) {
        positionOfItems.add(item.itemId)
    }
//    Log.d("balint-debug", " \n${positionOfItems.toString()}")

    val itemName by sharedViewModel.itemName
    val itemQuantity by sharedViewModel.itemQuantity
    val itemUnit by sharedViewModel.itemUnit

    var isAddItem by remember { mutableStateOf(false) }
    var isRearranging by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ContentWithoutInput(
            shoppingGroup = shoppingGroup,
            shoppingList = shoppingList,
            isRearranging = isRearranging,
            onDeleteGroupConfirmed = {
                onDeleteGroupConfirmed()
                sharedViewModel.deleteGroupAndItsItems()
            },
            onDeleteItemClicked = {
                sharedViewModel.deleteItem(it)
            },
            onCheckboxClicked = {
                sharedViewModel.updateItemChecked(it)
            },
            modifier = Modifier
            // sharedViewModel,
            // onItemsRearrangedOnGUI
        )
        if (isAddItem) {
            val scope = rememberCoroutineScope()

            ItemInputFields(
                itemName = itemName,
                itemQuantity = itemQuantity,
                itemUnit = itemUnit,
                onSubmitAddItemButtonClicked = {
                    scope.launch { sharedViewModel.createItems(groupId = shoppingGroup.groupId) }
                    isAddItem = false
                },
                onCancelButtonClicked = {
                    sharedViewModel.flushItemGUI()
                    isAddItem = false
                },
                sharedViewModel = sharedViewModel
            )
        } else {
            Row {
                Button(
                    onClick = { isAddItem = true },
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
                Button(
                    onClick = { isRearranging = !isRearranging },
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
                    if (isRearranging) {
                        Text(text = stringResource(R.string.button_rearrange_items_done))
                    } else {
                        Text(text = stringResource(R.string.button_rearrange_items))
                    }
                }
            }
        }
    }
}

@Composable
fun ContentWithoutInput(
    shoppingGroup: ShoppingGroupEntity,
    shoppingList: List<ShoppingItemEntity>,
    isRearranging: Boolean,
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
            titleGroupName = shoppingGroup.groupName,
            onDeleteGroupClicked = { isAlertDialogOpen = true },
            modifier = modifier
        )
        ItemsList(
            shoppingListItems = shoppingList,
            isRearranging = isRearranging,
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
            isAlertDialogOpen = false
            onDeleteGroupConfirmed()
        },
        onDismissClicked = { isAlertDialogOpen = false }
    )
}

@Composable
fun ItemsList(
    shoppingListItems: List<ShoppingItemEntity>,
    isRearranging: Boolean,
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
            isRearranging = isRearranging,
            onCheckboxClicked = onCheckboxClicked,
            onDeleteItemClicked = onDeleteItemClicked,
            modifier = modifier
        )
    }
}

@Composable
fun ItemInputFields(
    itemName: String,
    itemQuantity: String,
    itemUnit: String,
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
                if (!isItemNameError || it == "") sharedViewModel.setItemName(it)
            },
            onEraseItemNameInputButtonClicked = {
                sharedViewModel.setItemName("")
                validateItemNameInput(sharedViewModel.itemName.value)
            },
            onNextInItemNameInputClicked = { validateItemNameInput(itemName) }
        )
        ItemQuantityInput(
            itemQuantity = itemQuantity,
            isError = isItemQuantityError,
            onItemQuantityChange = { sharedViewModel.setItemQuantity(it) },
            onEraseItemQuantityInputButtonClicked = {
                sharedViewModel.setItemQuantity("")
                validateItemQuantityInput(sharedViewModel.itemQuantity.value)
            },
            onNextInItemQuantityInputClicked = { validateItemQuantityInput(itemQuantity) }
        )
        ItemUnitInput(
            itemUnit,
            onItemUnitChange = { sharedViewModel.setItemUnit(it) },
            onEraseItemUnitInputButtonClicked = {
                sharedViewModel.setItemUnit("")
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

/*

@Composable
fun DragIcon(
//    offsetY: Float,
    onDragAmount: (Float) -> Unit
) {
    Icon(
        imageVector = Icons.Filled.DragIndicator,
        contentDescription = "",
        modifier = Modifier
            .padding(start = PADDING_SMALL)
            .onGloballyPositioned {
//                globalY = it.positionInRoot().y
//                yPositionOfItems[index] = globalY
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDragAmount(dragAmount.y)
                }
            }
    )
}
*/

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
//                                        itemsThatCrossed = listOf(
//                                            shoppingListItems.elementAt(index = index),
//                                            shoppingListItems.elementAt(index = index + 1)
//                                        )
//                    }

//                                    if (
//                                        shoppingListItems.lastIndex != index
//                                        && globalY > shoppingListItems[index + 1].
//                                    )

//                    onItemsRearrangedOnGUI(yPositionOfItems)
//                                    LaunchedEffect(key1 = true) {
//                                        Log.d("balint-debug", yPositionOfItems.toString())
//                                        sharedViewModel.rearrangeItems(
//                                            shoppingListItems = shoppingListItems,
//                                            yPositionOfItems = yPositionOfItems
//                                        )
//                                    }
//                }
//            })
//}