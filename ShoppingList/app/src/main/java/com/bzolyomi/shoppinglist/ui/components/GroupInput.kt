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

@Composable
fun GroupInput(
    groupName: String,
    groupId: Long?,
    isError: Boolean,
    onGroupNameChange: (String) -> Unit,
    onEraseGroupNameInputButtonClicked: () -> Unit,
    onNextInGroupNameInputClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        GroupNameInput(
            groupName = groupName,
            isError = isError,
            groupId = groupId,
            onGroupNameChange = { onGroupNameChange(it) },
            onEraseGroupNameInputButtonClicked = onEraseGroupNameInputButtonClicked,
            onNextInGroupNameInputClicked = onNextInGroupNameInputClicked
        )
    }
}

@Composable
fun GroupNameInput(
    groupName: String,
    groupId: Long?,
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

    val isEnabled = groupId == -1L

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = groupName,
        enabled = isEnabled,
        onValueChange = { onGroupNameChange(it) },
        label = {
            Text(text = stringResource(R.string.input_label_group_name))
        },
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
            if (groupName.isNotBlank() && isEnabled) {
                TrailingIconForErase(onEraseGroupNameInputButtonClicked)
            } else if (isError) {
                Icon(
                    Icons.Filled.Error,
                    contentDescription = stringResource(
                        R.string.content_description_error_icon_input_field
                    )
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