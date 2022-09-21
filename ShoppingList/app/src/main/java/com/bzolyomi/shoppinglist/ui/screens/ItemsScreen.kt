package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.bzolyomi.shoppinglist.ui.components.AllItemsText
import com.bzolyomi.shoppinglist.ui.components.ShowItemsButton
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun ItemsScreen(
    sharedViewModel: SharedViewModel
) {
    var showData by rememberSaveable { mutableStateOf(false) }
    val shoppingList by sharedViewModel.shoppingList.collectAsState()

    if (!showData) {
        ShowItemsButton { showData = true }
    }
    AllItemsText(showData = showData, shoppingListItems = shoppingList)
}