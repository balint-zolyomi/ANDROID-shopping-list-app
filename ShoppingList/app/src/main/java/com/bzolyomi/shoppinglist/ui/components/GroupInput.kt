package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.util.Constants
import java.time.format.TextStyle

@Composable
fun GroupInput(
    groupName: String,
    isError: Boolean,
    onGroupNameChange: (String) -> Unit,
    onEraseGroupNameInputButtonClicked: () -> Unit,
    onNextInGroupNameInputClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        GroupNameInput(
            groupName = groupName,
            isError = isError,
            onGroupNameChange = { onGroupNameChange(it) },
            onEraseGroupNameInputButtonClicked = onEraseGroupNameInputButtonClicked,
            onNextInGroupNameInputClicked = onNextInGroupNameInputClicked
        )
    }
}

@Composable
fun GroupNameInput(
    groupName: String,
    isError: Boolean,
    onGroupNameChange: (String) -> Unit,
    onEraseGroupNameInputButtonClicked: () -> Unit,
    onNextInGroupNameInputClicked: () -> Unit
) {
    val inputTextStyle = if (isSystemInDarkTheme()) {
        LocalTextStyle.current.copy(color = Color.White)
    } else {
        LocalTextStyle.current
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = groupName,
        onValueChange = { onGroupNameChange(it) },
        label = { Text(text = stringResource(R.string.input_label_group_name)) },
        textStyle = inputTextStyle,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onNextInGroupNameInputClicked()
                if (!isError) defaultKeyboardAction(ImeAction.Next)
            }),
        trailingIcon = {
            if (groupName.isNotBlank()) {
                TrailingIconForErase(onEraseGroupNameInputButtonClicked)
            } else if (isError) {
                Icon(
                    Icons.Filled.Error,
                    contentDescription = ""
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