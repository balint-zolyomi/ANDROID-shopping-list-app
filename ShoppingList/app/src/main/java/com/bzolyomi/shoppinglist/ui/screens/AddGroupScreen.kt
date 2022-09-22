package com.bzolyomi.shoppinglist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun AddGroupScreen(
    sharedViewModel: SharedViewModel
) {
    val groupId: String = sharedViewModel.groupId
    val groupName: String = sharedViewModel.groupName

    Column(modifier = Modifier.fillMaxSize()) {
        GroupIdInput(groupId, onGroupIdChange = {
            sharedViewModel.groupId = it
        })
        GroupNameInput(groupName, onGroupNameChange = {
            sharedViewModel.groupName = it
        })
        SubmitButton(onSubmitButtonClicked = {
            sharedViewModel.createGroup()
        })
    }
}

@Composable
fun SubmitButton(onSubmitButtonClicked: () -> Unit) {
    Button(onClick = onSubmitButtonClicked) {
        Text(text = "ADD")
    }
}

@Composable
fun GroupNameInput(groupName: String, onGroupNameChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = groupName,
        onValueChange = { onGroupNameChange(it) },
        label = { Text(text = "Group name") },
        singleLine = true
    )
}

@Composable
fun GroupIdInput(groupId: String, onGroupIdChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = groupId,
        onValueChange = { onGroupIdChange(it) },
        label = { Text(text = "Group id") },
        singleLine = true
    )
}