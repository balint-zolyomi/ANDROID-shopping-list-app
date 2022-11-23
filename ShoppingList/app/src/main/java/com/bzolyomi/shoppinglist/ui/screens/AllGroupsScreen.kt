package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bzolyomi.shoppinglist.SharedViewModel

@Composable
fun AllGroupsScreen(
    sharedVM: SharedViewModel,
    navigateToAddScreen: () -> Unit,
    navigateToGroupScreen: (groupId: Long?) -> Unit
) {

    val groupsWithList by sharedVM.shoppingGroupsWithLists.collectAsState()

    Column {
        for (groupWithList in groupsWithList) {
            Text(text = groupWithList.toString())
            Button(
                onClick = {
                    navigateToGroupScreen(groupWithList.group.groupId)
                }
            ) {
                Text(text = "To GroupScreen")
            }
        }
        Row {
            Spacer(modifier = Modifier.weight(1F))
            Button(onClick = navigateToAddScreen) {
                Text(text = "To AddScreen")
            }
        }
    }
}