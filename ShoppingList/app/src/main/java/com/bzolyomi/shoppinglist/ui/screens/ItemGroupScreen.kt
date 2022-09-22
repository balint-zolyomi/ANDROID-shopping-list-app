package com.bzolyomi.shoppinglist.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.data.ShoppingListEntity
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun ItemGroupScreen(
    sharedViewModel: SharedViewModel
) {
    val shoppingGroupsWithLists by sharedViewModel.shoppingGroupsWithLists.collectAsState()

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        for (shoppingGroupWithList in shoppingGroupsWithLists) {
            Log.d("debug-balint", shoppingGroupWithList.toString())

            GroupCard(
                title = shoppingGroupWithList.group.groupName,
                shoppingList = shoppingGroupWithList.shoppingList
            )
        }
    }
}

@Composable
fun GroupCard(title: String, shoppingList: List<ShoppingListEntity>) {

    var expanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colors.primary,
        elevation = 2.dp,
        modifier = Modifier.padding(
            vertical = 4.dp, horizontal = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.ExtraBold)
                )
                if (expanded) {
                    for (item in shoppingList) {
                        Text(text = item.itemName)
                    }
                }
            }
            OutlinedButton(
                onClick = { expanded = !expanded }
            ) {
                Text(text = if (expanded) "Show less" else "Show more")
            }
        }
    }
}