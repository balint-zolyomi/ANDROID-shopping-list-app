package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
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
            AppBar(
                appBarTitle = stringResource(R.string.all_groups_screen_appbar_title),
                isShowingReorderIcon = false,
                isReordering = false,
                alertDialogMessage = stringResource(
                    R.string.delete_all_alert_dialog_message
                ),
                appBarDropDownTitle = stringResource(
                    R.string.delete_all_appbar_dropdown_menu_option_delete_all
                ),
                onDeleteClicked = {
                    for (groupWithList in shoppingGroupsWithLists) {
                        sharedViewModel.deleteGroup(groupId = groupWithList.group.groupId)
                        sharedViewModel.deleteItems(shoppingList = groupWithList.shoppingList)
                        sharedViewModel.deleteAllListOrders(groupId = groupWithList.group.groupId)
                    }
                },
                onReorderButtonToggled = {}
            )
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
                            shoppingList = shoppingGroupWithList.shoppingList,
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
    appBarTitle: String,
    isShowingReorderIcon: Boolean,
    isReordering: Boolean,
    appBarDropDownTitle: String,
    alertDialogMessage: String,
    onDeleteClicked: () -> Unit,
    onReorderButtonToggled: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = appBarTitle,
                color = MaterialTheme.colors.onBackground
            )
        },
        actions = {
            if (isShowingReorderIcon) {
                AppBarActionReorder(
                    isReordering = isReordering,
                    onReorderButtonToggled = onReorderButtonToggled
                )
            }
            AppBarActionDropdown(
                appBarDropDownTitle = appBarDropDownTitle,
                alertDialogMessage = alertDialogMessage,
                onConfirmClicked = onDeleteClicked
            )
        },
        backgroundColor = MaterialTheme.colors.background
    )
}

@Composable
fun AppBarActionReorder(
    isReordering: Boolean,
    onReorderButtonToggled: () -> Unit
) {

    IconButton(onClick = onReorderButtonToggled) {
        if (!isReordering) {
            Icon(
                imageVector = Icons.Outlined.ChangeCircle,
                contentDescription = stringResource(
                    R.string.content_description_toggle_on_reorder_items_icon
                ),
                tint = MaterialTheme.colors.onBackground
            )
        } else {
            Icon(
                imageVector = Icons.Filled.ChangeCircle,
                contentDescription = stringResource(
                    R.string.content_description_toggle_off_reorder_icon
                ),
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
fun AppBarActionDropdown(
    appBarDropDownTitle: String,
    alertDialogMessage: String,
    onConfirmClicked: () -> Unit
) {
    var isAlertDialogOpen by remember { mutableStateOf(false) }

    ShowAlertDialog(
        title = stringResource(R.string.delete_alert_dialog_title),
        message = alertDialogMessage,
        isOpen = isAlertDialogOpen,
        onConfirmClicked = {
            onConfirmClicked()
            isAlertDialogOpen = false
        },
        onDismissClicked = { isAlertDialogOpen = false }
    )

    DeleteAppBarAction(
        appBarDropDownTitle = appBarDropDownTitle,
        onDeleteClicked = { isAlertDialogOpen = true }
    )
}

@Composable
fun DeleteAppBarAction(
    appBarDropDownTitle: String,
    onDeleteClicked: () -> Unit
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
                onDeleteClicked()
            }) {
                Text(text = appBarDropDownTitle)
            }
        }
    }
}