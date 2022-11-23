package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.bzolyomi.shoppinglist.data.GroupWithList

@Composable
fun GroupScreen(
    groupWithList: GroupWithList
) {
    Text(text = "Screen: GroupScreen of group: ${groupWithList.group.groupName}")
}