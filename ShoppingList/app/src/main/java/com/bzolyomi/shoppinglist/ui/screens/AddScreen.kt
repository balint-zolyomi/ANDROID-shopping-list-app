package com.bzolyomi.shoppinglist.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.bzolyomi.shoppinglist.R
import com.bzolyomi.shoppinglist.ui.components.input.*
import com.bzolyomi.shoppinglist.ui.components.showShortToast
import com.bzolyomi.shoppinglist.ui.theme.FloatingActionButtonTint
import com.bzolyomi.shoppinglist.util.Constants.GROUP_UNSELECTED
import com.bzolyomi.shoppinglist.util.Constants.PADDING_MEDIUM
import com.bzolyomi.shoppinglist.viewmodels.SharedViewModel

@Composable
fun AddAllScreen(
    groupId: Long?,
    navigateToHomeScreen: () -> Unit,
    navigateToGroupScreen: () -> Unit,
    sharedViewModel: SharedViewModel,
    modifier: Modifier
) {
    BackHandler {
        if (groupId == GROUP_UNSELECTED) {
            navigateToHomeScreen()
            sharedViewModel.flushGroupGUI()
            sharedViewModel.clearItemsList()
        } else {
            navigateToGroupScreen()
        }
        sharedViewModel.flushItemGUI()
    }

    val inputTextStyle = if (isSystemInDarkTheme()) {
        LocalTextStyle.current.copy(color = Color.White)
    } else {
        LocalTextStyle.current
    }

    val context = LocalContext.current
    val itemAddedToastMessage = stringResource(R.string.toast_message_item_added)

    val groupName by sharedViewModel.groupName
    val itemName by sharedViewModel.itemName
    val itemQuantity by sharedViewModel.itemQuantity
    val itemUnit by sharedViewModel.itemUnit

    var isGroupNameError by rememberSaveable { mutableStateOf(false) }
    var isItemNameError by rememberSaveable { mutableStateOf(false) }
    var isItemQuantityError by rememberSaveable { mutableStateOf(false) }

    var isAnyError by rememberSaveable { mutableStateOf(false) }

    fun validateAll() {
        isGroupNameError = validateGroupNameInput(groupName)
        isItemNameError = validateItemNameInput(itemName)
        isItemQuantityError = validateItemQuantityInput(itemQuantity)
        isAnyError = isGroupNameError || isItemNameError || isItemQuantityError
    }

    fun onSubmit() {
        validateAll()
        if (!isAnyError) {
            if (groupId == GROUP_UNSELECTED) navigateToHomeScreen() else navigateToGroupScreen()
            sharedViewModel.createWithCoroutines()
            showShortToast(context = context, message = itemAddedToastMessage)
        }
    }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    if (groupId == GROUP_UNSELECTED) {
                        Text(text = stringResource(R.string.top_appbar_title_add_group_and_items))
                    } else {
                        Text(
                            text = stringResource(
                                R.string.top_appbar_title_add_items
                            )
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.background
            )
        },
        content = {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(PADDING_MEDIUM)
            ) {
                GroupNameInput(
                    groupName = groupName,
                    isError = isGroupNameError,
                    isEnabled = groupId == GROUP_UNSELECTED,
                    inputTextStyle = inputTextStyle,
                    onGroupNameChange = {
                        isGroupNameError = validateGroupNameInput(it)
                        // no error OR user wants to delete the input field with TrailingIcon
                        if (!isGroupNameError || it == "") sharedViewModel.setGroupName(it)
                    },
                    onEraseGroupNameInputButtonClicked = {
                        sharedViewModel.setGroupName("")
                        isGroupNameError = validateGroupNameInput(sharedViewModel.groupName.value)
                    },
                    onNextInGroupNameInputClicked = {
                        isGroupNameError = validateGroupNameInput(groupName)
                    }
                )
                ItemNameInput(
                    itemName = itemName,
                    isError = isItemNameError,
                    inputTextStyle = inputTextStyle,
                    onItemNameChange = {
                        isItemNameError = validateItemNameInput(it)
                        if (!isItemNameError || it == "") sharedViewModel.setItemName(it)
                    },
                    onEraseItemNameInputButtonClicked = {
                        sharedViewModel.setItemName("")
                        isItemNameError = validateItemNameInput(sharedViewModel.itemName.value)
                    }
                ) {
                    isItemNameError = validateItemNameInput(itemName)
                }
                ItemQuantityInput(
                    itemQuantity = itemQuantity,
                    isError = isItemQuantityError,
                    inputTextStyle = inputTextStyle,
                    onItemQuantityChange = { sharedViewModel.setItemQuantity(it) },
                    onEraseItemQuantityInputButtonClicked = {
                        sharedViewModel.setItemQuantity("")
                        isItemQuantityError = validateItemQuantityInput(
                            sharedViewModel.itemQuantity.value
                        )
                    },
                    onNextInItemQuantityInputClicked = {
                        isItemQuantityError = validateItemQuantityInput(itemQuantity)
                    }
                )
                ItemUnitInput(
                    itemUnit = itemUnit,
                    inputTextStyle = inputTextStyle,
                    onItemUnitChange = { sharedViewModel.setItemUnit(it) },
                    onEraseItemUnitInputButtonClicked = {
                        sharedViewModel.setItemUnit("")
                    },
                    onDone = { onSubmit() }
                )
                if (groupId == GROUP_UNSELECTED) {
                    AddItemButton(onAddItemButtonClicked = {
                        validateAll()
                        if (!isAnyError) {
                            val tempGroupName = groupName
                            sharedViewModel.createWithCoroutines()
                            sharedViewModel.setGroupName(tempGroupName)
                            showShortToast(context = context, message = itemAddedToastMessage)
                        }
                    })
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onSubmit() },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(R.string.content_description_fab),
                    tint = FloatingActionButtonTint
                )
            }
        }
    )
}

@Composable
fun AddItemButton(onAddItemButtonClicked: () -> Unit) {
    Button(
        onClick = onAddItemButtonClicked,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        modifier = Modifier.padding(PADDING_MEDIUM)
    ) {
        Text(text = stringResource(R.string.add_item_button_text))
    }
}
