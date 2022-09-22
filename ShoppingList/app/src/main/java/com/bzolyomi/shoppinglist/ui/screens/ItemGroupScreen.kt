package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bzolyomi.shoppinglist.data.ShoppingListEntity
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun ItemGroupScreen(
    sharedViewModel: SharedViewModel
) {
    val shoppingGroupsWithLists by sharedViewModel.shoppingGroupsWithLists.collectAsState()

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = shoppingGroupsWithLists) { shoppingGroupWithList ->
            GroupCard(
                title = shoppingGroupWithList.group.groupName,
                shoppingList = shoppingGroupWithList.shoppingList
            )
        }
    }
}

@Composable
fun GroupCard(title: String, shoppingList: List<ShoppingListEntity>) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    val categoryBottomDp by animateDpAsState(
        if (expanded) 0.dp else 24.dp
    )

    Surface(
        color = MaterialTheme.colors.primary,
        elevation = 2.dp,
        modifier = Modifier.padding(
            vertical = 4.dp, horizontal = 8.dp
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = categoryBottomDp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
                Column {
                    OutlinedButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Text(text = if (expanded) "Show less" else "Show more")
                    }
                }
            }
            AnimatedVisibility(
                visible = expanded,
                enter =  expandVertically(
                    // Expand from the top.
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.0f,
                    animationSpec = tween(durationMillis = 200)
                ),
                exit = shrinkVertically() + fadeOut(animationSpec = tween(durationMillis = 100))
            ) {
                Row(
                    modifier = Modifier.padding(
                        start = 24.dp,
                        top = 0.dp,
                        end = 24.dp,
                        bottom = 24.dp
                    )
                ) {
                    Column {
                        for (item in shoppingList) {
                            Text(text = item.itemName)
                        }
                    }
                }
            }
        }
    }
}