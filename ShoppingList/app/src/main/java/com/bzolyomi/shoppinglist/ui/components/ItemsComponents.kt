package com.bzolyomi.shoppinglist.ui.components

//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyListState
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.bzolyomi.shoppinglist.data.ShoppingListEntity
//
//@Composable
//fun AllItemsText(
//    showData: Boolean,
//    shoppingListItems: List<ShoppingListEntity>
//) {
//    if (showData) {
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            // The composable function rememberLazyListState creates an initial state for the list
//            // using rememberSaveable. When the Activity is recreated, the scroll state is
//            // maintained without you having to code anything.
//            LazyColumn(state = LazyListState(), modifier = Modifier.padding(10.dp)) {
//                items(items = shoppingListItems) {
//                    Row {
//                        Checkbox(checked = false, onCheckedChange = {})
//                        Text(
//                            text = it.itemName,
////                      However sometimes you need to deviate slightly from the selection of colors
////                      and font styles. In those situations it's better to base your color or style
////                      on an existing one.
////                      For this, you can modify a predefined style by using the copy function.
//                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.ExtraBold)
//                        )
//                        IconButton(onClick = {}) {
//                            Icon(Icons.Filled.Close, contentDescription = "Close")
//                        }
//                    }
//                }
//            }
//        }
//    }
//}