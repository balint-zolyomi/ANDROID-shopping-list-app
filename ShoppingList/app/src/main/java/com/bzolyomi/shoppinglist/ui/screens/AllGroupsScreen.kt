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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.data.GroupWithList
import com.bzolyomi.shoppinglist.ui.components.GroupAndItemsCard
import com.bzolyomi.shoppinglist.ui.components.ShowAlertDialog

@Composable
fun AllGroupsScreen(
    shoppingGroupsWithLists: List<GroupWithList>,
    onAddAllFABClicked: () -> Unit,
    onOpenGroupIconClicked: (groupId: Long?) -> Unit,
    onDeleteAllClicked: () -> Unit,
    modifier: Modifier
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(onDeleteAllClicked = onDeleteAllClicked)
        },
        modifier = modifier
            .fillMaxSize(),
        content = {
            LazyColumn(
                contentPadding = WindowInsets.systemBars
                    .only(WindowInsetsSides.Vertical)
                    .add(
                        WindowInsets(left = 8.dp, right = 8.dp, top = 16.dp, bottom = 72.dp)
                    )
                    .asPaddingValues(),
                modifier = modifier
                    .fillMaxSize()
            ) {
                items(
                    items = shoppingGroupsWithLists
                ) { shoppingGroupWithList ->
                    GroupAndItemsCard(
                        titleGroupName = shoppingGroupWithList.group.groupName,
                        shoppingList = shoppingGroupWithList.shoppingList,
                        onOpenGroupIconClicked = {
                            onOpenGroupIconClicked(shoppingGroupWithList.group.groupId)
                        },
                        modifier = Modifier
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddAllFABClicked,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    tint = Color.White
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
        backgroundColor = MaterialTheme.colors.primary
    )
}

@Composable
fun AppBarActions(
    onConfirmClicked: () -> Unit
) {
    var isAlertDialogOpen by remember { mutableStateOf(false) }

    ShowAlertDialog(
        title = stringResource(R.string.delete_all_alert_dialog_title),
        message = stringResource(R.string.delete_all_aler_dialog_message),
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
            imageVector = Icons.Filled.MoreVert, contentDescription = "",
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