package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bzolyomi.shoppinglist.data.GroupWithList
import com.bzolyomi.shoppinglist.ui.components.GroupCard
import com.bzolyomi.shoppinglist.util.Constants.PADDING_X_SMALL

@Composable
fun AllGroupsScreen(
    shoppingGroupsWithLists: List<GroupWithList>,
    onAddAllFABClicked: () -> Unit,
    onOpenGroupIconClicked: (groupId: Long?) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {},
        content = {
            LazyColumn(
                modifier = Modifier.padding(vertical = PADDING_X_SMALL)
            ) {
                items(
                    items = shoppingGroupsWithLists
                ) { shoppingGroupWithList ->
                    GroupCard(
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
                onClick = onAddAllFABClicked
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        })
}
