package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.util.Constants

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
    var isError by rememberSaveable { mutableStateOf(false) }

    fun validate(input: String) {
        isError = input.isBlank()
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = groupName,
        onValueChange = {

            isError = if (groupName == "" && it == " ") {
                true
            } else {
                onGroupNameChange(it)
                false
            }
        },
        label = { Text(text = stringResource(R.string.input_label_group_name)) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            validate(groupName)
            if (!isError) defaultKeyboardAction(ImeAction.Next)
        }),
        trailingIcon = {
            if (groupName.isNotBlank()) {
                TrailingIconForErase(onEraseGroupNameInputButtonClicked)
            } else if (isError) {
                Icon(
                    Icons.Filled.Error,
                    contentDescription = "",
                    tint = MaterialTheme.colors.error
                )
            }
        },
        singleLine = true,
        isError = isError
    )
    if (isError) {
        Text(
            text = stringResource(R.string.error_message_input_field),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.error,
            modifier = Modifier.padding(start = Constants.PADDING_MEDIUM)
        )
    }
}