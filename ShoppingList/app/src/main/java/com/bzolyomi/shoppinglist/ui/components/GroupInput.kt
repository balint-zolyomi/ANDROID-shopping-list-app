package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.bzolyomi.shoppinglist.R

@Composable
fun GroupInput(
    groupName: String,
    onGroupNameChange: (String) -> Unit,
    onEraseGroupNameInputButtonClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        GroupNameInput(
            groupName = groupName,
            onGroupNameChange = { onGroupNameChange(it) },
            onEraseGroupNameInputButtonClicked = onEraseGroupNameInputButtonClicked
        )
    }
}

@Composable
fun GroupNameInput(
    groupName: String,
    onGroupNameChange: (String) -> Unit,
    onEraseGroupNameInputButtonClicked: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = groupName,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        onValueChange = { onGroupNameChange(it) },
        label = { Text(text = stringResource(R.string.input_label_group_name)) },
        trailingIcon = { TrailingIconForErase(onEraseGroupNameInputButtonClicked)},
        singleLine = true
    )
}