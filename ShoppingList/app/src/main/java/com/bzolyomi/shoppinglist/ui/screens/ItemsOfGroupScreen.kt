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
    onDeleteGroupConfirmed: () -> Unit,
    onNavigationBarBackButtonClicked: () -> Unit,
    onAddItemFABClicked: (Long?) -> Unit,
    modifier: Modifier,
    sharedViewModel: SharedViewModel
) {
    BackHandler {
        onNavigationBarBackButtonClicked()
        sharedViewModel.flushItemGUI()
        sharedViewModel.setGroupName("")
    }

    val context = LocalContext.current
    val reorderHint = stringResource(R.string.toast_message_reorder_hint)

    val scaffoldState = rememberScaffoldState()

    val shoppingGroup by mutableStateOf(sharedViewModel.selectedGroupWithList.group)
    val shoppingList by sharedViewModel.selectedShoppingList.collectAsState()
    val listOrder by sharedViewModel.selectedListOrder.collectAsState()

//    val isRearrange by remember { mutableStateOf(false) }

    var orderOfItemIds: List<ListOrderEntity> = mutableListOf()

    var isReordering by remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                appBarTitle = shoppingGroup.groupName,
                isShowingReorderIcon = true,
                isReordering = isReordering,
                appBarDropDownTitle = stringResource(
                    R.string.delete_all_appbar_dropdown_menu_option_delete_group
                ),
                alertDialogMessage = stringResource(R.string.delete_group_alert_dialog_message),
                onDeleteClicked = {
                    onDeleteGroupConfirmed()
                    sharedViewModel.deleteGroup(groupId = shoppingGroup.groupId)
                    sharedViewModel.deleteItems(shoppingList)
                    sharedViewModel.deleteAllListOrders(groupId = shoppingGroup.groupId)
                },
                onReorderButtonToggled = {
                    if (isReordering) {
                        sharedViewModel.updateListOrder(orderOfItemIds)
                    } else {
                        Toast.makeText(
                            context,
                            reorderHint,
                            Toast.LENGTH_LONG).show()
                    }
                    isReordering = !isReordering
                }
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
//                    shoppingGroup = shoppingGroup,
                    shoppingList = shoppingList,
                    listOrder = listOrder,
                    isReordering = isReordering,
                    onDeleteGroupConfirmed = {
                        onDeleteGroupConfirmed()
                        sharedViewModel.deleteGroupAndItsItems()
                    },
                    onDeleteItemClicked = { itemId, groupId ->
                        sharedViewModel.deleteListOrder(
                            groupId = groupId,
                            itemId = itemId
                        )
                        sharedViewModel.deleteItem(
                            itemId = itemId
                        )
                    },
                    onCheckboxClicked = {
                        sharedViewModel.updateItemChecked(it)
                    },
                    onItemsOrderChange = {
                        orderOfItemIds = it
                    },
                    modifier = Modifier
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    sharedViewModel.setGroupName(shoppingGroup.groupName)
                    onAddItemFABClicked(shoppingGroup.groupId)
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
//    shoppingGroup: ShoppingGroupEntity,
    shoppingList: List<ShoppingItemEntity>,
    listOrder: List<ListOrderEntity>,
    isReordering: Boolean,
    onDeleteGroupConfirmed: () -> Unit,
    onDeleteItemClicked: (itemId: Long?, groupId: Long?) -> Unit,
    onCheckboxClicked: (ShoppingItemEntity) -> Unit,
    onItemsOrderChange: (List<ListOrderEntity>) -> Unit,
    modifier: Modifier
) {
    var isAlertDialogOpen by remember { mutableStateOf(false) }

    Column {
//        GroupCard(
//            titleGroupName = shoppingGroup.groupName,
//            onDeleteGroupClicked = { isAlertDialogOpen = true },
//            modifier = modifier
//        )
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

    ShowAlertDialog(
        title = stringResource(R.string.delete_alert_dialog_title),
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
