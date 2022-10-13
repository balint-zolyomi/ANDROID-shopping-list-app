package com.bzolyomi.shoppinglist.ui.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R

@Composable
fun AppBarOptionMore(
    dropdownItemTitle: String,
    alertDialogMessage: String,
    onConfirmClicked: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isAlertDialogOpen by remember { mutableStateOf(false) }

    IconButton(onClick = { isExpanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.content_description_open_dropdown_menu),
            tint = MaterialTheme.colors.onBackground
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(onClick = {
                isExpanded = false
                isAlertDialogOpen = true
            }) {
                Text(text = dropdownItemTitle)
            }
        }
    }

    if (isAlertDialogOpen) {
        AlertDialog(
            title = { Text(text = stringResource(R.string.delete_alert_dialog_title)) },
            text = { Text(text = alertDialogMessage) },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmClicked()
                        isAlertDialogOpen = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(text = stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { isAlertDialogOpen = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(text = stringResource(R.string.cancel_button))
                }
            },
            onDismissRequest = { isAlertDialogOpen = false }
        )
    }
}

@Composable
fun AppBarOptionToggleReorder(
    isReordering: Boolean,
    onReorderButtonToggled: () -> Unit
) {
    IconButton(onClick = onReorderButtonToggled) {
        if (!isReordering) {
            Icon(
                imageVector = Icons.Outlined.ChangeCircle,
                contentDescription = stringResource(
                    R.string.content_description_toggle_on_reorder_items_icon
                ),
                tint = MaterialTheme.colors.onBackground
            )
        } else {
            Icon(
                imageVector = Icons.Filled.ChangeCircle,
                contentDescription = stringResource(
                    R.string.content_description_toggle_off_reorder_icon
                ),
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
}
