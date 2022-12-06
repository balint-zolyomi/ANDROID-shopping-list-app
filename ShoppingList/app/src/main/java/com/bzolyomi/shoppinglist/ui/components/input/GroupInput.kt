package com.bzolyomi.shoppinglist.ui.components.input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
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
import com.bzolyomi.shoppinglist.ui.components.EraseTrailingIcon
import com.bzolyomi.shoppinglist.ui.components.ErrorText

@Composable
fun GroupNameInput(
    groupName: String,
    isError: Boolean,
    isEnabled: Boolean,
    inputTextStyle: TextStyle,
    onGroupNameChange: (String) -> Unit,
    onEraseGroupNameInputButtonClicked: () -> Unit,
    onNextInGroupNameInputClicked: () -> Unit,
    modifier: Modifier
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
                    if (!validateGroupNameInput(groupName)) defaultKeyboardAction(ImeAction.Next)
                    onNextInGroupNameInputClicked()
                }),
            trailingIcon = {
                if (groupName.isNotBlank() && isEnabled) {
                    EraseTrailingIcon(onEraseTrailingIconClicked = onEraseGroupNameInputButtonClicked)
                } else if (isError) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = stringResource(
                            R.string.content_description_icon_error_input_field
                        )
                    )
                }
            },
            singleLine = true,
            isError = isError,
            modifier = modifier.fillMaxWidth()
        )
        if (isError) {
            ErrorText(text = stringResource(R.string.error_message_name_input_field))
        }
    }
}