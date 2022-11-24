package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.ui.components.AppBarOptionMore
import com.bzolyomi.shoppinglist.ui.components.cards.GroupAndItemsCard
import com.bzolyomi.shoppinglist.ui.theme.FloatingActionButtonTint
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.util.Constants.PADDING_SMALL
import com.bzolyomi.shoppinglist.util.Constants.PADDING_XX_LARGE
import com.bzolyomi.shoppinglist.SharedViewModel

@Composable
fun AllGroupsScreen(
    navigateToAddScreen: () -> Unit,
    navigateToGroupScreen: (groupId: Long?) -> Unit,
    sharedVM: SharedViewModel,
    modifier: Modifier
) {
    val shoppingGroupsWithLists by sharedVM.shoppingGroupsWithLists.collectAsState()

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.appbar_title_all_groups_screen)) },
                actions = {
                    AppBarOptionMore(
                        dropdownItemTitle = stringResource(
                            R.string.appbar_dropdown_menu_option_delete_all
                        ),
                        alertDialogMessage = stringResource(
                            R.string.alert_dialog_message_delete_all
                        ),
                        onConfirmClicked = {
                            for (groupWithList in shoppingGroupsWithLists) {
                                sharedVM.deleteGroup(groupWithList.group.groupId)
                                sharedVM.deleteItems(shoppingList = groupWithList.shoppingList)
                            }
                        }
                    )
                },
                backgroundColor = MaterialTheme.colors.background
            )
        },
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        content = {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = PADDING_SMALL,
                    end = PADDING_SMALL,
                    top = PADDING_MEDIUM,
                    bottom = PADDING_XX_LARGE
                ),
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
                            onOpenGroupIconClicked = {
                                navigateToGroupScreen(shoppingGroupWithList.group.groupId)
                            },
                            modifier = Modifier
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddScreen,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(
                        R.string.content_description_fab_add_group_and_item
                    ),
                    tint = FloatingActionButtonTint,
                )
            }
        }
    )
}