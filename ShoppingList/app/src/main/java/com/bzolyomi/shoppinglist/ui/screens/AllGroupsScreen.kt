package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.data.ShoppingListEntity
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun AllGroupsScreen(
    sharedViewModel: SharedViewModel,
    onNavigateToAddAllScreen: () -> Unit,
    onNavigateToItemsOfGroupScreen: () -> Unit
) {
    val shoppingGroupsWithLists by sharedViewModel.shoppingGroupsWithLists.collectAsState()

    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState,
        topBar = {},
        content = {
            LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                items(items = shoppingGroupsWithLists) { shoppingGroupWithList ->
                    GroupCard(
                        title = shoppingGroupWithList.group.groupName,
                        shoppingList = shoppingGroupWithList.shoppingList,
                        onNavigateToItemsOfGroupScreen
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddAllScreen
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        })
}

@Composable // TODO: stateless possible? 
fun GroupCard(
    title: String,
    shoppingList: List<ShoppingListEntity>,
    onNavigateToItemsOfGroupScreen: () -> Unit
) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    val categoryBottomDp by animateDpAsState(
        if (expanded) 0.dp else 12.dp
    )
    val cardElevation = if (expanded) 10.dp else 4.dp

    Card(
        elevation = cardElevation,
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 24.dp, top = 12.dp, end = 12.dp, bottom = categoryBottomDp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h5.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Column {
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Surface(shape = CircleShape, modifier = Modifier.size(25.dp)) {
                            Icon(
                                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = "",
                                tint = MaterialTheme.colors.secondary
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = expanded,
//                visible = true,
                enter = expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    initialAlpha = 0.0f,
                    animationSpec = tween(durationMillis = 200)
                ),
                exit = shrinkVertically() + fadeOut(animationSpec = tween(durationMillis = 100))
            ) {
                Row(
                    modifier = Modifier.padding(
                        start = 36.dp,
                        top = 0.dp,
                        end = 24.dp,
                        bottom = 24.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        for (item in shoppingList) {
                            Text(
                                text = item.itemName + " -- " + item.itemQuantity + " " + item.itemUnit,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = onNavigateToItemsOfGroupScreen) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "",
                            tint = MaterialTheme.colors.secondary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun preview() {
    GroupCard(
        title = "Spar",
        shoppingList = listOf(ShoppingListEntity(1, 1, "", 1.0f, "")),
        onNavigateToItemsOfGroupScreen = {}
    )
}