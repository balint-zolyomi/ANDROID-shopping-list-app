package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.data.GroupWithList
import com.bzolyomi.shoppinglist.ui.components.GroupAndItemsCard
import com.bzolyomi.shoppinglist.util.Constants.PADDING_LARGE
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_SMALL

@Composable
fun AllGroupsScreen(
    shoppingGroupsWithLists: List<GroupWithList>,
    onAddAllFABClicked: () -> Unit,
    onOpenGroupIconClicked: (groupId: Long?) -> Unit,
//    contentPadding: PaddingValues,
    modifier: Modifier
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {},
        modifier = modifier
            .fillMaxSize(),
//            .background(MaterialTheme.colors.background),
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
//                    .background(MaterialTheme.colors.background)
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
