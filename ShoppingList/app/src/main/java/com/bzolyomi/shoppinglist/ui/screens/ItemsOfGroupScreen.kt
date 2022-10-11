package com.bzolyomi.shoppinglist.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.data.ListOrderEntity
import com.bzolyomi.shoppinglist.data.ShoppingGroupEntity
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.ui.components.*
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_LARGE
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun ItemsOfGroupScreen(
    onDeleteGroupConfirmed: () -> Unit,
    onNavigationBarBackButtonClicked: () -> Unit,
    modifier: Modifier,
    sharedViewModel: SharedViewModel
) {
    BackHandler {
        onNavigationBarBackButtonClicked()
        sharedViewModel.flushItemGUI()
    }

    val shoppingGroup by mutableStateOf(sharedViewModel.selectedGroupWithList.group)
    val shoppingList by sharedViewModel.selectedShoppingList.collectAsState()
    val listOrder by sharedViewModel.selectedListOrder.collectAsState()

    val itemName by sharedViewModel.itemName
    val itemQuantity by sharedViewModel.itemQuantity
    val itemUnit by sharedViewModel.itemUnit

    var isAddItem by remember { mutableStateOf(false) }
    var isRearrange by remember { mutableStateOf(false) }

    var orderOfItemIds: List<ListOrderEntity> = mutableListOf()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ContentWithoutInput(
            shoppingGroup = shoppingGroup,
            shoppingList = shoppingList,
            listOrder = listOrder,
            isRearrange = isRearrange,
            onDeleteGroupConfirmed = {
                onDeleteGroupConfirmed()
                sharedViewModel.deleteGroupAndItsItems()
            },
            onDeleteItemClicked = {
                sharedViewModel.deleteItem(
                    itemId = it,
                    groupId = shoppingGroup.groupId
                )
            },
            onCheckboxClicked = {
                sharedViewModel.updateItemChecked(it)
            },
            onItemsOrderChange = {
                orderOfItemIds = it
//                sharedViewModel.updateListOrder(it)
            },
            modifier = Modifier
        )
        if (isAddItem) {
            val scope = rememberCoroutineScope()

            ItemInputFields(
                itemName = itemName,
                itemQuantity = itemQuantity,
                itemUnit = itemUnit,
                onSubmitAddItemButtonClicked = {
                    sharedViewModel.setGroupName(shoppingGroup.groupName)
                    scope.launch { sharedViewModel.createWithCoroutines() }
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
                if (!isRearrange) {
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
                }
                Button(
                    onClick = {
                        if (isRearrange) {
                            sharedViewModel.updateListOrder(orderOfItemIds)
                        }
                        isRearrange = !isRearrange
                    },
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
                    if (!isRearrange) Text(text = "Rearrange") else Text(text = "Rearrange done")
                }
            }
        }
    }
}

@Composable
fun ContentWithoutInput(
    shoppingGroup: ShoppingGroupEntity,
    shoppingList: List<ShoppingItemEntity>,
    listOrder: List<ListOrderEntity>,
    isRearrange: Boolean,
    onDeleteGroupConfirmed: () -> Unit,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    onItemsOrderChange: (List<ListOrderEntity>) -> Unit,
    modifier: Modifier
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
            listOrder = listOrder,
            isRearrange = isRearrange,
            onDeleteItemClicked = onDeleteItemClicked,
            onCheckboxClicked = onCheckboxClicked,
            onItemsOrderChange = onItemsOrderChange,
            modifier = modifier
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
    listOrder: List<ListOrderEntity>,
    isRearrange: Boolean,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    onDeleteItemClicked: (itemId: Long?) -> Unit,
    onItemsOrderChange: (List<ListOrderEntity>) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (isRearrange) {
            ItemCardsRearrange(
                shoppingListItems = shoppingListItems,
                itemPositions = listOrder,
                onItemsOrderChange = onItemsOrderChange,
                modifier = modifier
            )
        } else {
            ItemCards(
                shoppingListItems = shoppingListItems,
                itemPositions = listOrder,
                onCheckboxClicked = onCheckboxClicked,
                onDeleteItemClicked = onDeleteItemClicked,
                modifier = modifier
            )
        }
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