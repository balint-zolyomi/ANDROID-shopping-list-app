package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.data.ShoppingListEntity

@Composable
fun AllItemsText(
    showData: Boolean,
    shoppingListItems: List<ShoppingListEntity>
) {
    if (showData) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                items(items = shoppingListItems) {
                    Text(
                        text = it.itemName,
//                      However sometimes you need to deviate slightly from the selection of colors
//                      and font styles. In those situations it's better to base your color or style
//                      on an existing one.
//                      For this, you can modify a predefined style by using the copy function.
                        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
            }
        }
    }
}

@Composable
fun ShowItemsButton(
    onButtonClicked: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onButtonClicked) {
            Text(text = "Show items")
        }
    }
}