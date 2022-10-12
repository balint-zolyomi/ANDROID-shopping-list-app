package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.data.GroupWithList
import com.bzolyomi.shoppinglist.ui.components.GroupAndItemsCard
import com.bzolyomi.shoppinglist.ui.components.ShowAlertDialog
import com.bzolyomi.shoppinglist.ui.theme.FloatingActionButtonTint
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.PADDING_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_XX_LARGE
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun AllGroupsScreen(
    onAddAllFABClicked: () -> Unit,
    onOpenGroupIconClicked: (groupId: Long?) -> Unit,
    sharedViewModel: SharedViewModel,
    modifier: Modifier
) {
    val shoppingGroupsWithLists by sharedViewModel.shoppingGroupsWithLists.collectAsState()

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(onDeleteAllClicked = {
                for (groupWithList in shoppingGroupsWithLists) {
                    sharedViewModel.deleteGroup(groupId = groupWithList.group.groupId)
                    sharedViewModel.deleteItems(shoppingList = groupWithList.shoppingList)
                    sharedViewModel.deleteAllListOrders(groupId = groupWithList.group.groupId)
                }
            })
        },
        modifier = modifier
            .fillMaxSize(),
        content = {
            LazyColumn(
                contentPadding = WindowInsets.systemBars
                    .only(WindowInsetsSides.Vertical)
                    .add(
                        WindowInsets(
                            left = PADDING_SMALL,
                            right = PADDING_SMALL,
                            top = PADDING_MEDIUM,
                            bottom = PADDING_XX_LARGE
                        )
                    )
                    .asPaddingValues(),
                modifier = modifier
                    .fillMaxSize()
            ) {
                items(
                    items = shoppingGroupsWithLists
                ) { shoppingGroupWithList ->
                    if (shoppingGroupWithList != null) {
                        GroupAndItemsCard(
                            titleGroupName = shoppingGroupWithList.group.groupName,
                            shoppingList = shoppingGroupWithList.shoppingList ,
                            listOrder = shoppingGroupWithList.listOrder,
                            onOpenGroupIconClicked = {
                                onOpenGroupIconClicked(shoppingGroupWithList.group.groupId)
                            },
                            modifier = Modifier
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddAllFABClicked,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.content_description_fab),
                    tint = FloatingActionButtonTint,
                )
            }
        }
    )
}

@Composable
fun AppBar(
    onDeleteAllClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.all_groups_screen_appbar_title),
                color = MaterialTheme.colors.onBackground
            )
        },
        actions = {
            AppBarActions(onConfirmClicked = onDeleteAllClicked)
        },
        backgroundColor = MaterialTheme.colors.background
    )
}

@Composable
fun AppBarActions(
    onConfirmClicked: () -> Unit
) {
    var isAlertDialogOpen by remember { mutableStateOf(false) }

    ShowAlertDialog(
        title = stringResource(R.string.delete_all_alert_dialog_title),
        message = stringResource(R.string.delete_all_alert_dialog_message),
        isOpen = isAlertDialogOpen,
        onConfirmClicked = {
            onConfirmClicked()
            isAlertDialogOpen = false
        },
        onDismissClicked = { isAlertDialogOpen = false }
    )

    DeleteAllAction(onDeleteAllClicked = { isAlertDialogOpen = true })
}

@Composable
fun DeleteAllAction(
    onDeleteAllClicked: () -> Unit
) {
    var isDropdownMenuOpen by remember { mutableStateOf(false) }

    IconButton(onClick = { isDropdownMenuOpen = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.content_description_open_dropdown_menu),
            tint = MaterialTheme.colors.onBackground
        )
        DropdownMenu(
            expanded = isDropdownMenuOpen,
            onDismissRequest = { isDropdownMenuOpen = false }
        ) {
            DropdownMenuItem(onClick = {
                isDropdownMenuOpen = false
                onDeleteAllClicked()
            }) {
                Text(text = stringResource(R.string.delete_all_appbar_dropdown_menu_option))
            }
        }
    }
}