package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R

@Composable
fun GroupInput(
    groupName: String,
    onGroupNameChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        GroupNameInput(
            groupName = groupName,
            onGroupNameChange = { onGroupNameChange(it) }
        )
    }
}

@Composable
fun GroupNameInput(groupName: String, onGroupNameChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = groupName,
        onValueChange = { onGroupNameChange(it) },
        label = { Text(text = stringResource(R.string.input_label_group_name)) },
        singleLine = true
    )
}