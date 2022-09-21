package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.data.ShoppingListEntity
import com.bzolyomi.shoppinglist.ui.theme.ShoppingListTheme
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun ItemGroupScreen(
    sharedViewModel: SharedViewModel
) {
    val shoppingList by sharedViewModel.shoppingList.collectAsState()

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        GroupCard(title = "Spar", shoppingList = shoppingList)
        GroupCard(title = "Lidl", shoppingList = shoppingList)
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
        Row(modifier = Modifier
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