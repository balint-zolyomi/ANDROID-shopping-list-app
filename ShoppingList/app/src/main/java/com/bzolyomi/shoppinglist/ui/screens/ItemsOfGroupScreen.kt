package com.bzolyomi.shoppinglist.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.data.ListOrderEntity
import com.bzolyomi.shoppinglist.data.ShoppingItemEntity
import com.bzolyomi.shoppinglist.ui.components.*
import com.bzolyomi.shoppinglist.ui.theme.FloatingActionButtonTint
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun ItemsOfGroupScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToAddItemScreen: (Long?) -> Unit,
    modifier: Modifier,
    sharedVM: SharedViewModel
) {

    var isReordering by remember { mutableStateOf(false) }

    BackHandler {
        if (isReordering) {
            isReordering = !isReordering
        } else {
            navigateToHomeScreen()
            sharedVM.flushItemGUI()
            sharedVM.setGroupName("")
        }
    }

    val context = LocalContext.current
    val reorderHint = stringResource(R.string.toast_message_reorder_hint)
    val toastMessageForGroupDelete = stringResource(R.string.toast_message_group_deleted)

    val scaffoldState = rememberScaffoldState()

    val shoppingGroup by mutableStateOf(sharedVM.selectedGroupWithList.group)
    val shoppingList by sharedVM.selectedShoppingList.collectAsState()
    val listOrder by sharedVM.selectedListOrder.collectAsState()

    var orderOfItemIds: List<ListOrderEntity>

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = shoppingGroup.groupName) },
                actions = {
                    AppBarOptionToggleReorder(
                        isReordering = isReordering,
                        onReorderButtonToggled = {
                            if (!isReordering) {
                                Toast.makeText(
                                    context,
                                    reorderHint,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            isReordering = !isReordering
                        }
                    )
                    AppBarOptionMore(
                        dropdownItemTitle = stringResource(
                            R.string.delete_all_appbar_dropdown_menu_option_delete_group
                        ),
                        alertDialogMessage = stringResource(
                            R.string.delete_group_alert_dialog_message
                        ),
                        onConfirmClicked = {
                            navigateToHomeScreen()
                            Toast.makeText(context, toastMessageForGroupDelete, Toast.LENGTH_SHORT)
                                .show()
                            sharedVM.deleteGroup(groupId = shoppingGroup.groupId)
                            sharedVM.deleteItems(shoppingList)
                            sharedVM.deleteAllListOrders(groupId = shoppingGroup.groupId)
                        }
                    )
                },
                backgroundColor = MaterialTheme.colors.background
            )
        },
        modifier = modifier
            .fillMaxSize(),
        content = {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                ContentWithoutInput(
                    shoppingList = shoppingList,
                    listOrder = listOrder,
                    isReordering = isReordering,
                    onDeleteItemClicked = { itemId, groupId ->
                        sharedVM.deleteListOrder(
                            groupId = groupId,
                            itemId = itemId
                        )
                        sharedVM.deleteItem(
                            itemId = itemId
                        )
                    },
                    onCheckboxClicked = {
                        sharedVM.updateItemChecked(it)
                    },
                    onItemsOrderChange = {
                        orderOfItemIds = it
                        sharedVM.updateListOrder(orderOfItemIds)
                    },
                    modifier = Modifier
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    sharedVM.setGroupName(shoppingGroup.groupName)
                    navigateToAddItemScreen(shoppingGroup.groupId)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.fab_add_item),
                    tint = FloatingActionButtonTint,
                )
            }
        }
    )
}

@Composable
fun ContentWithoutInput(
    shoppingList: List<ShoppingItemEntity>,
    listOrder: List<ListOrderEntity>,
    isReordering: Boolean,
    onDeleteItemClicked: (itemId: Long?, groupId: Long?) -> Unit,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    onItemsOrderChange: (List<ListOrderEntity>) -> Unit,
    modifier: Modifier
) {
    Column {
        ItemsList(
            shoppingListItems = shoppingList,
            listOrder = listOrder,
            isReordering = isReordering,
            onDeleteItemClicked = onDeleteItemClicked,
            onCheckboxClicked = onCheckboxClicked,
            onItemsOrderChange = onItemsOrderChange,
            modifier = modifier
        )
    }
}

@Composable
fun ItemsList(
    shoppingListItems: List<ShoppingItemEntity>,
    listOrder: List<ListOrderEntity>,
    isReordering: Boolean,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    onDeleteItemClicked: (itemId: Long?, groupId: Long?) -> Unit,
    onItemsOrderChange: (List<ListOrderEntity>) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (isReordering) {
            ItemCardsRearrange(
                shoppingListItems = shoppingListItems,
                listOrderById = listOrder,
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
