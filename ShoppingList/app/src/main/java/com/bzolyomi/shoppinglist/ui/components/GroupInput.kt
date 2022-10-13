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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM

@Composable
fun GroupNameInput(
    groupName: String,
    isError: Boolean,
    isEnabled: Boolean,
    inputTextStyle: TextStyle,
    onGroupNameChange: (String) -> Unit,
    onEraseGroupNameInputButtonClicked: () -> Unit,
    onNextInGroupNameInputClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
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
            ErrorText(text = stringResource(R.string.error_message_input_field))
        }
    }
}